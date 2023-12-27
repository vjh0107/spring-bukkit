package kr.summitsystems.springbukkit.core.util.extension

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

fun CommandSender.getLocale(): Locale {
    return when (this) {
        is Player -> getLanguage()
        else -> LocaleContextHolder.getLocale()
    }
}

fun Player.getLanguage(): Locale {
    return Locale(this.locale.split("_").first())
}