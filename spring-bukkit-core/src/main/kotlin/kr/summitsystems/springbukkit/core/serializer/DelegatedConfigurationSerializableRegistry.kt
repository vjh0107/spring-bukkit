package kr.summitsystems.springbukkit.core.serializer

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.springframework.stereotype.Component

@Component
class DelegatedConfigurationSerializableRegistry : ConfigurationSerializableRegistry {
    override fun <T : ConfigurationSerializable> register(clazz: Class<T>, alias: String) {
        ConfigurationSerialization.registerClass(clazz, alias)
    }

    @Suppress("UNCHECKED_CAST")
    override fun findRegisteredAll(): Map<String, Class<out ConfigurationSerializable>> {
        val aliasesField = ConfigurationSerialization::class.java.getDeclaredField("aliases").also {
            it.isAccessible = true
        }
       return aliasesField.get(null) as Map<String, Class<out ConfigurationSerializable>>
    }
}