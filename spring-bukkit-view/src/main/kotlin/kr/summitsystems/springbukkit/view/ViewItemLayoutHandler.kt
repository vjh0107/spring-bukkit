package kr.summitsystems.springbukkit.view

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.springframework.stereotype.Component

@Component
class ViewItemLayoutHandler {
    @EventHandler
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
