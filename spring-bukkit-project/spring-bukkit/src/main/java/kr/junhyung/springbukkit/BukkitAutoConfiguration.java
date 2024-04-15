package kr.junhyung.springbukkit;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

@ConditionalOnClass(Bukkit.class)
@Role(BeanDefinition.ROLE_SUPPORT)
@AutoConfiguration
public class BukkitAutoConfiguration {
    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean
    @Bean(destroyMethod = "")
    Server server() {
        return Bukkit.getServer();
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean
    @Bean
    PluginManager pluginManager(Server server) {
        return server.getPluginManager();
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean
    @Bean
    ServicesManager serviceManager(Server server) {
        return server.getServicesManager();
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean
    @Bean
    FileConfiguration fileConfiguration(Plugin plugin) {
        return plugin.getConfig();
    }

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean
    @Bean
    Plugin plugin() {
        throw new IllegalStateException();
    }
}