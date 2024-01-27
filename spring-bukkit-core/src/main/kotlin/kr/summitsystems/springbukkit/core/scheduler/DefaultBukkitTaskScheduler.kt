package kr.summitsystems.springbukkit.core.scheduler

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class DefaultBukkitTaskScheduler(
    private val plugin: Plugin
) : BukkitTaskScheduler {
    private val bukkitScheduler = Bukkit.getScheduler()

    override fun schedule(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTask(plugin)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    override fun scheduleAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTaskTimer(plugin, initialDelayTicks, periodTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    override fun scheduleWithFixedDelay(delayedTicks: Long, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTaskLater(plugin, delayedTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    override fun scheduleAsync(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTaskAsynchronously(plugin)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    override fun scheduleAsyncAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTaskTimerAsynchronously(plugin, initialDelayTicks, periodTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    override fun scheduleAsyncWithFixedDelay(
        delayedTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val bukkitRunnable = taskToBukkitRunnable(task)
        bukkitRunnable.runTaskLaterAsynchronously(plugin, delayedTicks)
        return DefaultBukkitScheduledTask(bukkitScheduler, bukkitRunnable, plugin)
    }

    private fun taskToBukkitRunnable(task: (BukkitScheduledTask) -> Unit): BukkitRunnable {
        return object : BukkitRunnable() {
            override fun run() {
                val scheduledTask = DefaultBukkitScheduledTask(bukkitScheduler, this, plugin)
                task.invoke(scheduledTask)
            }
        }
    }
}