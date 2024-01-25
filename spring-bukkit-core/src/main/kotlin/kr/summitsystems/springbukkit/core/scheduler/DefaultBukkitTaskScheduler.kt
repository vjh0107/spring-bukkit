package kr.summitsystems.springbukkit.core.scheduler

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class DefaultBukkitTaskScheduler(
    private val plugin: Plugin
) : BukkitTaskScheduler {
    private val bukkitScheduler = Bukkit.getScheduler()

    override fun schedule(task: () -> Unit): BukkitScheduledTask {
        val bukkitTask = bukkitScheduler.runTask(plugin, task)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitTask)
    }

    override fun scheduleAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: () -> Unit
    ): BukkitScheduledTask {
        val bukkitTask = bukkitScheduler.runTaskTimer(plugin, task, initialDelayTicks, periodTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitTask)
    }

    override fun scheduleWithFixedDelay(delayedTicks: Long, task: () -> Unit): BukkitScheduledTask {
        val bukkitTask = bukkitScheduler.runTaskLater(plugin, task, delayedTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitTask)
    }
}