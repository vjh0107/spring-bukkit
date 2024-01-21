package kr.summitsystems.springbukkit.kotlinx.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.stereotype.Component
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

@Component
class BukkitObjectKSerializer(
    private val bukkitObjectSerializer: Serializer<ConfigurationSerializable>,
    private val bukkitObjectDeserializer: Deserializer<ConfigurationSerializable>
) : KSerializer<ConfigurationSerializable> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("bukkit.ConfigurationSerializable", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ConfigurationSerializable) {
        val serialized = bukkitObjectSerializer.serializeToByteArray(value)
        val encoded = Base64Coder.encode(serialized)
        encoder.encodeString(encoded.concatToString())
    }

    override fun deserialize(decoder: Decoder): ConfigurationSerializable {
        val decoded = Base64Coder.decode(decoder.decodeString())
        return bukkitObjectDeserializer.deserializeFromByteArray(decoded)
    }
}