package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player

interface ViewFactory {
    fun <C : ViewInitializationContext> create(viewer: Player, view: Class<out View<C>>, context: C): View<C>
}