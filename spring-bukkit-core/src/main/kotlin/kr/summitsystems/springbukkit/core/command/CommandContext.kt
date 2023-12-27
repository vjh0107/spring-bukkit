package kr.summitsystems.springbukkit.core.command

import org.bukkit.command.CommandSender

interface CommandContext {
    val sender: CommandSender

    val label: String

    fun sendUsage()

    fun sendUsageAll()

    fun getParameters(): Map<Int, Any?>
}