package kr.summitsystems.springbukkit.core.command

import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConversionService
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class CommandConfiguration {
    @ConditionalOnMissingBean
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandTabCompletionProvider(
        commandMappingRegistry: CommandMappingRegistry,
        commandArgumentConversionService: CommandArgumentConversionService
    ): CommandTabCompletionProvider {
        return CommandTabCompletionProviderImpl(commandMappingRegistry, commandArgumentConversionService)
    }

    @ConditionalOnMissingBean
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandExecutor(
        commandMappingRegistry: CommandMappingRegistry,
        commandFeedbackSource: CommandFeedbackSource,
        commandContextHolder: CommandContextHolder,
        commandArgumentConversionService: CommandArgumentConversionService
    ): CommandExecutor {
        return GenericCommandExecutor(
            commandMappingRegistry,
            commandFeedbackSource,
            commandContextHolder,
            commandArgumentConversionService
        )
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandExceptionHandlerRegistry(): CommandExceptionHandlerRegistry {
        return CommandExceptionHandlerRegistryImpl()
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandContextHolder(): CommandContextHolder {
        return ThreadCommandContextHolder()
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandMappingRegistry(): CommandMappingRegistry {
        return CommandMappingRegistry()
    }
}