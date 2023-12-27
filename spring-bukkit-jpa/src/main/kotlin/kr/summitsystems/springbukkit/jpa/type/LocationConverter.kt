package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.summitsystems.springbukkit.core.support.serializer.LocationSerializer
import org.bukkit.Location

@Converter
class LocationConverter : AttributeConverter<Location, ByteArray> {
    override fun convertToDatabaseColumn(attribute: Location?): ByteArray? {
        if (attribute == null) {
            return null
        }
        return LocationSerializer.serializeToByteArray(attribute)
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): Location? {
        if (dbData == null) {
            return null
        }
        return LocationSerializer.deserializeFromByteArray(dbData)
    }
}