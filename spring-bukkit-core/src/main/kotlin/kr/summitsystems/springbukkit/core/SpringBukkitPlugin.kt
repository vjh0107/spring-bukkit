package kr.summitsystems.springbukkit.core

import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.FileSystemResource
import java.io.File
import java.util.*

abstract class SpringBukkitPlugin : JavaPlugin(), ApplicationContextInitializer<GenericApplicationContext>,
    DisposableContainer, Disposable {
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
        return SpringApplicationBuilder(
            SpringBukkitResourceLoader(this, this.classLoader, server.pluginManager),
            applicationSource
        )
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .logStartupInfo(false)
            .initializers(this)
            .run()
    }

    override fun initialize(applicationContext: GenericApplicationContext) {
        applicationContext.setClassLoader(this.classLoader)
        registerYamlPropertySource(applicationContext, "application.yml")
        registerYamlPropertySource(applicationContext, "config.yml")
        registerPropertiesPropertySource(applicationContext, "application.properties")
        registerMetadataReaderFactory(applicationContext)
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

    private fun registerPluginBean(beanDefinitionRegistry: BeanDefinitionRegistry) {
        val definition: BeanDefinition = BeanDefinitionBuilder
            .rootBeanDefinition(
                Plugin::class.java
            ) { this }
            .getBeanDefinition()
        beanDefinitionRegistry.registerBeanDefinition(this::class.qualifiedName!!, definition)
    }

    private fun registerMetadataReaderFactory(beanDefinitionRegistry: BeanDefinitionRegistry) {
        val beanName = "org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory"
        val definition: BeanDefinition = BeanDefinitionBuilder
            .rootBeanDefinition(
                SpringBukkitMetadataReaderFactoryBean::class.java
            ) { SpringBukkitMetadataReaderFactoryBean() }
            .getBeanDefinition()
        beanDefinitionRegistry.registerBeanDefinition(beanName, definition)
    }
}