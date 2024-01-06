package kr.summitsystems.springbukkit.view

import kr.summitsystems.springbukkit.core.listener.annotation.BukkitListener
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.springframework.stereotype.Component

@Component
class ViewLifecycleHandler {
    @BukkitListener
    fun onViewOpen(event: InventoryOpenEvent) {
        val holder = event.inventory.holder
        if (holder is ViewLifecycle<*>) {
            val player = event.player as? Player ?: return
            holder.onRender(player)
        }
    }
}