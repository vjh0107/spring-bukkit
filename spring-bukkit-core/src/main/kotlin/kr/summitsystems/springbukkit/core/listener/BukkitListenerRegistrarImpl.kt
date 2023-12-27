package kr.summitsystems.springbukkit.core.listener

import org.apache.commons.lang3.ClassUtils
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.SimplePluginManager
import java.lang.reflect.Method

class BukkitListenerRegistrarImpl(
    private val plugin: Plugin,
    private val pluginManager: PluginManager,
    private val eventExecutorFactory: EventExecutorFactory
) : BukkitListenerRegistrar {
    override fun registerListener(
        eventClass: Class<*>,
        instance: Any,
        method: Method,
        handleOrder: HandleOrder,
        ignoreCancelled: Boolean
    ) {
        if (!ClassUtils.isAssignable(eventClass, Event::class.java)) {
            throw IllegalStateException("first parameter of event handler must be event. (method: ${method.name}, parameter1: ${eventClass.simpleName})")
        }
        val executor = eventExecutorFactory.create(eventClass, instance, method)
        val eventListeners = SimplePluginManager::class.java.getDeclaredMethod("getEventListeners", Class::class.java)
        eventListeners.isAccessible = true
        val listener = RegisteredListener(object : Listener {}, executor, getBukkitEventPriority(handleOrder), plugin, ignoreCancelled)

        val handlerList = eventListeners.invoke(pluginManager, eventClass) as HandlerList
        handlerList.register(listener)
    }

    private fun getBukkitEventPriority(handleOrder: HandleOrder): EventPriority {
        return when(handleOrder) {
            HandleOrder.FIRST -> EventPriority.LOWEST
            HandleOrder.EARLY -> EventPriority.LOW
            HandleOrder.NORMAL -> EventPriority.NORMAL
            HandleOrder.LATE -> EventPriority.HIGH
            HandleOrder.LAST -> EventPriority.HIGHEST
            HandleOrder.MONITOR -> EventPriority.MONITOR
        }
    }
}