package kr.summitsystems.springbukkit.support.folia.coroutines

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.internal.MainDispatcherFactory
import kr.summitsystems.springbukkit.support.folia.FoliaUtils

@OptIn(InternalCoroutinesApi::class)
class FoliaMainDispatcherFactory : MainDispatcherFactory {
    override val loadPriority: Int
        get() = -1

    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher {
        return if (FoliaUtils.isUsingFolia()) {
            FoliaMainDispatcher()
        } else {
            allFactories
                .first { it.loadPriority != this.loadPriority }
                .createDispatcher(allFactories)
        }
    }
}