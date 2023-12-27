package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.summitsystems.springbukkit.core.support.serializer.ItemStackSerializer
import org.bukkit.inventory.ItemStack

@Converter
class ItemStackConverter : AttributeConverter<ItemStack, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ItemStack?): ByteArray? {
        if (attribute == null) {
            return null
        }
        return ItemStackSerializer.serializeToByteArray(attribute)
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): ItemStack? {
        if (dbData == null) {
            return null
        }
        return ItemStackSerializer.deserializeFromByteArray(dbData)
    }
}