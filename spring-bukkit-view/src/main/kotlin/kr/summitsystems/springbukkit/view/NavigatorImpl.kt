package kr.summitsystems.springbukkit.view

import kr.summitsystems.springbukkit.core.listener.annotation.BukkitListener
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class NavigatorImpl(
    private val bukkitScheduler: BukkitScheduler,
    private val plugin: Plugin,
    private val viewFactory: ViewFactory
) : Navigator {
    private val views: ConcurrentHashMap<UUID, Stack<NamedView>> = ConcurrentHashMap()

    @BukkitListener
    fun onInventoryClose(event: InventoryCloseEvent) {
        val view = event.inventory.holder
        val player = event.player

        if (player !is Player || view !is View<*>) {
            return
        }

        if (!view.isStandby() && !view.isDisposed()) {
            popView(player)
            return
        }
    }

    @BukkitListener
    fun onPlayerQuit(event: PlayerQuitEvent) {
        views.remove(event.player.uniqueId)
    }

    override fun <C : ViewInitializationContext> pushView(viewer: Player, view: Class<out View<C>>, context: C, name: String?) {
        val currentView = findPlayerCurrentView(viewer.uniqueId)?.original
        @Suppress("IfThenToSafeAccess")
        if (currentView != null) {
            currentView.standby()
        }
        val instantiatedView = viewFactory.create(viewer, view, context)
        val views = views.computeIfAbsent(viewer.uniqueId) {
            Stack()
        }
        views.push(NamedView(name, instantiatedView))
        openInventory(viewer, instantiatedView.inventory)
    }

    override fun popView(viewer: Player) {
        popView(viewer, true)
    }

    private fun popView(viewer: Player, sendInventory: Boolean) {
        val viewStack = views[viewer.uniqueId]
        if (viewStack.isNullOrEmpty()) {
            return
        }

        viewStack.pop().original.dispose()
        if (viewStack.isNotEmpty()) {
            val previousView = viewStack.peek()
            previousView.original.active()
            if (sendInventory) {
                closeInventory(viewer)
                openInventory(viewer, previousView.original.inventory, 1L)
            }
        }
    }

    override fun popViewAll(viewer: Player) {
        val viewStack = views[viewer.uniqueId]
        if (viewStack.isNullOrEmpty()) {
            return
        }

        while (viewStack.size >= 1) {
            popView(viewer, false)
        }
        closeInventory(viewer)
    }

    override fun popViewUntilFirst(viewer: Player) {
        val viewStack = views[viewer.uniqueId]
        if (viewStack.isNullOrEmpty()) {
            return
        }
        while (viewStack.size >= 3) {
            popView(viewer, false)
        }
        popView(viewer, true)
    }

    override fun popViewUntilNamed(viewer: Player, name: String) {
        val viewStack = views[viewer.uniqueId]
        if (viewStack.isNullOrEmpty()) {
            return
        }
        while (viewStack.size >= 1) {
            if (findPlayerPreviousView(viewer.uniqueId)?.name == name) {
                popView(viewer, true)
                return
            } else {
                popView(viewer, false)
            }
        }
        closeInventory(viewer)
    }

    private fun closeInventory(viewer: Player) {
        bukkitScheduler.runTask(plugin) { _ ->
            viewer.closeInventory()
        }
    }

    private fun openInventory(viewer: Player, inventory: Inventory, delay: Long? = null) {
        if (delay == null) {
            bukkitScheduler.runTask(plugin) { _ ->
                viewer.openInventory(inventory)
            }
        } else {
            bukkitScheduler.runTaskLater(plugin, { _ ->
                viewer.openInventory(inventory)
            }, delay)
        }
    }

    private fun findPlayerCurrentView(viewerId: UUID): NamedView? {
        val viewStack = views[viewerId]
        if (viewStack.isNullOrEmpty()) {
            return null
        }
        return viewStack.peek()
    }

    private fun findPlayerPreviousView(viewerId: UUID): NamedView? {
        val viewStack = views[viewerId]
        if (viewStack.isNullOrEmpty()) {
            return null
        }
        return viewStack.getOrNull(viewStack.size - 2)
    }

    private data class NamedView(val name: String?, val original: View<*>)
}