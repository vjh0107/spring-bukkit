package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player

interface Navigator {
    fun <C : ViewInitializationContext> pushView(viewer: Player, view: Class<out View<C>>, context: C, name: String? = null)

    fun popView(viewer: Player)

    fun popViewUntilFirst(viewer: Player)

    fun popViewUntilNamed(viewer: Player, name: String)

    fun popViewAll(viewer: Player)
}