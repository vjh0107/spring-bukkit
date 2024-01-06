package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player
import org.springframework.beans.factory.Aware

interface ViewerAware : Aware {
    fun setViewer(viewer: Player)
}