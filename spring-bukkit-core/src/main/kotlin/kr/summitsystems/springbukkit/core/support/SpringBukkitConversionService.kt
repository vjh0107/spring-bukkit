package kr.summitsystems.springbukkit.core.support

import org.bukkit.configuration.ConfigurationSection
import org.springframework.boot.convert.ApplicationConversionService
import org.springframework.core.convert.converter.Converter
import java.util.*

open class SpringBukkitConversionService : ApplicationConversionService() {
    init {
        addPropertyConverters()
    }

    private fun addPropertyConverters() {
        addConverter(ConfigurationSectionToMapConverter())
        addConverter(ConfigurationSectionToPropertiesConverter())
    }

    private class ConfigurationSectionToMapConverter : Converter<ConfigurationSection, Map<*, *>> {
        override fun convert(source: ConfigurationSection): Map<*, *> {
            return source.getValues(true)
        }
    }

    @Suppress("IdentifierGrammar")
    private class ConfigurationSectionToPropertiesConverter : Converter<ConfigurationSection, Properties> {
        override fun convert(source: ConfigurationSection): Properties {
            return object : Properties() {
                override val values: MutableCollection<Any>
                    get() = source.getValues(true).values

                override val keys: MutableSet<Any>
                    get() = source.getKeys(true).toMutableSet()
            }
        }
    }
}