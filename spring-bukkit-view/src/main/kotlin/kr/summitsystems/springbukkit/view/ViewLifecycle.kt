package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player

interface ViewLifecycle<C : ViewInitializationContext> {
    fun onCreate(context: C) {}

    fun onRender(viewer: Player) {}

    fun onDispose() {}
}