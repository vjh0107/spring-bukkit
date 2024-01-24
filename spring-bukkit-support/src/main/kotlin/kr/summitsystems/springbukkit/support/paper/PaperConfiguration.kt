package kr.summitsystems.springbukkit.support.paper

import kr.summitsystems.springbukkit.command.CommandTabCompletionProvider
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.*

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class PaperConfiguration {
    @ConditionalOnClass(name = ["com.destroystokyo.paper.event.server.AsyncTabCompleteEvent"])
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun paperTabCompleter(commandTabCompletionProvider: CommandTabCompletionProvider): PaperTabCompleter {
        return PaperTabCompleter(commandTabCompletionProvider)
    }
}