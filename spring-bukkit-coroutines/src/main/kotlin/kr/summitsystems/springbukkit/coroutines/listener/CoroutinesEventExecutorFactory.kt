package kr.summitsystems.springbukkit.coroutines.listener

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.core.listener.EventExecutorFactory
import org.bukkit.plugin.EventExecutor
import java.lang.reflect.Method

class CoroutinesEventExecutorFactory(private val coroutineScope: CoroutineScope) : EventExecutorFactory {
    override fun create(eventClass: Class<*>, listenerInstance: Any, method: Method): EventExecutor {
        return CoroutinesEventExecutor(coroutineScope, eventClass, listenerInstance, method)
    }
}