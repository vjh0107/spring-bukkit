package kr.summitsystems.springbukkit.core.listener

import java.lang.reflect.Method

interface BukkitListenerRegistrar {
    fun registerListener(
        eventClass: Class<*>,
        instance: Any,
        method: Method,
        handleOrder: HandleOrder,
        ignoreCancelled: Boolean
    )
}