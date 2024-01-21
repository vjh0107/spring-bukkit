package kr.summitsystems.springbukkit.kotlinx.serialization

import be.seeseemelk.mockbukkit.MockBukkitExtension
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kr.summitsystems.springbukkit.core.serializer.BukkitObjectSerializer
import kr.summitsystems.springbukkit.core.serializer.DelegatedConfigurationSerializableRegistry
import kr.summitsystems.springbukkit.core.util.extension.item
import kr.summitsystems.springbukkit.core.util.extension.print
import kr.summitsystems.springbukkit.kotlinx.serialization.serializer.BukkitObjectKSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.springframework.beans.factory.ObjectProvider
import java.io.IOException
import java.util.function.Consumer
import java.util.stream.Stream

@ExtendWith(MockBukkitExtension::class)
class KotlinxSerializationTests {
    private lateinit var json: Json

    @BeforeEach
    fun setup() {
        val bukkitObjectSerializer = BukkitObjectSerializer()
        val bukkitObjectKSerializer = BukkitObjectKSerializer(bukkitObjectSerializer, bukkitObjectSerializer)
        val configuration = KotlinxSerializationConfiguration()
        val serializerModule = configuration.serializersModule(bukkitObjectKSerializer, DelegatedConfigurationSerializableRegistry())
        val objectProvider = object : ObjectProvider<SerializersModule> by mock() {
            override fun stream(): Stream<SerializersModule> {
                return Stream.of(serializerModule)
            }
        }
        this.json = configuration.json(objectProvider)
    }

    @Test
    fun serializeTest() {
        val itemStack = item(Material.SALMON)
        val serialized = json.encodeToString(itemStack)
        assertThrows<IOException> {
            json.decodeFromString<ItemStack>(serialized)
        }
    }
}