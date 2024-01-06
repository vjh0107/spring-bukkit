package kr.summitsystems.springbukkit.view.page

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper
import kr.summitsystems.springbukkit.view.ChestView
import kr.summitsystems.springbukkit.view.ChestViewInitializationContext
import kr.summitsystems.springbukkit.view.ViewItemLayout
import kr.summitsystems.springbukkit.view.ViewItemLayoutContainer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class PagingView<C : ChestViewInitializationContext> : ChestView<C>() {
    var currentPage: Int = START_PAGE_INDEX
        private set

    private val pages: MutableMap<Int, PagingViewItemLayoutContainer> by lazy {
        mutableMapOf<Int, PagingViewItemLayoutContainer>().also { map ->
            map[START_PAGE_INDEX] = PagingViewItemLayoutContainer(this)
        }
    }

    private companion object {
        const val START_PAGE_INDEX = 1
    }

    @OverridingMethodsMustInvokeSuper
    override fun initializeInventory(context: C) {
        super.initializeInventory(context)
        pages[START_PAGE_INDEX]!!.setup()
    }

    final override fun onRender(viewer: Player) {
        onPageRender(viewer)
    }

    /**
     * This method indicates that the rendering of the first page is over.
     */
    open fun onPageRender(viewer: Player) {}

    override fun findItemLayout(slot: Int): ViewItemLayout? {
        return getCurrentPageLayouts().findItemLayout(slot)
    }

    override fun itemLayout(itemStack: ItemStack, slot: Int, vararg additionalSlots: Int): ViewItemLayout {
        return getCurrentPageLayouts().itemLayout(itemStack, slot, *additionalSlots)
    }

    fun hasNextPage(): Boolean {
        return findNextPageLayouts() != null
    }

    fun hasPreviousPage(): Boolean {
        return findPreviousPageLayouts() != null
    }

    fun goNextPage(): Boolean {
        val nextPage = findNextPageLayouts() ?: return false
        nextPage.setup()
        currentPage++
        return true
    }

    fun goPreviousPage(): Boolean {
        val previousPage = findPreviousPageLayouts() ?: return false
        previousPage.setup()
        currentPage--
        return true
    }

    fun withPage(page: Int, layout: ViewItemLayoutContainer.() -> Unit) {
        val container = PagingViewItemLayoutContainer(this).apply(layout)
        pages[page] = container
    }

    private fun findNextPageLayouts(): PagingViewItemLayoutContainer? {
        return pages[currentPage + 1]
    }

    private fun findPreviousPageLayouts(): PagingViewItemLayoutContainer? {
        return pages[currentPage - 1]
    }

    private fun getCurrentPageLayouts(): PagingViewItemLayoutContainer {
        return pages[currentPage]!!
    }
}