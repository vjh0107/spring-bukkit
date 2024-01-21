package kr.summitsystems.springbukkit.kotlinx.coroutines.support

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kr.summitsystems.springbukkit.kotlinx.coroutines.PluginCoroutineContextElement
import org.bukkit.plugin.Plugin
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Role
import kotlin.coroutines.CoroutineContext

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class DefaultCoroutineConfiguration {

    @ConditionalOnBean(Plugin::class)
    @ConditionalOnMissingBean(name = ["coroutineScope", "pluginCoroutineScope"])
    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun pluginCoroutineScope(plugin: Plugin): CoroutineScope {
        return object : CoroutineScope {
            private val job = SupervisorJob()
            private val coroutineName = CoroutineName(plugin.name)
            private val pluginElement = PluginCoroutineContextElement(plugin)

            override val coroutineContext: CoroutineContext
                get() = Dispatchers.Default + job + coroutineName + pluginElement
        }
    }
}