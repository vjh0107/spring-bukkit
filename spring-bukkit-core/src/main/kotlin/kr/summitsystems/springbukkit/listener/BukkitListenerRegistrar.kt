package kr.summitsystems.springbukkit.listener

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