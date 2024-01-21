package kr.summitsystems.springbukkit.core.serializer

import org.bukkit.configuration.serialization.ConfigurationSerializable

interface ConfigurationSerializableRegistry {
    fun <T : ConfigurationSerializable> register(clazz: Class<T>, alias: String)

    fun findRegisteredAll(): Map<String, Class<out ConfigurationSerializable>>
}