package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.springframework.core.serializer.Deserializer
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

@Component
class BukkitObjectJsonDeserializer(
    private val deserializer: Deserializer<ConfigurationSerializable>
) : JsonDeserializer<ConfigurationSerializable>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ConfigurationSerializable {
        val deserializedItemStack = Base64Coder.decode(parser.valueAsString)
        return deserializer.deserializeFromByteArray(deserializedItemStack)
    }
}