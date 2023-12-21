package kr.summitsystems.springbukkit.coroutines.dispatcher

import kotlinx.coroutines.*
import kr.summitsystems.springbukkit.coroutines.PluginCoroutineContextElement
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.IllegalPluginAccessException
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import kotlin.coroutines.CoroutineContext

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
open class BukkitMainDispatcher(
    private val server: Server = Bukkit.getServer(),
    private val scheduler: BukkitScheduler = Bukkit.getScheduler()
) : MainCoroutineDispatcher(), Delay {

    override val immediate: MainCoroutineDispatcher
        get() = BukkitMainDispatcherImmediate(server, scheduler)

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        try {
            scheduler.runTask(getPluginByCoroutineContext(context), block)
        } catch (_: IllegalPluginAccessException) {
        }
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val runnable = object : BukkitRunnable() {
            override fun run() {
                with(continuation) {
                    resumeUndispatched(Unit)
                }
            }
        }

        val task = try {
            runnable.runTaskLater(getPluginByCoroutineContext(continuation.context), timeMillis / 50)
        } catch (_: IllegalPluginAccessException) {
            null
        }
        continuation.invokeOnCancellation { task?.cancel() }
    }

    private fun getPluginByCoroutineContext(coroutineContext: CoroutineContext): Plugin {
        return coroutineContext[PluginCoroutineContextElement]?.plugin
            ?: throw IllegalStateException("PluginCoroutineContextElement is missing.")
    }

    private class BukkitMainDispatcherImmediate(
        private val server: Server,
        scheduler: BukkitScheduler
    ) : BukkitMainDispatcher(server, scheduler), Delay {
        override val immediate: MainCoroutineDispatcher
            get() = this

        override fun isDispatchNeeded(context: CoroutineContext): Boolean {
            return !server.isPrimaryThread
        }
    }
}