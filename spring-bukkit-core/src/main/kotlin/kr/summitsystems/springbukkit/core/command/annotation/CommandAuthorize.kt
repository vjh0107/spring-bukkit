package kr.summitsystems.springbukkit.core.command.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandAuthorize(
    val permission: String = "",
    val isOp: Boolean = false
)