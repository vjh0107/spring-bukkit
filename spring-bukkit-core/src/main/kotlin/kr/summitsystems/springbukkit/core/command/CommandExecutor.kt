package kr.summitsystems.springbukkit.core.command

import org.bukkit.command.CommandSender

interface CommandExecutor {
    fun executeCommand(sender: CommandSender, commandName: String, args: Array<String>)
}