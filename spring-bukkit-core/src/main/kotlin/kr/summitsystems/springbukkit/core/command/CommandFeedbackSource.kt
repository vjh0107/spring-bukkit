package kr.summitsystems.springbukkit.core.command

import java.util.Locale

interface CommandFeedbackSource {
    fun getUsageMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String

    fun getPermissionDeniedMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String
}