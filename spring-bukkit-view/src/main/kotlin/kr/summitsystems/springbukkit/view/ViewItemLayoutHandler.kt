package kr.summitsystems.springbukkit.view

import kr.summitsystems.springbukkit.core.listener.annotation.BukkitListener
import org.bukkit.event.inventory.InventoryClickEvent
import org.springframework.stereotype.Component

@Component
class ViewItemLayoutHandler {
    @BukkitListener
    fun onClick(event: InventoryClickEvent) {
        val item = event.currentItem
        val view = event.inventory.holder
        if (item != null && view is ViewItemLayoutContainer) {
            val layout = view.findItemLayout(event.rawSlot)
            if (layout is ViewItemLayoutHandle) {
                layout.handleOnClick(event)
            }
        }
    }
}
