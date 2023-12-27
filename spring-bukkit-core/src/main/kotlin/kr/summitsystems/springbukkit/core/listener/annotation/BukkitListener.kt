package kr.summitsystems.springbukkit.core.listener.annotation

import kr.summitsystems.springbukkit.core.listener.HandleOrder

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BukkitListener(val handleOrder: HandleOrder = HandleOrder.NORMAL, val ignoreCancelled: Boolean = false)