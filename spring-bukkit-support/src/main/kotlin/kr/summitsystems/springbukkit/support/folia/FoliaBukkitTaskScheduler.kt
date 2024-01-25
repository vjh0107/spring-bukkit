package kr.summitsystems.springbukkit.support.folia

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler
import kr.summitsystems.springbukkit.core.scheduler.BukkitScheduledTask
import kr.summitsystems.springbukkit.core.scheduler.BukkitTaskScheduler
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class FoliaBukkitTaskScheduler(
    private val plugin: Plugin
) : BukkitTaskScheduler {
    private val globalRegionScheduler: GlobalRegionScheduler = Bukkit.getGlobalRegionScheduler()

    override fun schedule(task: () -> Unit): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.run(plugin) { task.invoke() }
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: () -> Unit
    ): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.runAtFixedRate(plugin, { task.invoke() }, initialDelayTicks, periodTicks)
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleWithFixedDelay(delayedTicks: Long, task: () -> Unit): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.runDelayed(plugin, { task.invoke() }, delayedTicks)
        return FoliaBukkitScheduledTask(scheduledTask)
    }
}