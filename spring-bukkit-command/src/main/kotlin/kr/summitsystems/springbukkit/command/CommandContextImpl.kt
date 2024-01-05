package kr.summitsystems.springbukkit.command

import kr.summitsystems.springbukkit.core.util.extension.getLocale
import org.bukkit.command.CommandSender

internal class CommandContextImpl<T : CommandSender>(
    override val sender: T,
    override val label: String,
    private val registrableCommandMapping: RegistrableCommandMapping,
    private val commandMappingRegistry: CommandMappingRegistry,
    private val commandFeedbackSource: CommandFeedbackSource,
    private val parameters: Map<Int, Any?>
) : CommandContext {
    override fun sendUsage() {
        sender.sendMessage(commandFeedbackSource.getUsageMessage(registrableCommandMapping, sender.getLocale()))
    }

    override fun sendUsageAll() {
        commandMappingRegistry
            .findAllByRoot(registrableCommandMapping.qualifier.split(".").first())
            .forEach { mapping ->
                sender.sendMessage(commandFeedbackSource.getUsageMessage(mapping, sender.getLocale()))
            }
    }

    override fun getParameters(): Map<Int, Any?> {
        return parameters
    }
}