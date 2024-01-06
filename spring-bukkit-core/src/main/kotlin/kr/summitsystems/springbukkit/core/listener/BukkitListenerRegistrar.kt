package kr.summitsystems.springbukkit.core.listener

import org.bukkit.event.EventPriority
import java.lang.reflect.Method

interface BukkitListenerRegistrar {
    fun registerListener(
        eventClass: Class<*>,
        instance: Any,
        method: Method,
        eventPriority: EventPriority,
        ignoreCancelled: Boolean
    )
}