package kr.summitsystems.springbukkit.core

import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource

class SpringBukkitResourceLoader(
    plugin: JavaPlugin,
    classLoader: ClassLoader,
    private val pluginManager: PluginManager
): DefaultResourceLoader(classLoader) {
    private val dependResourceLoaders: List<InternalDefaultResourceLoader> = plugin.description.depend.map { pluginId ->
        val javaPlugin = pluginManager.getPlugin(pluginId) as? JavaPlugin
            ?: throw IllegalStateException("The plugin with id $pluginId is not found.")
        val getClassLoaderMethodAccessor = JavaPlugin::class.java.getDeclaredMethod("getClassLoader")
        getClassLoaderMethodAccessor.isAccessible = true
        val pluginClassLoader = getClassLoaderMethodAccessor.invoke(javaPlugin) as ClassLoader
        InternalDefaultResourceLoader(pluginClassLoader)
    }

    override fun getResource(location: String): Resource {
        val superResource = super.getResource(location)
        if (superResource.exists()) {
            return superResource
        } else {
            dependResourceLoaders.forEach { dependResourceLoader ->
                val dependResource = dependResourceLoader.getResource(location)
                if (dependResource.exists()) {
                    return dependResource
                }
            }
        }
        return superResource
    }

    override fun getResourceByPath(path: String): Resource {
        val superResource = super.getResourceByPath(path)
        if (superResource.exists()) {
            return superResource
        } else {
            dependResourceLoaders.forEach { dependResourceLoader ->
                val dependResource = dependResourceLoader.getResourceByPath(path)
                if (dependResource.exists()) {
                    return dependResource
                }
            }
        }
        return superResource
    }

    private class InternalDefaultResourceLoader(classLoader: ClassLoader): DefaultResourceLoader(classLoader) {
        public override fun getResourceByPath(path: String): Resource {
            return super.getResourceByPath(path)
        }
    }
}