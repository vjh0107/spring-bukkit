package kr.summitsystems.springbukkit.core

import org.bukkit.plugin.java.JavaPlugin
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.io.FileSystemResource
import java.io.File

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
        val configFile = File(this@SpringBukkitPlugin.dataFolder.absolutePath + "/config.yml")
        if (configFile.exists()) {
            val resource = FileSystemResource(configFile)
            val yamlPropertySourceLoader = YamlPropertySourceLoader()
            val yamlPropertySources = yamlPropertySourceLoader.load("spring-bukkit", resource)
            yamlPropertySources.forEach { yamlPropertySource ->
                propertySources.addLast(yamlPropertySource)
            }
        }
    }

    private fun registerPluginBean(applicationContext: ConfigurableApplicationContext) {
        applicationContext.beanFactory.registerSingleton("plugin", this)
    }
}