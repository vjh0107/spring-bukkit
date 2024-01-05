package kr.summitsystems.springbukkit.command.support

import kr.summitsystems.springbukkit.command.CommandExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BukkitCommandExecutorDelegate(
    private val commandExecutor: CommandExecutor
) : org.bukkit.command.CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        commandExecutor.executeCommand(sender, label, args)
        return true
    }
}