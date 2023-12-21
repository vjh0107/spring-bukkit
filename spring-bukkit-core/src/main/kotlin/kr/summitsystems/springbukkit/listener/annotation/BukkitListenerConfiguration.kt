package kr.summitsystems.springbukkit.listener.annotation

import kr.summitsystems.springbukkit.listener.BukkitListenerRegistrar
import kr.summitsystems.springbukkit.listener.BukkitListenerRegistrarImpl
import kr.summitsystems.springbukkit.listener.EventExecutorFactory
import kr.summitsystems.springbukkit.listener.SimpleEventExecutorFactory
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
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
        executorFactory: EventExecutorFactory
    ): BukkitListenerRegistrar {
        return BukkitListenerRegistrarImpl(plugin, pluginManager, executorFactory)
    }

    @ConditionalOnMissingBean(EventExecutorFactory::class)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun eventExecutorFactory(): EventExecutorFactory {
        return SimpleEventExecutorFactory()
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun bukkitListenerAnnotationBeanPostProcessor(
        applicationContext: ApplicationContext
    ): BukkitListenerAnnotationBeanPostProcessor {
        return BukkitListenerAnnotationBeanPostProcessor(applicationContext)
    }
}