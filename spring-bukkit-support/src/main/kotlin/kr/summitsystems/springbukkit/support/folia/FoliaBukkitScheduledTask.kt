package kr.summitsystems.springbukkit.support.folia

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import kr.summitsystems.springbukkit.core.scheduler.BukkitScheduledTask
import org.bukkit.plugin.Plugin

class FoliaBukkitScheduledTask(private val delegate: ScheduledTask) : BukkitScheduledTask {
    override fun isCurrentlyRunning(): Boolean {
        return delegate.executionState == ScheduledTask.ExecutionState.RUNNING
    }

    override fun getPlugin(): Plugin {
        return delegate.owningPlugin
    }

    override fun isDisposed(): Boolean {
        return delegate.isCancelled
    }

    override fun dispose() {
        delegate.cancel()
    }
}