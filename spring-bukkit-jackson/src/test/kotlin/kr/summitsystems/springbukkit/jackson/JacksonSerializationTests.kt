package kr.summitsystems.springbukkit.jackson

import be.seeseemelk.mockbukkit.MockBukkitExtension
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kr.summitsystems.springbukkit.serialization.BukkitObjectSerializer
import kr.summitsystems.springbukkit.serialization.DelegatedConfigurationSerializableRegistry
import kr.summitsystems.springbukkit.core.util.extension.item
import kr.summitsystems.springbukkit.jackson.serializer.BukkitObjectJsonDeserializer
import kr.summitsystems.springbukkit.jackson.serializer.BukkitObjectJsonSerializer
import kr.summitsystems.springbukkit.jackson.serializer.BukkitSerializerModule
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockBukkitExtension::class)
class JacksonSerializationTests {
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        val bukkitObjectSerializer = BukkitObjectSerializer()
        val jsonSerializer = BukkitObjectJsonSerializer(bukkitObjectSerializer)
        val jsonDeserializer = BukkitObjectJsonDeserializer(bukkitObjectSerializer)
        val serializerModule = BukkitSerializerModule(jsonSerializer, jsonDeserializer, DelegatedConfigurationSerializableRegistry())
        val objectMapper = ObjectMapper()

        objectMapper.registerModules(serializerModule)
        this.objectMapper = objectMapper
    }

    @Test
    fun serializeTest() {
        val itemStack = item(Material.SALMON)
        val serialized = objectMapper.writeValueAsString(itemStack)
        assertThrows<IOException> {
            objectMapper.readValue<ItemStack>(serialized)
        }
    }
}