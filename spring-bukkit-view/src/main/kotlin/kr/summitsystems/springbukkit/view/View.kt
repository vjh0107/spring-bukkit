package kr.summitsystems.springbukkit.view

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper
import kr.summitsystems.springbukkit.core.Disposable
import kr.summitsystems.springbukkit.core.DisposableContainer
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.support.GenericApplicationContext

abstract class View<C : ViewInitializationContext> : ViewLifecycle<C>, ViewItemLayoutContainer, DisposableContainer, InventoryHolder, ViewModelProvider, ApplicationContextAware, Navigator,
    Disposable {
    private val viewModelStore: ViewModelStore = ViewModelStore()
    private val viewLayouts: MutableMap<Int, ViewItemLayout> = mutableMapOf()
    private var isStandby: Boolean = false
    private var isDisposed: Boolean = false
    private val disposables: MutableList<Disposable> = mutableListOf()
    private lateinit var applicationContext: ApplicationContext
    private lateinit var inventory: Inventory

    final override fun getInventory(): Inventory {
        return inventory
    }

    @OverridingMethodsMustInvokeSuper
    internal open fun initializeInventory(context: C) {
        inventory = createInventory(context)
        onCreate(context)
    }

    abstract fun createInventory(context: C): Inventory

    private fun getNavigator(): Navigator {
        return getApplicationContext().getBean(Navigator::class.java)
    }

    final override fun <C : ViewInitializationContext> pushView(
        viewer: Player,
        view: Class<out View<C>>,
        context: C,
        name: String?
    ) {
        getNavigator().pushView(viewer, view, context, name)
    }

    final override fun popView(viewer: Player) {
        getNavigator().popView(viewer)
    }

    final override fun popViewAll(viewer: Player) {
        getNavigator().popViewAll(viewer)
    }

    final override fun popViewUntilFirst(viewer: Player) {
        getNavigator().popViewUntilFirst(viewer)
    }

    final override fun popViewUntilNamed(viewer: Player, name: String) {
        getNavigator().popViewUntilNamed(viewer, name)
    }

    internal fun standby() {
        this.isStandby = true
    }

    fun isStandby(): Boolean {
        return isStandby
    }

    internal fun active() {
        this.isStandby = false
    }

    final override fun dispose() {
        viewModelStore.clear()
        disposables.forEach { it.dispose() }
        onDispose()
        this.isDisposed = true
    }

    fun isDisposed(): Boolean {
        return isDisposed
    }

    final override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun getApplicationContext(): ApplicationContext {
        return applicationContext
    }

    override fun <VM : ViewModel> provideViewModel(viewModel: Class<VM>): VM {
        val context = getApplicationContext() as GenericApplicationContext
        return context.beanFactory.createBean(viewModel).also { instantiatedViewModel ->
            viewModelStore.put(instantiatedViewModel)
        }
    }

    override fun itemLayout(itemStack: ItemStack, slots: Collection<Int>): ViewItemLayout {
        val viewItemLayout = ViewItemLayoutHandle(itemStack)
        slots.forEach { slot ->
            viewLayouts[slot] = viewItemLayout
            getInventory().setItem(slot, itemStack)
        }
        return viewItemLayout
    }

    override fun findItemLayout(slot: Int): ViewItemLayout? {
        return viewLayouts[slot]
    }

    final override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}