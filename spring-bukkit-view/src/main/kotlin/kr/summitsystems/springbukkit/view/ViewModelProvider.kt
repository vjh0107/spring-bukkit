package kr.summitsystems.springbukkit.view

interface ViewModelProvider {
    fun <VM : ViewModel> provideViewModel(viewModel: Class<VM>): VM
}