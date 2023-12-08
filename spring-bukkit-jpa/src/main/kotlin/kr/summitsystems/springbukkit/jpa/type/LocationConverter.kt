package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.bukkit.Bukkit
import org.bukkit.Location

@Converter
class LocationConverter : AttributeConverter<Location, String> {
    override fun convertToDatabaseColumn(attribute: Location?): String? {
        if (attribute == null) {
            return null
        }
        val parsed = attribute.run {
            val world = requireNotNull(world) { "Location with nullable world can't be stored in database" }
            "${world.name};$x;$y;$z;$yaw;$pitch"
        }
        return parsed
    }

    override fun convertToEntityAttribute(dbData: String?): Location? {
        if (dbData == null) {
            return null
        }
        return dbData.split(";").let {
            Location(
                Bukkit.getWorld(it[0]),
                it[1].toDouble(),
                it[2].toDouble(),
                it[3].toDouble(),
                it[4].toFloat(),
                it[5].toFloat(),
            )
        }
    }
}