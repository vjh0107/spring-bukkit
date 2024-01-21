package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import kr.summitsystems.springbukkit.core.serializer.ConfigurationSerializableRegistry
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.springframework.stereotype.Component

@Component
class BukkitSerializerModule(
    private val bukkitObjectJsonSerializer: JsonSerializer<ConfigurationSerializable>,
    private val bukkitObjectJsonDeserializer: JsonDeserializer<ConfigurationSerializable>,
    private val configurationSerializableRegistry: ConfigurationSerializableRegistry
) : Module() {
    override fun version(): Version {
        return Version(1, 0, 0, "", "kr.summitsystems", "springbukkit")
    }

    override fun getModuleName(): String {
        return "bukkit-serializer-module"
    }

    override fun setupModule(context: SetupContext) {
        SimpleSerializers()
            .apply {
                addSerializer(ConfigurationSerializable::class.java, bukkitObjectJsonSerializer)
            }
            .also { serializers ->
                context.addSerializers(serializers)
            }
        SimpleDeserializers()
            .apply {
                val deserializers: Map<Class<*>, JsonDeserializer<*>> = configurationSerializableRegistry
                    .findRegisteredAll()
                    .values
                    .toSet()
                    .associateWith {
                        bukkitObjectJsonDeserializer
                    }
                addDeserializers(deserializers)
            }
            .also { deserializers ->
                context.addDeserializers(deserializers)
            }
    }
}