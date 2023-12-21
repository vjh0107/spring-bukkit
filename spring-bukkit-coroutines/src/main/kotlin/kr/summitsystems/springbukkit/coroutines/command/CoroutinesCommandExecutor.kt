package kr.summitsystems.springbukkit.coroutines.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.summitsystems.springbukkit.command.*
import org.springframework.core.KotlinDetector
import org.springframework.core.convert.ConversionService
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

class CoroutinesCommandExecutor(
    private val coroutineScope: CoroutineScope,
    commandMappingRegistry: CommandMappingRegistry,
    commandFeedbackSource: CommandFeedbackSource,
    commandContextHolder: CommandContextHolder,
    conversionService: ConversionService
) : GenericCommandExecutor(commandMappingRegistry, commandFeedbackSource, commandContextHolder, conversionService) {
    override fun invokeMapping(
        mapping: RegistrableCommandMapping,
        context: CommandContext,
        convertedParameters: Array<Any?>
    ) {
        if (KotlinDetector.isSuspendingFunction(mapping.mappingMethod)) {
            coroutineScope.launch {
                commandContextHolder.setContext(context)
                mapping
                    .mappingMethod
                    .kotlinFunction!!
                    .callSuspend(mapping.controllerInstance, context, *convertedParameters)
            }
        } else {
            super.invokeMapping(mapping, context, convertedParameters)
        }
    }
}