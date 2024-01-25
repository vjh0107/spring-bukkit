package kr.summitsystems.springbukkit.kotlinx.coroutines.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.summitsystems.springbukkit.command.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Role
import org.springframework.core.KotlinDetector
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

@Primary
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
class CoroutinesCommandExecutor(
    @Qualifier("commandCoroutineScope") private val coroutineScope: CoroutineScope,
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