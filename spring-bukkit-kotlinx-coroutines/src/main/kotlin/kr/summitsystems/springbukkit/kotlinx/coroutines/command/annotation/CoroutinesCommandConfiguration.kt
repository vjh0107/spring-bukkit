package kr.summitsystems.springbukkit.kotlinx.coroutines.command.annotation

import kotlinx.coroutines.CoroutineScope
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class CoroutinesCommandConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun commandCoroutineScope(
        pluginCoroutineScope: CoroutineScope,
        commandConfigurers: ObjectProvider<CoroutinesCommandConfigurer>
    ): CoroutineScope {
        return commandConfigurers.singleOrNull()?.getCoroutineScope() ?: pluginCoroutineScope
    }
}