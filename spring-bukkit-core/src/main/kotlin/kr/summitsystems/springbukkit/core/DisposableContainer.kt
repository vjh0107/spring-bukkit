package kr.summitsystems.springbukkit.core

interface DisposableContainer {
    fun addDisposable(disposable: Disposable)
}