package kr.summitsystems.springbukkit.view

open class GenericViewInitializationContext : ViewInitializationContext {
    private val extras: MutableMap<String, Any> = mutableMapOf()

    override fun <T> setExtra(key: String, value: T) {
        extras[key] = value as Any
    }

    override fun <T> findExtra(key: String): T? {
        if (extras[key] == null) {
            return null
        }
        return getExtra(key)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getExtra(key: String): T {
        if (extras[key] == null) {
            throw NoSuchElementException("No value present with key '$key'")
        }
        return extras[key] as T
    }
}