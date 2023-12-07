package kr.summitsystems.springbukkit

open class SpringBukkitException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    class Unexpected : SpringBukkitException("An unexpected exception has occurred.")
}