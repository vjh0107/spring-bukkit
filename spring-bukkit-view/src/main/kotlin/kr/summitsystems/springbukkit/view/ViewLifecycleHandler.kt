package kr.summitsystems.springbukkit.view

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent
import org.springframework.stereotype.Component

@Component
class ViewLifecycleHandler {
    @EventHandler
    fun onViewOpen(event: InventoryOpenEvent) {
        val holder = event.inventory.holder
        if (holder is ViewLifecycle<*>) {
            val player = event.player as? Player ?: return
            holder.onRender(player)
        }
    }
}