package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.summitsystems.springbukkit.core.support.serializer.BukkitObjectSerializer
import org.bukkit.Location

@Converter
class LocationConverter(
    private val locationSerializer: BukkitObjectSerializer<Location>
) : AttributeConverter<Location, ByteArray> {
    override fun convertToDatabaseColumn(attribute: Location?): ByteArray? {
        if (attribute == null) {
            return null
        }
        return locationSerializer.serializeToByteArray(attribute)
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): Location? {
        if (dbData == null) {
            return null
        }
        return locationSerializer.deserializeFromByteArray(dbData)
    }
}