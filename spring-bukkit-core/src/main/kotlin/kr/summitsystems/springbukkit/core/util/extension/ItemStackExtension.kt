package kr.summitsystems.springbukkit.core.util.extension

import kr.summitsystems.springbukkit.core.util.BukkitColorUtils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

inline fun <reified IM : ItemMeta> ItemStack.meta(
    block: IM.() -> Unit,
): ItemStack = apply {
    itemMeta = (itemMeta as? IM)?.apply(block)
}

fun item(material: Material, amount: Int = 1, meta: ItemMeta.() -> Unit = {}): ItemStack {
    return ItemStack(material, amount).meta<ItemMeta>(meta)
}

fun ItemStack.displayName(displayName: String?): ItemStack = meta<ItemMeta> {
    this.setDisplayName(displayName?.let { BukkitColorUtils.parse(displayName) })
}

fun ItemStack.lore(lore: List<String>): ItemStack = meta<ItemMeta> {
    this.lore = lore.map { BukkitColorUtils.parse(it) }
}

fun ItemStack.type(material: Material): ItemStack = apply {
    this.type = material
}

fun ItemStack.addLore(lore: String): ItemStack = meta<ItemMeta> {
    this.lore = this.lore?.apply { add(BukkitColorUtils.parse(lore)) } ?: listOf(BukkitColorUtils.parse(lore))
}

fun ItemStack.customModelData(data: Int): ItemStack = meta<ItemMeta> {
    this.setCustomModelData(data)
}

@OptIn(ExperimentalContracts::class)
fun ItemStack?.isExist(): Boolean {
    contract { returns(true) implies (this@isExist != null) }
    return when {
        this == null -> false
        type == Material.AIR -> false
        amount == 0 -> false
        else -> true
    }
}