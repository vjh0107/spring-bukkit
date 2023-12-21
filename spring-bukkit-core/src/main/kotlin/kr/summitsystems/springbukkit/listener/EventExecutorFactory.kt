package kr.summitsystems.springbukkit.listener

import org.bukkit.plugin.EventExecutor
import java.lang.reflect.Method

interface EventExecutorFactory {
    fun create(eventClass: Class<*>, listenerInstance: Any, method: Method): EventExecutor
}