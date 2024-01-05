package kr.summitsystems.springbukkit.command.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandMapping(val command: String = "", val path: String = "")