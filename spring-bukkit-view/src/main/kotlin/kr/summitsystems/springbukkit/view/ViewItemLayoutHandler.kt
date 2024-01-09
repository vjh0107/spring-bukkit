package kr.summitsystems.springbukkit.view

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
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
