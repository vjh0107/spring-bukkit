package kr.summitsystems.springbukkit.support.folia

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler
import kr.summitsystems.springbukkit.core.scheduler.BukkitScheduledTask
import kr.summitsystems.springbukkit.core.scheduler.BukkitTaskScheduler
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

class FoliaBukkitTaskScheduler(
    private val plugin: Plugin,
    private val globalRegionScheduler: GlobalRegionScheduler = Bukkit.getGlobalRegionScheduler(),
    private val asyncScheduler: AsyncScheduler = Bukkit.getAsyncScheduler()
) : BukkitTaskScheduler {


    override fun schedule(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.run(plugin) {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.runAtFixedRate(plugin, {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }, initialDelayTicks, periodTicks)
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleWithFixedDelay(delayedTicks: Long, task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val scheduledTask = globalRegionScheduler.runDelayed(plugin, {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }, delayedTicks)
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleAsync(task: (BukkitScheduledTask) -> Unit): BukkitScheduledTask {
        val scheduledTask = asyncScheduler.runNow(plugin) {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleAsyncAtFixedRate(
        periodTicks: Long,
        initialDelayTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val scheduledTask = asyncScheduler.runAtFixedRate(plugin, {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }, initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS)
        return FoliaBukkitScheduledTask(scheduledTask)
    }

    override fun scheduleAsyncWithFixedDelay(
        delayedTicks: Long,
        task: (BukkitScheduledTask) -> Unit
    ): BukkitScheduledTask {
        val scheduledTask = asyncScheduler.runDelayed(plugin, {
            val scheduledTask = FoliaBukkitScheduledTask(it)
            task.invoke(scheduledTask)
        }, delayedTicks * 50, TimeUnit.MILLISECONDS)
        return FoliaBukkitScheduledTask(scheduledTask)
    }
}