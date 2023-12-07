package kr.summitsystems.springbukkit.listener.annotation

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.listener.BukkitListenerRegistrar
import kr.summitsystems.springbukkit.listener.support.ServerBukkitListenerRegistrar
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class BukkitListenerConfiguration {

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun bukkitListenerRegistrar(
        plugin: Plugin,
        pluginManager: PluginManager,
        coroutineScope: CoroutineScope
    ): BukkitListenerRegistrar {
        return ServerBukkitListenerRegistrar(plugin, pluginManager, coroutineScope)
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun bukkitListenerAnnotationBeanPostProcessor(
        applicationContext: ApplicationContext
    ): BukkitListenerAnnotationBeanPostProcessor {
        return BukkitListenerAnnotationBeanPostProcessor(applicationContext)
    }
}