package kr.summitsystems.springbukkit.command

import kr.summitsystems.springbukkit.command.annotation.CommandAuthorize
import kr.summitsystems.springbukkit.core.util.extension.getLocale
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.ConverterNotFoundException
import org.springframework.core.convert.TypeDescriptor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

open class GenericCommandExecutor(
    protected val commandMappingRegistry: CommandMappingRegistry,
    protected val commandFeedbackSource: CommandFeedbackSource,
    protected val commandContextHolder: CommandContextHolder,
    protected val commandArgumentConversionService: ConversionService
) : CommandExecutor {
    override fun executeCommand(sender: CommandSender, commandName: String, args: Array<String>) {
        val inputQualifier = "${commandName}.${args.joinToString(".")}."
        val allMappings = commandMappingRegistry.findAllByRoot(commandName)
        val commandMappings = allMappings.filter {
            inputQualifier.startsWith(it.qualifier + ".")
        }
        val targetMapping = commandMappings.singleOrNull() ?: commandMappingRegistry.find(commandName)
        if (targetMapping == null) {
            allMappings.forEach { mapping ->
                sender.sendMessage(commandFeedbackSource.getUsageMessage(mapping, sender.getLocale()))
            }
            return
        }

        val convertedParameters: MutableMap<Int, Any?> = mutableMapOf()
        var context: CommandContext = createCommandContext(sender, commandName, targetMapping, convertedParameters)
        commandContextHolder.setContext(context)

        if (commandMappingRegistry.find(commandName) == null) {
            if (commandMappings.isEmpty() || commandMappings.singleOrNull() == null) {
                val mappingsForPrintUsage = allMappings.filter {
                    it.qualifier.startsWith(inputQualifier)
                }
                if (mappingsForPrintUsage.isEmpty()) {
                    allMappings.forEach { mapping ->
                        sender.sendMessage(commandFeedbackSource.getUsageMessage(mapping, sender.getLocale()))
                    }
                    return
                } else {
                    mappingsForPrintUsage.forEach { mapping ->
                        sender.sendMessage(commandFeedbackSource.getUsageMessage(mapping, sender.getLocale()))
                    }
                    return
                }
            }
        }

        val parameters = CommandAopUtils.extractCommandParameters(targetMapping.mappingMethod.kotlinFunction!!)
        parameters.forEachIndexed map@{ index, parameter ->
            val argumentValue = args.getOrNull(index + targetMapping.getSize() - 1)
            context = createCommandContext(
                sender,
                commandName,
                targetMapping,
                parameters.mapIndexed { index0, parameter0 ->
                    index0 to parameter0
                }.toMap()
            )
            if (argumentValue == null && (parameter.type.isMarkedNullable || parameter.isOptional)) {
                convertedParameters[index] = null
                return@map
            } else if (argumentValue == null) {
                sender.sendMessage(commandFeedbackSource.getUsageMessage(targetMapping, sender.getLocale()))
                return
            }
            commandContextHolder.setContext(context)
            val convertedParameter =
                convertCommandArgument(argumentValue, parameter.type.jvmErasure.javaObjectType, context)
            if (convertedParameter == null) {
                sender.sendMessage(commandFeedbackSource.getUsageMessage(targetMapping, sender.getLocale()))
                return
            }
            convertedParameters[index] = convertedParameter
        }
        if (checkPermission(sender, targetMapping.mappingMethod)) {
            try {
                invokeMapping(targetMapping, context, convertedParameters.values.toTypedArray())
            } catch (exception: InvocationTargetException) {
                throw exception.cause ?: throw exception
            }

        } else {
            sender.sendMessage(commandFeedbackSource.getPermissionDeniedMessage(targetMapping, sender.getLocale()))
        }

        return
    }

    private fun createCommandContext(
        sender: CommandSender,
        label: String,
        mapping: RegistrableCommandMapping,
        parameters: Map<Int, Any?>
    ): CommandContext {
        return CommandContextImpl(sender, label, mapping, commandMappingRegistry, commandFeedbackSource, parameters)
    }

    private fun convertCommandArgument(parameterStringValue: String, type: Class<*>, context: CommandContext): Any? {
        return if (commandArgumentConversionService.canConvert(CommandArgument::class.java, type)) {
            val commandArgument = CommandArgument(parameterStringValue, context)
            commandArgumentConversionService.convert(commandArgument, type)
        } else if (commandArgumentConversionService.canConvert(String::class.java, type)) {
            commandArgumentConversionService.convert(parameterStringValue, type)
        } else {
            throw ConverterNotFoundException(
                TypeDescriptor.valueOf(CommandArgument::class.java),
                TypeDescriptor.valueOf(type)
            )
        }
    }

    private fun checkPermission(sender: CommandSender, mappingMethod: Method): Boolean {
        val annotation = AnnotationUtils.getAnnotation(mappingMethod, CommandAuthorize::class.java) ?: return true

        if (sender !is Player) {
            return true
        }
        if (!sender.isOp && annotation.isOp) {
            return false
        }
        if (annotation.permission == "" || sender.hasPermission(annotation.permission)) {
            return true
        }
        throw CommandException.Unexpected()
    }

    protected open fun invokeMapping(
        mapping: RegistrableCommandMapping,
        context: CommandContext,
        convertedParameters: Array<Any?>
    ) {
        commandContextHolder.setContext(context)
        mapping.mappingMethod.invoke(mapping.controllerInstance, context, *convertedParameters)
    }
}