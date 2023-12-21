package kr.summitsystems.springbukkit.coroutines.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.springframework.core.KotlinDetector
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

class CoroutinesEventExecutor(
    private val coroutineScope: CoroutineScope,
    private val eventClass: Class<*>,
    private val instance: Any,
    private val method: Method
) : EventExecutor {
    override fun execute(empty: Listener, event: Event) {
        if (eventClass.isInstance(event)) {
            try {
                if (KotlinDetector.isSuspendingFunction(method)) {
                    runBlocking(coroutineScope.coroutineContext) {
                        method.kotlinFunction!!.callSuspend(instance, event)
                    }
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