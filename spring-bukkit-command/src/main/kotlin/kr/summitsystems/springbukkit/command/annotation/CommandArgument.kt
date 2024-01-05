package kr.summitsystems.springbukkit.command.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CommandArgument(
    val label: String = "",
    val messageSource: String = "",
)