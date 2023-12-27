package kr.summitsystems.springbukkit.core.command.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class CommandController(
    @get:AliasFor(annotation = Component::class) val value: String = ""
)