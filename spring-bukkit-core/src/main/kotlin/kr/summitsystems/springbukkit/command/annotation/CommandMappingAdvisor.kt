package kr.summitsystems.springbukkit.command.annotation

import kr.summitsystems.springbukkit.command.CommandContext
import kr.summitsystems.springbukkit.command.CommandContextHolder
import kr.summitsystems.springbukkit.command.CommandExceptionHandlerRegistry
import kr.summitsystems.springbukkit.command.RegistrableCommandExceptionHandler
import kr.summitsystems.springbukkit.util.BukkitColorUtils
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.bukkit.command.CommandSender
import org.springframework.core.convert.TypeDescriptor
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.kotlinFunction

@Aspect
class CommandMappingAdvisor(
    private val registry: CommandExceptionHandlerRegistry,
    private val commandContextHolder: CommandContextHolder
) {
    private companion object UnhandledExceptionFeedbackMessages {
        const val TITLE = "Unhandled exception (Click to copy details):"
        const val NAME = " - name: §7%s"
        const val MESSAGE = " - message: §7%s"
    }

    @Pointcut("execution(* kr.summitsystems.springbukkit.command.CommandExecutor.executeCommand(..))")
    fun pointcut() {
    }

    @Around(value = "pointcut()")
    fun handleExceptions(joinPoint: ProceedingJoinPoint) {
        val sender = joinPoint.args.first() as? CommandSender
            ?: throw IllegalStateException("The first parameter of the executeCommand method must be a command dispatcher.")
        try {
            joinPoint.proceed()
        } catch (throwable: Throwable) {
            val throwableType = TypeDescriptor.valueOf(throwable::class.javaObjectType)
            val handler = registry.find(throwableType)

            if (handler == null) {
                // Deep Search Start
                val deepSearchedHandlers: MutableList<RegistrableCommandExceptionHandler> = mutableListOf()
                throwable::class.allSuperclasses.forEach { superClass ->
                    val maySearched = registry.find(TypeDescriptor.valueOf(superClass.javaObjectType))
                    if (maySearched != null) {
                        deepSearchedHandlers.add(maySearched)
                    }
                }
                val candidateHandler = deepSearchedHandlers.sortedBy { it.order }.firstOrNull()
                if (candidateHandler == null) {
                    sendUnhandledExceptionMessage(sender, throwable)
                } else {
                    invokeExceptionHandler(candidateHandler, throwable)
                }

            } else {
               invokeExceptionHandler(handler, throwable)
            }
        }
    }

    private fun invokeExceptionHandler(handler: RegistrableCommandExceptionHandler, throwable: Throwable) {
        if (handler.handlerMethod.kotlinFunction!!.extensionReceiverParameter?.type == CommandContext::class.starProjectedType) {
            val context = commandContextHolder.getCurrent()
            handler.handlerMethod.invoke(handler.exceptionHandlerInstance, context, throwable)
        } else {
            handler.handlerMethod.invoke(handler.exceptionHandlerInstance, throwable)
        }
    }

    private fun sendUnhandledExceptionMessage(sender: CommandSender, throwable: Throwable) {
        with(sender.spigot()) {
            sendMessage(buildUnhandledExceptionMessage(throwable, TITLE, "#DB0000"))
            val throwableName = throwable::class.java.simpleName
            sendMessage(buildUnhandledExceptionMessage(throwable, String.format(NAME, throwableName), "#DF4D4D"))
            sendMessage(buildUnhandledExceptionMessage(throwable, String.format(MESSAGE, throwable.message), "#DF4D4D"))
        }
    }

    private fun buildUnhandledExceptionMessage(throwable: Throwable, message: String, color: String): TextComponent {
        return TextComponent().apply {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))
            this.clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stringWriter.toString())
            this.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to copy details."))
            this.color = ChatColor.of(color)
            this.text = BukkitColorUtils.parse(message)
        }
    }
}