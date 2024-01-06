package kr.summitsystems.springbukkit.view

abstract class ViewModel {
    open fun onDispose() {}

    fun dispose() {
        onDispose()
    }
}