package kr.summitsystems.springbukkit.serialization

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

@Component
class BukkitObjectSerializer : Serializer<ConfigurationSerializable>, Deserializer<ConfigurationSerializable> {
    override fun serialize(configurationSerializable: ConfigurationSerializable, outputStream: OutputStream) {
        BukkitObjectOutputStream(outputStream).use { bukkitObjectOutputStream ->
            bukkitObjectOutputStream.writeObject(configurationSerializable)
        }
    }

    override fun deserialize(inputStream: InputStream): ConfigurationSerializable {
        BukkitObjectInputStream(inputStream).use { bukkitObjectInputStream ->
            return bukkitObjectInputStream.readObject() as ConfigurationSerializable
        }
    }

    override fun serializeToByteArray(configurationSerializable: ConfigurationSerializable): ByteArray {
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            serialize(configurationSerializable, byteArrayOutputStream)
            return byteArrayOutputStream.toByteArray()
        }
    }

    override fun deserializeFromByteArray(byteArray: ByteArray): ConfigurationSerializable {
        ByteArrayInputStream(byteArray).use { byteArrayInputStream ->
            return deserialize(byteArrayInputStream)
        }
    }
}