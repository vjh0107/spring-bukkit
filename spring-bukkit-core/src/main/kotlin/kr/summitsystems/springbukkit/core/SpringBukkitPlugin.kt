package kr.summitsystems.springbukkit.core

import org.bukkit.plugin.java.JavaPlugin
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.FileSystemResource
import java.io.File
import java.util.*

abstract class SpringBukkitPlugin : JavaPlugin(), ApplicationContextInitializer<ConfigurableApplicationContext>, DisposableContainer, Disposable {
    private var applicationContext: ConfigurableApplicationContext? = null
    private val disposables: MutableList<Disposable> = mutableListOf()

    final override fun onEnable() {
        loadDefaultConfig()
        if (AnnotationUtils.getAnnotation(getApplicationClass(), SpringBukkitApplication::class.java) == null) {
            throw IllegalStateException("Unable to initialize the unannotated application with @SpringBukkitApplication.")
        }
        applicationContext = runApplication(getApplicationClass())
    }

    final override fun onDisable() {
        disposables.forEach { it.dispose() }
        applicationContext?.close()
    }

    final override fun dispose() {
        server.pluginManager.disablePlugin(this)
    }

    final override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    private fun loadDefaultConfig() {
        loadResource("application.yml")
        loadResource("config.yml")
        loadResource("application.properties")
    }

    private fun loadResource(filename: String) {
        if (getResource(filename) != null && !File(dataFolder, filename).exists()) {
            saveResource(filename, false)
        }
    }

    protected abstract fun getApplicationClass(): Class<*>

    private fun runApplication(applicationSource: Class<*>): ConfigurableApplicationContext {
        Thread.currentThread().contextClassLoader = this.classLoader
        return SpringApplicationBuilder(DefaultResourceLoader(this.classLoader), applicationSource)
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .logStartupInfo(false)
            .initializers(this)
            .run()
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        applicationContext.setClassLoader(this.classLoader)
        registerYamlPropertySource(applicationContext, "application.yml")
        registerYamlPropertySource(applicationContext, "config.yml")
        registerPropertiesPropertySource(applicationContext, "application.properties")
        registerPluginBean(applicationContext)
    }

    private fun registerYamlPropertySource(applicationContext: ConfigurableApplicationContext, file: String) {
        val configFile = File(this@SpringBukkitPlugin.dataFolder.absolutePath + "/" + file)
        if (configFile.exists()) {
            val resource = FileSystemResource(configFile)
            val yamlPropertySourceLoader = YamlPropertySourceLoader()
            val yamlPropertySources = yamlPropertySourceLoader.load("spring-bukkit-${file}", resource)
            yamlPropertySources.forEach { yamlPropertySource ->
                applicationContext.environment.propertySources.addLast(yamlPropertySource)
            }
        }
    }

    private fun registerPropertiesPropertySource(applicationContext: ConfigurableApplicationContext, file: String) {
        val configFile = File(this@SpringBukkitPlugin.dataFolder.absolutePath + "/" + file)
        if (configFile.exists()) {
            val properties = Properties().also { properties -> properties.load(configFile.inputStream()) }
            val propertySource = PropertiesPropertySource("spring-bukkit-${file}", properties)
            applicationContext.environment.propertySources.addLast(propertySource)
        }
    }

    private fun registerPluginBean(applicationContext: ConfigurableApplicationContext) {
        applicationContext.beanFactory.registerSingleton("plugin", this)
    }
}