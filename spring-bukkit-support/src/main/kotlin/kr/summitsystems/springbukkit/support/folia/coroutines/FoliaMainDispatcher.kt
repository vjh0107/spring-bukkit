package kr.summitsystems.springbukkit.support.folia.coroutines

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kr.summitsystems.springbukkit.core.scheduler.BukkitScheduledTask
import kr.summitsystems.springbukkit.kotlinx.coroutines.dispatcher.BukkitMainDispatcher
import kr.summitsystems.springbukkit.support.folia.FoliaBukkitScheduledTask
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

@InternalCoroutinesApi
open class FoliaMainDispatcher(
    private val globalRegionScheduler: GlobalRegionScheduler = Bukkit.getGlobalRegionScheduler()
) : BukkitMainDispatcher(), Delay {
    override val immediate: MainCoroutineDispatcher
        get() = object : FoliaMainDispatcher(globalRegionScheduler) {
            override val immediate: MainCoroutineDispatcher
                get() = this

            override fun isDispatchNeeded(context: CoroutineContext): Boolean {
                return true
            }
        }

    override fun runTask(plugin: Plugin, task: () -> Unit): BukkitScheduledTask {
        return FoliaBukkitScheduledTask(globalRegionScheduler.run(plugin) { task.invoke() })
    }

    override fun runTaskWithFixedDelay(plugin: Plugin, delay: Long, task: () -> Unit): BukkitScheduledTask {
        return FoliaBukkitScheduledTask(globalRegionScheduler.runDelayed(plugin, { task.invoke() }, delay))
    }
}