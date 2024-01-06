package kr.summitsystems.springbukkit.view

interface ViewInitializationContext {
    fun <T> setExtra(key: String, value: T)

    fun <T> findExtra(key: String): T?

    fun <T> getExtra(key: String): T
}