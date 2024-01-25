package kr.summitsystems.springbukkit.support.folia.coroutines

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.internal.MainDispatcherFactory

@OptIn(InternalCoroutinesApi::class)
class FoliaMainDispatcherFactory : MainDispatcherFactory {
    override val loadPriority: Int
        get() = -1

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher {
        return if (classExists("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler")) {
            FoliaMainDispatcher()
        } else {
            allFactories
                .first { it.loadPriority != this.loadPriority }
                .createDispatcher(allFactories)
        }
    }

    private fun classExists(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (ex: ClassNotFoundException) {
            false
        }
    }
}