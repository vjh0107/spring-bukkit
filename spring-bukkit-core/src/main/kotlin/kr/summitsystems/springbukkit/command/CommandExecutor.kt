package kr.summitsystems.springbukkit.command

import org.bukkit.command.CommandSender

interface CommandExecutor {
    fun executeCommand(sender: CommandSender, commandName: String, args: Array<String>)
}