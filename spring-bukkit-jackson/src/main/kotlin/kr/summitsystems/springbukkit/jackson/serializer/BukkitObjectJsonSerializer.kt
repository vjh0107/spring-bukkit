package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.springframework.core.serializer.Serializer
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

@Component
class BukkitObjectJsonSerializer(
    private val serializer: Serializer<ConfigurationSerializable>,
) : JsonSerializer<ConfigurationSerializable>() {
    override fun serialize(value: ConfigurationSerializable, jsonGenerator: JsonGenerator, serializers: SerializerProvider?) {
        val serializedItemStack = serializer.serializeToByteArray(value)
        val encodedItemStack = Base64Coder.encode(serializedItemStack).concatToString()
        jsonGenerator.writeString(encodedItemStack)
    }
}