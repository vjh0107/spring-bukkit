package kr.summitsystems.springbukkit.kotlinx.coroutines.dispatcher

import kotlinx.coroutines.*
import kr.summitsystems.springbukkit.core.scheduler.BukkitScheduledTask
import kr.summitsystems.springbukkit.core.scheduler.DefaultBukkitScheduledTask
import kr.summitsystems.springbukkit.kotlinx.coroutines.PluginCoroutineContextElement
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.IllegalPluginAccessException
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import kotlin.coroutines.CoroutineContext

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
open class BukkitMainDispatcher(
    private val server: Server = Bukkit.getServer(),
    private val scheduler: BukkitScheduler = Bukkit.getScheduler()
) : MainCoroutineDispatcher(), Delay {
    override val immediate: MainCoroutineDispatcher
        get() = object : BukkitMainDispatcher(server, scheduler) {
            override val immediate: MainCoroutineDispatcher
                get() = this

            override fun isDispatchNeeded(context: CoroutineContext): Boolean {
                return !server.isPrimaryThread
            }
        }

    open fun runTask(plugin: Plugin, task: () -> Unit): BukkitScheduledTask {
        return DefaultBukkitScheduledTask(scheduler, scheduler.runTask(plugin, task))
    }

    open fun runTaskWithFixedDelay(plugin: Plugin, delay: Long, task: () -> Unit): BukkitScheduledTask {
        return DefaultBukkitScheduledTask(scheduler, scheduler.runTaskLater(plugin, task, delay))
    }

    final override fun dispatch(context: CoroutineContext, block: Runnable) {
        try {
            runTask(getPluginByCoroutineContext(context)) { block.run() }
        } catch (_: IllegalPluginAccessException) {
        }
    }

    final override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val task = try {
            runTaskWithFixedDelay(getPluginByCoroutineContext(continuation.context), timeMillis / 50) {
                with(continuation) {
                    resumeUndispatched(Unit)
                }
            }
        } catch (_: IllegalPluginAccessException) {
            null
        }
        continuation.invokeOnCancellation { task?.dispose() }
    }

    private fun getPluginByCoroutineContext(coroutineContext: CoroutineContext): Plugin {
        return coroutineContext[PluginCoroutineContextElement]?.plugin
            ?: throw IllegalStateException("PluginCoroutineContextElement is missing.")
    }
}