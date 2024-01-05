package kr.summitsystems.springbukkit.core.command.support

import kr.summitsystems.springbukkit.core.command.CommandExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BukkitCommandConfiguration {

    @Bean
    fun bukkitCommandDelegateExecutor(commandExecutor: CommandExecutor): BukkitCommandExecutorDelegate {
        return BukkitCommandExecutorDelegate(commandExecutor)
    }

    @Configuration
    class BukkitCommandRegistrar {
        @Autowired
        fun registerCommand(
            plugin: Plugin,
            pluginDescriptionFile: PluginDescriptionFile,
            commandExecutor: BukkitCommandExecutorDelegate
        ) {
            if (plugin is JavaPlugin) {
                pluginDescriptionFile.commands.keys.forEach { name ->
                    val command = plugin.getCommand(name) ?: return@forEach
                    command.setExecutor(commandExecutor)
                }
            }
        }
    }
}