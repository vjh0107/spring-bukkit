package kr.junhyung.springbukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class BukkitListenerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            return bean;
        }

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (!AnnotationUtils.isCandidateClass(targetClass, EventHandler.class)) {
            return bean;
        }
        Set<Method> annotatedMethods = MethodIntrospector
            .selectMethods(targetClass, (Method method) -> AnnotatedElementUtils.isAnnotated(method, EventHandler.class));
        if (annotatedMethods.isEmpty()) {
            return bean;
        }

        Plugin plugin = applicationContext.getBean(Plugin.class);
        PluginManager pluginManager = applicationContext.getBean(PluginManager.class);

        for (Method method : annotatedMethods) {
            List<Parameter> methodParameters = Arrays.stream(method.getParameters()).toList();
            if (methodParameters.size() != 1) {
                throw new IllegalStateException("A event handler can only have one event parameter.");
            }
            Parameter methodParameter = methodParameters.stream().findFirst().get();
            System.out.println(methodParameter.getType().toString());

            if (methodParameter.getType().isAssignableFrom(Event.class)) {
                throw new IllegalStateException("first parameter of event handler must be event.");
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> event = (Class<? extends Event>) methodParameter.getType();

            EventHandler eventHandler = AnnotationUtils.getAnnotation(method, EventHandler.class);
            Objects.requireNonNull(eventHandler);
            EventExecutor eventExecutor = bakeEventExecutor(bean, event, method);
            pluginManager.registerEvent(event, new Listener() {}, eventHandler.priority(), eventExecutor, plugin, eventHandler.ignoreCancelled());
        }
        return bean;
    }

    private static EventExecutor bakeEventExecutor(Object listenerInstance, Class<? extends Event> eventClass, Method method) {
        return (unused, event) -> {
            try {
                if (eventClass.isAssignableFrom(event.getClass())) {
                    method.invoke(listenerInstance, event);
                }
            } catch (InvocationTargetException | IllegalAccessException exception) {
                throw new EventException(exception.getCause());
            }
        };
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
