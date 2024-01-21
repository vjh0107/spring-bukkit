package kr.summitsystems.springbukkit.kotlinx.coroutines

import org.bukkit.plugin.Plugin
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class PluginCoroutineContextElement(
    val plugin: Plugin
) : AbstractCoroutineContextElement(PluginCoroutineContextElement) {
    companion object Key : CoroutineContext.Key<PluginCoroutineContextElement>
}