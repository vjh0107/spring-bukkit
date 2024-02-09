package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.summitsystems.springbukkit.core.serializer.BukkitObjectSerializer
import org.bukkit.configuration.serialization.ConfigurationSerializable

@Converter
class BukkitObjectConverter(
    private val bukkitObjectSerializer: BukkitObjectSerializer
) : AttributeConverter<ConfigurationSerializable, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ConfigurationSerializable?): ByteArray? {
        if (attribute == null) {
            return null
        }
        return bukkitObjectSerializer.serializeToByteArray(attribute)
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): ConfigurationSerializable? {
        if (dbData == null) {
            return null
        }
        return bukkitObjectSerializer.deserializeFromByteArray(dbData)
    }
}