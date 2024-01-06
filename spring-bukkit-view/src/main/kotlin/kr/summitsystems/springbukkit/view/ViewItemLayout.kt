package kr.summitsystems.springbukkit.view

import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

interface ViewItemLayout {
    fun onClick(action: (event: InventoryClickEvent) -> Unit): ViewItemLayout

    fun onClick(clickType: ClickType, action: (event: InventoryClickEvent) -> Unit): ViewItemLayout

    fun cancelOnClick(cancel: Boolean = true): ViewItemLayout

    fun getItemStack(): ItemStack
}