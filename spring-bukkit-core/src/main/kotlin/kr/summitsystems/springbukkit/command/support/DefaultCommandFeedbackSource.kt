package kr.summitsystems.springbukkit.command.support

import kr.summitsystems.springbukkit.command.CommandAopUtils
import kr.summitsystems.springbukkit.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.command.RegistrableCommandMapping
import kr.summitsystems.springbukkit.command.annotation.CommandArgument
import org.springframework.context.MessageSource
import java.util.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.kotlinFunction

class DefaultCommandFeedbackSource(
    private val messageSource: MessageSource
) : CommandFeedbackSource {
    private companion object {
        const val USAGE_MESSAGE_KEY = "command.usage"
        const val USAGE_DEFAULT_MESSAGE = "<#FAED7D>/{0}"

        const val ARGUMENT_BRACKET_KEY = "command.argument.bracket"
        const val OPTIONAL_ARGUMENT_BRACKET_KEY = "command.argument.bracket.optional"
        const val DEFAULT_ARGUMENT_BRACKET = "<{0}>"
        const val DEFAULT_OPTIONAL_ARGUMENT_BRACKET = "[{0}]"

        const val PERMISSION_DENIED_KEY = "command.permission.denied"
        const val DEFAULT_PERMISSION_DENIED = "<#DF4D4D>Permission denied."
    }

    override fun getUsageMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String {
        val parametersExtracted =
            CommandAopUtils.extractCommandParameters(registrableCommandMapping.mappingMethod.kotlinFunction!!)
        val arguments = parametersExtracted
            .map { parameter ->
                val name = if (parameter.hasAnnotation<CommandArgument>()) {
                    val annotation = parameter.findAnnotation<CommandArgument>()!!
                    if (annotation.messageSource != "") {
                        messageSource.getMessage(annotation.messageSource, emptyArray<Any?>(), null, locale)
                            ?: if (annotation.label != "") {
                                annotation.label
                            } else {
                                parameter.name
                            }
                    } else {
                        if (annotation.label != "") {
                            annotation.label
                        } else {
                            parameter.name
                        }
                    }
                } else {
                    parameter.name
                }
                if (parameter.isOptional || parameter.type.isMarkedNullable) {
                    messageSource.getMessage(
                        OPTIONAL_ARGUMENT_BRACKET_KEY,
                        arrayOf(name),
                        DEFAULT_OPTIONAL_ARGUMENT_BRACKET,
                        locale
                    )
                } else {
                    messageSource.getMessage(ARGUMENT_BRACKET_KEY, arrayOf(name), DEFAULT_ARGUMENT_BRACKET, locale)
                }
            }
            .joinToString(" ")
        val label = registrableCommandMapping.qualifier
            .split(".")
            .first()
            .let { label ->
                if (label.contains(":")) {
                    label.split(":").last()
                } else {
                    label
                }
            }
        val subLabels = registrableCommandMapping.qualifier
            .split(".")
            .drop(1)
            .joinToString(" ")

        val usage = "$label $subLabels $arguments"
        return messageSource.getMessage(USAGE_MESSAGE_KEY, arrayOf(usage), USAGE_DEFAULT_MESSAGE, locale)!!
    }


    override fun getPermissionDeniedMessage(registrableCommandMapping: RegistrableCommandMapping, locale: Locale): String {
        return messageSource.getMessage(PERMISSION_DENIED_KEY, arrayOf(), DEFAULT_PERMISSION_DENIED, locale)!!
    }
}