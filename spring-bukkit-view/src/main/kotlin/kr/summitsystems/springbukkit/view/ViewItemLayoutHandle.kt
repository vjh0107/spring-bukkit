package kr.summitsystems.springbukkit.view

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ViewItemLayoutHandle(
    private val itemStack: ItemStack
) : ViewItemLayout {
    private var actions: MutableList<(event: InventoryClickEvent) -> Unit> = mutableListOf()
    private val actionsByClickType: Multimap<ClickType, (event: InventoryClickEvent) -> Unit> = ArrayListMultimap.create()
    private var cancelOnClick: Boolean = true

    override fun onClick(action: (event: InventoryClickEvent) -> Unit): ViewItemLayout {
        actions.add(action)
        return this
    }

    override fun onClick(clickType: ClickType, action: (event: InventoryClickEvent) -> Unit): ViewItemLayout {
        actionsByClickType.put(clickType, action)
        return this
    }

    override fun cancelOnClick(cancel: Boolean): ViewItemLayout {
        cancelOnClick = cancel
        return this
    }

    override fun getItemStack(): ItemStack {
        return itemStack
    }

    fun handleOnClick(event: InventoryClickEvent) {
        event.isCancelled = cancelOnClick
        actions.forEach { action ->
            action.invoke(event)
        }
        actionsByClickType.forEach { clickType, action ->
            if (event.click == clickType) {
                action.invoke(event)
            }
        }
    }
}