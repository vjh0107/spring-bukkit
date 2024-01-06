package kr.summitsystems.springbukkit.view

import org.bukkit.Server
import org.bukkit.inventory.Inventory

abstract class ChestView <C : ChestViewInitializationContext> : View<C>() {
    final override fun createInventory(context: C): Inventory {
        return getApplicationContext()
            .getBean(Server::class.java)
            .createInventory(this, context.getRaw() * 9, context.getTitle())
    }
}