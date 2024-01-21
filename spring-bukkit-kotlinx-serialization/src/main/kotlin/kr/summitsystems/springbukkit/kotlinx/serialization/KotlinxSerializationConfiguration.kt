package kr.summitsystems.springbukkit.kotlinx.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kr.summitsystems.springbukkit.core.serializer.ConfigurationSerializableRegistry
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KotlinxSerializationConfiguration {

    @Value("\${spring.kotlinx.serialization.ignore-unknown-keys}")
    private var ignoreUnknownKeys: Boolean = true

    @Value("\${spring.kotlinx.serialization.pretty-print}")
    private var prettyPrint: Boolean = false

    @Bean
    fun json(
        serializersModules: ObjectProvider<SerializersModule>
    ): Json {
        return Json {
            this.ignoreUnknownKeys = this@KotlinxSerializationConfiguration.ignoreUnknownKeys
            serializersModules.forEach { module ->
                serializersModule += module
            }
            this.prettyPrint = this@KotlinxSerializationConfiguration.prettyPrint
        }
    }

    @Bean
    fun serializersModule(
        kSerializer: KSerializer<ConfigurationSerializable>,
        configurableSerializationRegistry: ConfigurationSerializableRegistry
    ): SerializersModule {
        return SerializersModule {
            contextual(ConfigurationSerializable::class, kSerializer)
            configurableSerializationRegistry
                .findRegisteredAll()
                .values
                .toSet()
                .forEach { configurationSerializableClass ->
                    contextual(configurationSerializableClass.kotlin) { kSerializer }
                }
        }
    }
}