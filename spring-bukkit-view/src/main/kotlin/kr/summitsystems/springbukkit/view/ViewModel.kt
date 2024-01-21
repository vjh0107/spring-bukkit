package kr.summitsystems.springbukkit.view

import kr.summitsystems.springbukkit.core.Disposable
import kr.summitsystems.springbukkit.core.DisposableContainer

abstract class ViewModel : DisposableContainer, Disposable {
    private val disposables: MutableList<Disposable> = mutableListOf()

    open fun onDispose() {}

    final override fun dispose() {
        disposables.forEach { it.dispose() }
        onDispose()
    }

    final override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}