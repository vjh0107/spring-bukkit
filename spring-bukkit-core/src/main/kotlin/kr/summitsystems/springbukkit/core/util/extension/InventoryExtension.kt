package kr.summitsystems.springbukkit.core.util.extension

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.hasSpace(itemStacks: Collection<ItemStack>, repeat: Int = 1): Boolean {
    val clonedInventory = Bukkit.getServer().createInventory(null, 36).apply {
        val contents = this@hasSpace.storageContents
        if (contents.isNotEmpty()) {
            contents.filter { itemStack ->
                itemStack.isExist()
            }.forEach { itemStack ->
                addItem(itemStack.clone())
            }
        }
    }
    val clonedItemStacks = itemStacks.map(ItemStack::clone)
    repeat(repeat) {
        val addFailedItems = clonedInventory.addItem(*clonedItemStacks.toTypedArray())
        if (addFailedItems.isNotEmpty()) {
            return false
        }
    }
    return true
}

fun PlayerInventory.hasSpace(itemStack: ItemStack, vararg additionalItemStacks: ItemStack, repeat: Int = 1): Boolean {
    val itemStacks = listOf(itemStack, *additionalItemStacks)
    return hasSpace(itemStacks, repeat)
}