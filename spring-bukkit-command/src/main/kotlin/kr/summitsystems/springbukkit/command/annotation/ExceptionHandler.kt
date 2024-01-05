package kr.summitsystems.springbukkit.command.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExceptionHandler(
    vararg val value: KClass<out Throwable> = []
)