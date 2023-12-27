package kr.summitsystems.springbukkit.core.support.config

import kr.summitsystems.springbukkit.core.support.SpringBukkitConversionService
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.ServicesManager
import org.bukkit.scheduler.BukkitScheduler
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.ConversionService

@Configuration
class BukkitConfiguration {
    @Bean
    fun bukkitServer(): Server {
        return Bukkit.getServer()
    }

    @Bean
    fun bukkitPluginManager(server: Server): PluginManager {
        return server.pluginManager
    }

    @Bean
    fun bukkitServicesManager(server: Server): ServicesManager {
        return server.servicesManager
    }

    @Bean
    fun bukkitScheduler(server: Server): BukkitScheduler {
        return server.scheduler
    }

    @Bean
    fun pluginDescriptionFile(plugin: Plugin): PluginDescriptionFile {
        return plugin.description
    }

    @ConditionalOnMissingBean(Plugin::class)
    @Bean
    fun plugin(): Plugin {
        throw NotImplementedError()
    }

    @Bean
    fun conversionService(): ConversionService {
        return SpringBukkitConversionService()
    }
}