package kr.summitsystems.springbukkit.support

import kr.summitsystems.springbukkit.core.SpringBukkitAutoConfiguration
import kr.summitsystems.springbukkit.core.command.CommandTabCompletionProvider
import kr.summitsystems.springbukkit.support.paper.PaperTabCompleter
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Role
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@SpringBukkitAutoConfiguration
class SpringBukkitSupportConfiguration {

    @ConditionalOnClass(name = ["com.destroystokyo.paper.event.server.AsyncTabCompleteEvent"])
    @Scope(proxyMode = ScopedProxyMode.DEFAULT)
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun paperTabCompleter(
        commandTabCompletionProvider: CommandTabCompletionProvider
    ): PaperTabCompleter {
        return PaperTabCompleter(commandTabCompletionProvider)
    }
}