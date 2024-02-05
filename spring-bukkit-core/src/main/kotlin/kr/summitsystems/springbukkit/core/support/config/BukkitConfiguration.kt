package kr.summitsystems.springbukkit.core.support.config

import kr.summitsystems.springbukkit.core.support.SpringBukkitConversionService
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.ServicesManager
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.core.convert.ConversionService

@Role(BeanDefinition.ROLE_SUPPORT)
@Configuration
class BukkitConfiguration {
    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean(destroyMethod = "")
    fun bukkitServer(): Server {
        return Bukkit.getServer()
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    fun bukkitPluginManager(server: Server): PluginManager {
        return server.pluginManager
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    fun bukkitServicesManager(server: Server): ServicesManager {
        return server.servicesManager
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    fun pluginDescriptionFile(plugin: Plugin): PluginDescriptionFile {
        return plugin.description
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    fun pluginFileConfiguration(plugin: Plugin): FileConfiguration {
        return plugin.config
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean(Plugin::class)
    @Bean
    fun plugin(): Plugin {
        throw IllegalStateException()
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    fun conversionService(): ConversionService {
        return SpringBukkitConversionService()
    }
}