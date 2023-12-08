package kr.summitsystems.springbukkit.jpa.type

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.Blob
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterOutputStream
import javax.sql.rowset.serial.SerialBlob

@Converter
class ItemStackConverter : AttributeConverter<ItemStack, Blob> {
    override fun convertToDatabaseColumn(attribute: ItemStack?): Blob? {
        if (attribute == null) {
            return null
        }
        return SerialBlob(serializeItemStack(attribute))
    }

    override fun convertToEntityAttribute(dbData: Blob?): ItemStack? {
        if (dbData == null) {
            return null
        }
        val byteArray = dbData.getBytes(1, dbData.length().toInt())
        return deserializeItemStack(byteArray)
    }

    private fun serializeItemStack(itemStack: ItemStack): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            BukkitObjectOutputStream(byteArrayOutputStream).use { bukkitObjectOutputStream ->
                bukkitObjectOutputStream.writeObject(itemStack)
            }
            return compressByteArray(byteArrayOutputStream.toByteArray())
        }
    }

    private fun deserializeItemStack(byteArray: ByteArray): ItemStack {
        ByteArrayInputStream(decompressByteArray(byteArray)).use { byteArrayInputStream ->
            BukkitObjectInputStream(byteArrayInputStream).use { bukkitObjectInputStream ->
                return bukkitObjectInputStream.readObject() as ItemStack
            }
        }
    }

    private fun compressByteArray(byteArray: ByteArray): ByteArray {
        ByteArrayOutputStream().use {
            DeflaterOutputStream(it).use { outputStream ->
                outputStream.write(byteArray)
            }
            return it.toByteArray()
        }
    }

    private fun decompressByteArray(byteArray: ByteArray): ByteArray {
        ByteArrayOutputStream().use {
            InflaterOutputStream(it).use { inputStream ->
                inputStream.write(byteArray)
            }
            return it.toByteArray()
        }
    }
}