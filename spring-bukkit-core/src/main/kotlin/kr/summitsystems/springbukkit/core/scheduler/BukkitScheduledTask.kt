package kr.summitsystems.springbukkit.core.scheduler

import kr.summitsystems.springbukkit.core.Disposable
import org.bukkit.plugin.Plugin

interface BukkitScheduledTask : Disposable {
    fun isCurrentlyRunning(): Boolean

    fun getPlugin(): Plugin

    fun isDisposed(): Boolean
}