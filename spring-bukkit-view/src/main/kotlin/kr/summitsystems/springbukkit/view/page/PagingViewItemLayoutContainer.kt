package kr.summitsystems.springbukkit.view.page

import kr.summitsystems.springbukkit.view.ViewItemLayout
import kr.summitsystems.springbukkit.view.ViewItemLayoutContainer
import kr.summitsystems.springbukkit.view.ViewItemLayoutHandle
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class PagingViewItemLayoutContainer(inventory: InventoryHolder) : ViewItemLayoutContainer, InventoryHolder by inventory {
    private val viewLayouts: MutableMap<Int, ViewItemLayout> = mutableMapOf()

    override fun itemLayout(itemStack: ItemStack, slots: Collection<Int>): ViewItemLayout {
        val viewItemLayout = ViewItemLayoutHandle(itemStack)

        slots.forEach { targetSlot ->
            viewLayouts[targetSlot] = viewItemLayout
        }
        return viewItemLayout
    }

    override fun findItemLayout(slot: Int): ViewItemLayout? {
        return viewLayouts[slot]
    }

    internal fun setup() {
        inventory.clear()
        viewLayouts.forEach { (slot, layout) ->
            inventory.setItem(slot, layout.getItemStack())
        }
    }
}
