package kr.summitsystems.springbukkit.view

class ViewModelStore(
    private val map: MutableMap<String, ViewModel> = mutableMapOf()
) : MutableMap<String, ViewModel> by map {
    override fun put(key: String, value: ViewModel): ViewModel? {
        val oldViewModel = map.put(key, value)
        oldViewModel?.dispose()
        return oldViewModel
    }

    fun put(viewModel: ViewModel): ViewModel? {
        return put(viewModel::class.java.name, viewModel)
    }

    override fun clear() {
        for (viewModel in values) {
            viewModel.dispose()
        }
        map.clear()
    }
}