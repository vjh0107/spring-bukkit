package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import kr.summitsystems.springbukkit.core.support.serializer.BukkitObjectSerializer
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

class BukkitObjectJsonDeserializer<T : ConfigurationSerializable>(
    private val serializer: BukkitObjectSerializer<T>
) : JsonDeserializer<T>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): T {
        val deserializedItemStack = Base64Coder.decode(parser.valueAsString)

        return serializer.deserializeFromByteArray(deserializedItemStack)
    }
}