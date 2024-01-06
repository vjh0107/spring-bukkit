package kr.summitsystems.springbukkit.core.listener

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.*
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
        eventPriority: EventPriority,
        ignoreCancelled: Boolean
    ) {
        if (eventClass.isAssignableFrom(Event::class.java)) {
            throw IllegalStateException("first parameter of event handler must be event. (method: ${method.name}, parameter1: ${eventClass.simpleName})")
        }
        val executor = eventExecutorFactory.create(eventClass, instance, method)
        val listener = bakeListener(executor, eventPriority, plugin, ignoreCancelled)
        registerBakedListener(eventClass, listener)
    }

    private fun bakeListener(executor: EventExecutor, priority: EventPriority, plugin: Plugin, ignoreCancelled: Boolean): RegisteredListener {
        return RegisteredListener(object : Listener {}, executor, priority, plugin, ignoreCancelled)
    }

    private fun registerBakedListener(eventClass: Class<*>, registeredListener: RegisteredListener) {
        val eventListeners = SimplePluginManager::class.java.getDeclaredMethod("getEventListeners", Class::class.java)
        eventListeners.isAccessible = true

        val handlerList = eventListeners.invoke(pluginManager, eventClass) as HandlerList
        handlerList.register(registeredListener)
    }
}