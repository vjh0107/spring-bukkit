package kr.junhyung.springbukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public class BukkitListenerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private Supplier<BukkitListenerRegistrar> registrar;

    public BukkitListenerBeanPostProcessor() {}

    public void configure(Supplier<BukkitListenerRegistrar> registrar) {
        this.registrar = registrar;
    }

    @Override
    public void setApplicationContext(@NonNull  ApplicationContext applicationContext) throws BeansException {
        this.registrar = () -> {
            Plugin plugin = applicationContext.getBean(Plugin.class);
            PluginManager pluginManager = applicationContext.getBean(PluginManager.class);
            return new SimpleBukkitListenerRegistrar(plugin, pluginManager);
        };
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            return bean;
        }

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (!AnnotationUtils.isCandidateClass(targetClass, EventHandler.class)) {
            return bean;
        }

        Set<Method> annotatedMethods = MethodIntrospector.selectMethods(targetClass, (Method method) -> AnnotatedElementUtils.isAnnotated(method, EventHandler.class));
        if (annotatedMethods.isEmpty()) {
            return bean;
        }
        registrar.get().registerEventHandler(bean);
        return bean;
    }
}