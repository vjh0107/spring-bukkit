package kr.summitsystems.springbukkit.core

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import java.lang.IllegalStateException
import java.util.*

abstract class SpringBukkitPlugin : JavaPlugin(), ApplicationContextInitializer<ConfigurableApplicationContext> {
    private var applicationContext: ConfigurableApplicationContext? = null

    final override fun onEnable() {
        loadDefaultConfig()
        if (AnnotationUtils.getAnnotation(getApplicationClass(), SpringBukkitApplication::class.java) == null) {
            throw IllegalStateException("Unable to initialize the unannotated application with @SpringBukkitApplication.")
        }
        applicationContext = runApplication(getApplicationClass())
    }

    final override fun onDisable() {
        applicationContext?.close()
    }

    private fun loadDefaultConfig() {
        getResource("config.yml") ?: return
        saveDefaultConfig()
    }

    protected abstract fun getApplicationClass(): Class<*>

    private fun runApplication(applicationSource: Class<*>): ConfigurableApplicationContext {
        val genuineClassLoader = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = this.classLoader
        return SpringApplicationBuilder(applicationSource)
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .initializers(this)
            .run()
            .also {
                Thread.currentThread().contextClassLoader = genuineClassLoader
            }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        registerPropertySource(applicationContext)
        registerPluginBean(applicationContext)
    }

    private fun registerPropertySource(applicationContext: ConfigurableApplicationContext) {
        val propertySources = applicationContext.environment.propertySources
        propertySources.addLast(BukkitPropertySource(this@SpringBukkitPlugin.config))
        val properties = Properties().apply {
            put("plugin.name", this@SpringBukkitPlugin.name)
        }
        propertySources.addLast(PropertiesPropertySource("spring-bukkit-api", properties))
    }

    private fun registerPluginBean(applicationContext: ConfigurableApplicationContext) {
        applicationContext.beanFactory.registerSingleton("plugin", this)
    }

    private class BukkitPropertySource(
        private val config: FileConfiguration
    ) : PropertySource<FileConfiguration>("bukkit_yaml") {
        override fun getProperty(name: String): Any? {
            return config.get(name)
        }

        override fun containsProperty(name: String): Boolean {
            return config.contains(name)
        }

        override fun getSource(): FileConfiguration {
            return config
        }
    }
}