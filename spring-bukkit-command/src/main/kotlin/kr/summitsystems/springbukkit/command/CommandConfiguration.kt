package kr.summitsystems.springbukkit.command

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.core.convert.ConversionService

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class CommandConfiguration {
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(CommandExecutor::class)
    @Bean
    fun defaultCommandExecutor(
        commandMappingRegistry: CommandMappingRegistry,
        commandFeedbackSource: CommandFeedbackSource,
        commandContextHolder: CommandContextHolder,
        commandArgumentConversionService: ConversionService
    ): CommandExecutor {
        return GenericCommandExecutor(
            commandMappingRegistry,
            commandFeedbackSource,
            commandContextHolder,
            commandArgumentConversionService
        )
    }
}