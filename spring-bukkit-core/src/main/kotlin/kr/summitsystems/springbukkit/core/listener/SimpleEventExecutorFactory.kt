package kr.summitsystems.springbukkit.core.listener

import org.bukkit.plugin.EventExecutor
import java.lang.reflect.Method

class SimpleEventExecutorFactory : EventExecutorFactory {
    override fun create(eventClass: Class<*>, listenerInstance: Any, method: Method): EventExecutor {
        return SimpleEventExecutor(eventClass, listenerInstance, method)
    }
}