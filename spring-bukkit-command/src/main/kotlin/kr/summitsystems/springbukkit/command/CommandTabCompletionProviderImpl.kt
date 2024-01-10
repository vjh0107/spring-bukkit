package kr.summitsystems.springbukkit.command

import kr.summitsystems.springbukkit.command.convert.CommandArgumentConversionService
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverterAdapter
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.core.annotation.OrderUtils
import org.springframework.core.convert.TypeDescriptor
import org.springframework.stereotype.Component
import kotlin.reflect.full.allSuperclasses

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
class CommandTabCompletionProviderImpl(
    private val commandMappingRegistry: CommandMappingRegistry,
    private val commandArgumentConversionService: CommandArgumentConversionService
)  : CommandTabCompletionProvider {
    override fun provideTabComplete(inputBuffer: String): List<String> {
        val inputQualifier = inputBuffer
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
                    return emptyList()
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
                }.getOrNull(index - 1)?.type ?: return emptyList()

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
                        val adapterDeepSearched = commandArgumentConversionService.findCommandArgumentConverterAdapter(
                            TypeDescriptor.valueOf(clazz))
                        if (adapterDeepSearched != null) {
                            result.add(adapterDeepSearched)
                        }
                    }
                    val candidateConverterAdapter = result.sortedBy { OrderUtils.getOrder(it::class.java) }.firstOrNull()
                    if (candidateConverterAdapter != null) {
                        invokeAdapterProvideCompletes(candidateConverterAdapter, parameterType)
                    } else {
                        return emptyList()
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

        return completions
    }

    private fun invokeAdapterProvideCompletes(adapterInstance: Any, parameterType: Class<*>): Collection<String> {
        @Suppress("UNCHECKED_CAST")
        return AopProxyUtils.ultimateTargetClass(adapterInstance)
            .getMethod("provideCompletes", Class::class.java)
            .invoke(adapterInstance, parameterType) as Collection<String>
    }
}