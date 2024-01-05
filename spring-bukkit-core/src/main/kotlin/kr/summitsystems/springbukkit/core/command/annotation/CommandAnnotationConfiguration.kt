package kr.summitsystems.springbukkit.core.command.annotation

import kr.summitsystems.springbukkit.core.command.CommandContextHolder
import kr.summitsystems.springbukkit.core.command.CommandExceptionHandlerRegistry
import kr.summitsystems.springbukkit.core.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConversionService
import kr.summitsystems.springbukkit.core.command.support.DefaultCommandFeedbackSource
import org.bukkit.Server
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Configuration
class CommandAnnotationConfiguration {
    @Autowired
    fun setConfigurers(
        commandConfigurers: ObjectProvider<CommandConfigurer>
    ) {
        val configurers = commandConfigurers.stream().toList()
        if (configurers.size > 1) {
            throw IllegalStateException("Only one CommandConfigurer may exist")
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandMappingAdvisor(
        commandExceptionHandlerRegistry: CommandExceptionHandlerRegistry,
        commandContextHolder: CommandContextHolder
    ): CommandMappingAdvisor {
        return CommandMappingAdvisor(commandExceptionHandlerRegistry, commandContextHolder)
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandMappingAnnotationBeanPostProcessor(
        applicationContext: ApplicationContext
    ): CommandMappingAnnotationBeanPostProcessor {
        return CommandMappingAnnotationBeanPostProcessor(applicationContext)
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandControllerAdviceAnnotationBeanPostProcessor(
        applicationContext: ApplicationContext
    ) : CommandControllerAdviceAnnotationBeanPostProcessor {
        return CommandControllerAdviceAnnotationBeanPostProcessor(applicationContext)
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun commandArgumentConversionService(
        server: Server,
        commandConfigurers: ObjectProvider<CommandConfigurer>
    ): CommandArgumentConversionService {
        val service = CommandArgumentConversionService(server)
        commandConfigurers.forEach { configurer ->
            configurer.addCommandArgumentCompleterAdapter(service)
            configurer.addArgumentConverter(service)
        }
        return service
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandFeedbackSource(
        commandConfigurers: ObjectProvider<CommandConfigurer>,
        messageSource: MessageSource
    ): CommandFeedbackSource {
        return commandConfigurers.singleOrNull()?.getCommandFeedbackSource() ?: DefaultCommandFeedbackSource(messageSource)
    }
}