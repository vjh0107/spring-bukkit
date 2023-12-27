package kr.summitsystems.springbukkit.core.support.config

import kr.summitsystems.springbukkit.core.util.BukkitColorUtils
import org.bukkit.plugin.Plugin
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

@Role(BeanDefinition.ROLE_SUPPORT)
@Configuration
class DefaultMessageSourceConfiguration {

    @Role(BeanDefinition.ROLE_SUPPORT)
    @ConditionalOnMissingBean(MessageSource::class)
    @Bean
    fun messageSource(plugin: Plugin): MessageSource {
        val messageSource = object : ReloadableResourceBundleMessageSource() {
            override fun formatMessage(msg: String, args: Array<out Any>?, locale: Locale): String {
                return BukkitColorUtils.parse(super.formatMessage(msg, args, locale))
            }

            override fun getMessageInternal(code: String?, args: Array<out Any>?, locale: Locale?): String? {
                return super.getMessageInternal(code, args, locale)?.let { BukkitColorUtils.parse(it) }
            }
        }

        val sources = listOf(
            "/${plugin.dataFolder.absolutePath}/messages",
            "/${plugin.dataFolder.absolutePath}/message/messages",
            "/${plugin.dataFolder.absolutePath}/messages/messages",
            "/${plugin.dataFolder.absolutePath}/translations",
            "/${plugin.dataFolder.absolutePath}/translation/translations",
            "/${plugin.dataFolder.absolutePath}/translations/translations",
        ).map { File(it).toURI().toString() }.toTypedArray()
        messageSource.setBasenames(*sources)
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name())
        messageSource.setCacheSeconds(-1)
        return messageSource
    }
}