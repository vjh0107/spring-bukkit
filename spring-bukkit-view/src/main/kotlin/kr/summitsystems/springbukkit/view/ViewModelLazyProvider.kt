package kr.summitsystems.springbukkit.view

inline fun <reified VM : ViewModel> ViewModelProvider.viewModels(): Lazy<VM> {
    return lazy {
        provideViewModel(VM::class.java)
    }
}
