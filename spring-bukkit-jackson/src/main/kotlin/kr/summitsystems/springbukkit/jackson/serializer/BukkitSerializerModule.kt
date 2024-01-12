package kr.summitsystems.springbukkit.jackson.serializer

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import kr.summitsystems.springbukkit.core.support.serializer.BukkitObjectSerializer
import kr.summitsystems.springbukkit.core.util.TypeReflectionUtils
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class BukkitSerializerModule(private val serializerObjectProvider: ObjectProvider<BukkitObjectSerializer<*>>) : Module() {
    override fun version(): Version {
        return Version(1, 0, 0, "", "kr.summitsystems", "springbukkit")
    }

    override fun getModuleName(): String {
        return "bukkit-serializer-module"
    }

    override fun setupModule(context: SetupContext) {
        val serializers = SimpleSerializers()

        serializerObjectProvider.forEach { bukkitObjectSerializer ->
            serializers.addSerializer(BukkitObjectJsonSerializer(bukkitObjectSerializer))
        }
        val deserializers = SimpleDeserializers()
        val deserializersMap = serializerObjectProvider.associate { bukkitObjectSerializer ->
            val type = TypeReflectionUtils.getSingleGenericTypeInfo(
                bukkitObjectSerializer::class.java,
                BukkitObjectSerializer::class.java
            )
            val deserializer = BukkitObjectJsonDeserializer(bukkitObjectSerializer)
            type to deserializer
        }
        deserializers.addDeserializers(deserializersMap)

        context.addSerializers(serializers)
        context.addDeserializers(deserializers)
    }
}