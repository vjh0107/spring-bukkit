package kr.junhyung.springbukkit;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;

public abstract class SpringPlugin extends JavaPlugin implements ApplicationContextInitializer<GenericApplicationContext> {
    private ConfigurableApplicationContext configurableApplicationContext;

    protected abstract @NonNull Class<?> getApplicationClass();

    protected SpringApplicationBuilder builder(SpringApplicationBuilder builder) {
        return builder;
    }

    @Override
    public void initialize(@NonNull GenericApplicationContext applicationContext) {
        registerPluginBean(applicationContext);
    }

    @Override
    public void onEnable() {
        ResourceLoader resourceLoader = new SpringBukkitResourceLoader(this, getServer().getPluginManager());
        SpringApplicationBuilder builder = new SpringApplicationBuilder()
            .resourceLoader(resourceLoader)
            .sources(getApplicationClass())
            .web(WebApplicationType.NONE)
            .bannerMode(Banner.Mode.OFF)
            .logStartupInfo(false)
            .initializers(this);

        configurableApplicationContext = builder(builder)
            .run();
    }

    @Override
    public void onDisable() {
        if (configurableApplicationContext != null) {
            configurableApplicationContext.close();
        }
    }

    private void registerPluginBean(BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder
            .genericBeanDefinition(Plugin.class, () -> this)
            .setRole(BeanDefinition.ROLE_SUPPORT)
            .getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition(this.getClass().getName(), beanDefinition);
    }
}