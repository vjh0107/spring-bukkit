package kr.summitsystems.springbukkit.view

import org.bukkit.inventory.ItemStack

interface ViewItemLayoutContainer {
    fun itemLayout(itemStack: ItemStack, slot: Int, vararg additionalSlots: Int): ViewItemLayout {
        val slots = listOf(slot, *additionalSlots.toTypedArray())
        return itemLayout(itemStack, slots)
    }

    fun itemLayout(itemStack: ItemStack, slot: IntRange): ViewItemLayout {
        return itemLayout(itemStack, slot.toList())
    }

    fun itemLayout(itemStack: ItemStack, slots: Collection<Int>): ViewItemLayout

    fun findItemLayout(slot: Int): ViewItemLayout?
}