package kr.summitsystems.springbukkit.listener

import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class SimpleEventExecutor(
    private val eventClass: Class<*>,
    private val listenerInstance: Any,
    private val method: Method
) : EventExecutor {
    override fun execute(empty: Listener, event: Event) {
        if (eventClass.isInstance(event)) {
            try {
                method.invoke(listenerInstance, event)
            } catch (exception: InvocationTargetException) {
                val cause = exception.cause ?: exception
                throw cause
            }
        }
    }
}