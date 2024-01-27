package kr.summitsystems.springbukkit.core.scheduler

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class DefaultBukkitScheduledTask(
    private val bukkitScheduler: BukkitScheduler,
    private val delegate: BukkitRunnable,
    private val owner: Plugin
) : BukkitScheduledTask {
    override fun isCurrentlyRunning(): Boolean {
        return bukkitScheduler.isCurrentlyRunning(delegate.taskId)
    }

    override fun getPlugin(): Plugin {
        return owner
    }

    override fun isDisposed(): Boolean {
        return delegate.isCancelled
    }

    override fun dispose() {
        return delegate.cancel()
    }
}