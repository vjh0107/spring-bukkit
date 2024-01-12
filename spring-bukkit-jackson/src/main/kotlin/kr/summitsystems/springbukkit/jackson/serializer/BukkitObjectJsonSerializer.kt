package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kr.summitsystems.springbukkit.core.support.serializer.BukkitObjectSerializer
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

class BukkitObjectJsonSerializer<T : ConfigurationSerializable>(
    private val serializer: BukkitObjectSerializer<T>
) : JsonSerializer<T>() {
    override fun serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider?) {
        val serializedItemStack = serializer.serializeToByteArray(value)
        val encodedItemStack = String(Base64Coder.encode(serializedItemStack))
        gen.writeObject(encodedItemStack)
    }
}