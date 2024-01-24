package kr.summitsystems.springbukkit.command

import jakarta.annotation.PostConstruct
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.stereotype.Component

@Component
class CommandInitializer(
    private val plugin: Plugin,
    private val pluginDescriptionFile: PluginDescriptionFile,
    private val commandExecutor: CommandExecutor
) {
    @PostConstruct
    fun registerCommand() {
        if (plugin is JavaPlugin) {
            pluginDescriptionFile.commands.keys.forEach { name ->
                val command = plugin.getCommand(name) ?: return@forEach
                command.setExecutor(BukkitCommandExecutorDelegate(commandExecutor))
            }
        }
    }

    class BukkitCommandExecutorDelegate(
        private val commandExecutor: CommandExecutor
    ) : org.bukkit.command.CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
            commandExecutor.executeCommand(sender, label, args)
            return true
        }
    }
}