package kr.summitsystems.springbukkit.support.serializer

import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterOutputStream

object ItemStackSerializer : BukkitObjectSerializer<ItemStack>(true)
object LocationSerializer: BukkitObjectSerializer<Location>()
object PotionEffectSerializer : BukkitObjectSerializer<PotionEffect>()

open class BukkitObjectSerializer<T : ConfigurationSerializable>(
    private val deflate: Boolean = false
) : Serializer<T>, Deserializer<T> {

    override fun serialize(configurationSerializable: T, outputStream: OutputStream) {
        BukkitObjectOutputStream(outputStream).use { bukkitObjectOutputStream ->
            bukkitObjectOutputStream.writeObject(configurationSerializable)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(inputStream: InputStream): T {
        BukkitObjectInputStream(inputStream).use { bukkitObjectInputStream ->
            return bukkitObjectInputStream.readObject() as T
        }
    }

    override fun serializeToByteArray(configurationSerializable: T): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            serialize(configurationSerializable, byteArrayOutputStream)
            return deflateByteArray(byteArrayOutputStream.toByteArray())
        }
    }

    override fun deserializeFromByteArray(byteArray: ByteArray): T {
        val buf = if (deflate) {
            inflateByteArray(byteArray)
        } else {
            byteArray
        }
        ByteArrayInputStream(buf).use { byteArrayInputStream ->
            return deserialize(byteArrayInputStream)
        }
    }

    private fun deflateByteArray(byteArray: ByteArray): ByteArray {
        ByteArrayOutputStream().use {
            DeflaterOutputStream(it).use { outputStream ->
                outputStream.write(byteArray)
            }
            return it.toByteArray()
        }
    }

    private fun inflateByteArray(byteArray: ByteArray): ByteArray {
        ByteArrayOutputStream().use {
            InflaterOutputStream(it).use { inputStream ->
                inputStream.write(byteArray)
            }
            return it.toByteArray()
        }
    }
}