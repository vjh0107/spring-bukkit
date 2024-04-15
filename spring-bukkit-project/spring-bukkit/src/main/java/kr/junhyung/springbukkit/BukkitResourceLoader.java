package kr.junhyung.springbukkit;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

class BukkitResourceLoader extends DefaultResourceLoader {
    private final List<InternalResourceLoader> resourceLoaders = new ArrayList<>();

    BukkitResourceLoader(JavaPlugin javaPlugin, PluginManager pluginManager) {
        super(getClassLoader(javaPlugin));

        Set<String> dependPluginNames = new HashSet<>();
        dependPluginNames.addAll(javaPlugin.getDescription().getDepend());
        dependPluginNames.addAll(javaPlugin.getDescription().getSoftDepend());

        for (String dependPluginName : dependPluginNames) {
            Plugin dependPlugin = pluginManager.getPlugin(dependPluginName);
            if (dependPlugin instanceof JavaPlugin dependJavaPlugin) {
                ClassLoader classLoader = getClassLoader(dependJavaPlugin);
                InternalResourceLoader resourceLoader = new InternalResourceLoader(classLoader);
                this.resourceLoaders.add(resourceLoader);
            }
        }
    }

    @Override
    public @NonNull Resource getResource(@NonNull String location) {
        Resource resourceFromSuper = super.getResource(location);
        if (!resourceFromSuper.exists()) {
            for (ResourceLoader resourceLoader : resourceLoaders) {
                Resource resourceFromDependPlugin = resourceLoader.getResource(location);
                if (resourceFromDependPlugin.exists()) {
                    return resourceFromDependPlugin;
                }
            }
        }
        return resourceFromSuper;
    }

    @Override
    protected @NonNull Resource getResourceByPath(@NonNull String path) {
        Resource resourceFromSuper = super.getResourceByPath(path);
        if (!resourceFromSuper.exists()) {
            for (InternalResourceLoader resourceLoader : resourceLoaders) {
                Resource resourceFromDependPlugin = resourceLoader.getResourceByPath(path);
                if (resourceFromDependPlugin.exists()) {
                    return resourceFromDependPlugin;
                }
            }
        }
        return resourceFromSuper;
    }

    private static @NonNull ClassLoader getClassLoader(JavaPlugin javaPlugin) {
        Method getClassLoaderMethod = ReflectionUtils.findMethod(JavaPlugin.class, "getClassLoader");
        Objects.requireNonNull(getClassLoaderMethod, "A method named 'getClassLoader' not found.");
        ClassLoader classLoader;
        if (!getClassLoaderMethod.canAccess(javaPlugin)) {
            try {
                getClassLoaderMethod.setAccessible(true);
                classLoader = (ClassLoader) ReflectionUtils.invokeMethod(getClassLoaderMethod, javaPlugin);
            } finally {
                getClassLoaderMethod.setAccessible(false);
            }
        } else {
            classLoader = (ClassLoader) ReflectionUtils.invokeMethod(getClassLoaderMethod, javaPlugin);
        }
        return Objects.requireNonNull(classLoader);
    }

    private static class InternalResourceLoader extends DefaultResourceLoader {
        InternalResourceLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        public @NonNull Resource getResourceByPath(@NonNull String path) {
            return super.getResourceByPath(path);
        }
    }
}