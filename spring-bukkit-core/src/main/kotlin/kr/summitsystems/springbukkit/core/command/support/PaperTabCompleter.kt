package kr.summitsystems.springbukkit.core.command.support

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
import kr.summitsystems.springbukkit.core.command.CommandMappingRegistry
import kr.summitsystems.springbukkit.core.command.CommandAopUtils
import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConversionService
import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConverterAdapter
import kr.summitsystems.springbukkit.core.listener.annotation.BukkitListener
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.core.annotation.OrderUtils
import org.springframework.core.convert.TypeDescriptor
import kotlin.reflect.full.allSuperclasses

class PaperTabCompleter(
    private val commandMappingRegistry: CommandMappingRegistry,
    private val commandArgumentConversionService: CommandArgumentConversionService
) {
    @BukkitListener
    fun onTabComplete(event: AsyncTabCompleteEvent) {
        if (!event.isCommand || event.buffer.indexOf(' ') == -1) {
            return
        }

        val inputQualifier = event.buffer
            .removePrefix("/")
            .replace(" ", ".")
        val mappingPackage = inputQualifier
            .split(".")
            .dropLast(1)
            .joinToString(".")
        val mappings = commandMappingRegistry.findAllMappingQualifierStartsWith(inputQualifier)
        val completions = if (mappings.isNotEmpty()) {
            mappings.map { mapping ->
                val completion = mapping.qualifier
                    .removePrefix(mappingPackage)
                    .removePrefix(".")
                    .split(".")
                    .first()
                completion
            }
        } else {
            val rootLabel = inputQualifier
                .split(".")
                .first()
            var mapping = commandMappingRegistry.findAllByRoot(rootLabel)
                .singleOrNull { mapping ->
                    inputQualifier.startsWith(mapping.qualifier)
                }
            if (mapping == null) {
                val mayRoot = commandMappingRegistry.find(rootLabel)
                if (mayRoot != null) {
                    mapping = mayRoot
                } else {
                    return
                }
            }
            val index = inputQualifier
                .removePrefix(mapping.qualifier)
                .removePrefix(".")
                .split(".")
                .size
            val parameterType = mapping
                .mappingMethod
                .let {
                    CommandAopUtils.extractCommandParameters(it)
                }.getOrNull(index - 1)?.type ?: return

            val converter = commandArgumentConversionService.findCommandArgumentConverter(TypeDescriptor.valueOf(parameterType))
            val completions = if (converter != null) {
                converter.provideCompletes()
            } else if (commandArgumentConversionService.canConvert(String::class.java, parameterType)) {
                val adapter = commandArgumentConversionService.findCommandArgumentConverterAdapter(TypeDescriptor.valueOf(parameterType))
                if (adapter == null) {
                    // Deep Search Start
                    val result: MutableList<CommandArgumentConverterAdapter<*>> = mutableListOf()
                    parameterType.kotlin.allSuperclasses.forEach {
                        val clazz = it.javaObjectType
                        val adapterDeepSearched = commandArgumentConversionService.findCommandArgumentConverterAdapter(TypeDescriptor.valueOf(clazz))
                        if (adapterDeepSearched != null) {
                            result.add(adapterDeepSearched)
                        }
                    }
                    val candidateConverterAdapter = result.sortedBy { OrderUtils.getOrder(it::class.java) }.firstOrNull()
                    if (candidateConverterAdapter != null) {
                        invokeAdapterProvideCompletes(candidateConverterAdapter, parameterType)
                    } else {
                        return
                    }
                } else {
                    invokeAdapterProvideCompletes(adapter, parameterType)
                }
            } else {
                emptyList()
            }
            completions
        }.filter { completion ->
            val lastArgument = inputQualifier.split(".").last()
            completion.startsWith(lastArgument)
        }

        val wrappedCompletions = completions.map { completion ->
            AsyncTabCompleteEvent.Completion.completion(completion)
        }

        event.completions(wrappedCompletions)
        event.isHandled = true
    }

    private fun invokeAdapterProvideCompletes(adapterInstance: Any, parameterType: Class<*>): Collection<String> {
        @Suppress("UNCHECKED_CAST")
        return AopProxyUtils.ultimateTargetClass(adapterInstance)
            .getMethod("provideCompletes", Class::class.java)
            .invoke(adapterInstance, parameterType) as Collection<String>
    }
}