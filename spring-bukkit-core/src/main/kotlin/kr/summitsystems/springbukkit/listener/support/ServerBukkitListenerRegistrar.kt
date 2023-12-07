package kr.summitsystems.springbukkit.listener.support

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.summitsystems.springbukkit.listener.BukkitListenerRegistrar
import kr.summitsystems.springbukkit.listener.HandleOrder
import org.apache.commons.lang3.ClassUtils
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.*
import org.springframework.core.KotlinDetector
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

class ServerBukkitListenerRegistrar(
    private val plugin: Plugin,
    private val pluginManager: PluginManager,
    private val coroutineScope: CoroutineScope
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
        val executor = SuspendableEventExecutor(eventClass, instance, method)
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

    inner class SuspendableEventExecutor(
        private val eventClass: Class<*>,
        private val instance: Any,
        private val method: Method
    ) : EventExecutor {
        override fun execute(empty: Listener, event: Event) {
            if (eventClass.isInstance(event)) {
                 coroutineScope.launch(Dispatchers.Unconfined, CoroutineStart.UNDISPATCHED) {
                    invokeHandlerMethod(event)
                }
            }
        }

        private suspend fun invokeHandlerMethod(event: Event) {
            try {
                if (KotlinDetector.isSuspendingFunction(method)) {
                    method.kotlinFunction!!.callSuspend(instance, event)
                } else {
                    method.invoke(instance, event)
                }
            } catch (exception: InvocationTargetException) {
                val cause = exception.cause ?: exception
                throw cause
            }
        }
    }
}