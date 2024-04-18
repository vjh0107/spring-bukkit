package kr.junhyung.springbukkit.listener;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SimpleBukkitListenerRegistrar implements BukkitListenerRegistrar {
    private final Plugin plugin;
    private final PluginManager pluginManager;

    public SimpleBukkitListenerRegistrar(Plugin plugin, PluginManager pluginManager) {
        this.plugin = plugin;
        this.pluginManager = pluginManager;
    }

    public void registerEventHandler(Object listenerInstance) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(listenerInstance);
        Set<Method> annotatedMethods = MethodIntrospector.selectMethods(targetClass, (Method method) -> AnnotatedElementUtils.isAnnotated(method, EventHandler.class));
        if (annotatedMethods.isEmpty()) {
            return;
        }
        for (Method method : annotatedMethods) {
            List<Parameter> methodParameters = Arrays.stream(method.getParameters()).toList();
            if (methodParameters.size() != 1) {
                throw new IllegalStateException("A event handler can only have one event parameter.");
            }
            Parameter methodParameter = methodParameters.stream().findFirst().get();
            if (methodParameter.getType().isAssignableFrom(Event.class)) {
                throw new IllegalStateException("first parameter of event handler must be event.");
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> event = (Class<? extends Event>) methodParameter.getType();

            EventHandler eventHandler = AnnotationUtils.getAnnotation(method, EventHandler.class);
            EventExecutor eventExecutor = bakeEventExecutor(listenerInstance, event, method);
            Objects.requireNonNull(eventHandler);
            registerEventHandler(event, eventHandler, plugin, eventExecutor);
        }
    }

    private void registerEventHandler(Class<? extends Event> event, EventHandler eventHandler, Plugin plugin, EventExecutor eventExecutor) {
        EventPriority priority = eventHandler.priority();
        boolean ignoreCancelled = eventHandler.ignoreCancelled();
        pluginManager.registerEvent(event, new Listener() {}, priority, eventExecutor, plugin, ignoreCancelled);
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
}