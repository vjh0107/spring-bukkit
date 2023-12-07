package kr.summitsystems.springbukkit.command

import java.util.Locale

interface CommandFeedbackSource {
    fun getUsageMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String

    fun getPermissionDeniedMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String
}