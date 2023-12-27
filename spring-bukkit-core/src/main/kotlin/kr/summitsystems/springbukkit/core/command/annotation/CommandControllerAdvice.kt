package kr.summitsystems.springbukkit.core.command.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandControllerAdvice(
    @get:AliasFor(annotation = Component::class) val value: String = ""
)