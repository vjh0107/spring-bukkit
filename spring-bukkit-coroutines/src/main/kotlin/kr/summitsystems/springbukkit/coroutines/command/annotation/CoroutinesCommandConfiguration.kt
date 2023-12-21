package kr.summitsystems.springbukkit.coroutines.command.annotation

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.command.CommandContextHolder
import kr.summitsystems.springbukkit.command.CommandExecutor
import kr.summitsystems.springbukkit.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.command.CommandMappingRegistry
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConversionService
import kr.summitsystems.springbukkit.coroutines.command.CoroutinesCommandExecutor
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class CoroutinesCommandConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun coroutinesCommandExecutor(
        @Qualifier("commandCoroutineScope") commandCoroutineScope: CoroutineScope,
        commandMappingRegistry: CommandMappingRegistry,
        commandFeedbackSource: CommandFeedbackSource,
        commandContextHolder: CommandContextHolder,
        commandArgumentConversionService: CommandArgumentConversionService
    ): CommandExecutor {
        return CoroutinesCommandExecutor(
            commandCoroutineScope,
            commandMappingRegistry,
            commandFeedbackSource,
            commandContextHolder,
            commandArgumentConversionService
        )
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandCoroutineScope(
        pluginCoroutineScope: CoroutineScope,
        commandConfigurers: ObjectProvider<CoroutinesCommandConfigurer>
    ): CoroutineScope {
        return commandConfigurers.singleOrNull()?.getCoroutineScope() ?: pluginCoroutineScope
    }
}