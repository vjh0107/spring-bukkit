package kr.summitsystems.springbukkit.reactive.view

import kr.summitsystems.springbukkit.core.DisposableContainer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun <T> DisposableContainer.subscribe(flux: Flux<T>, consumer: (T) -> Unit) {
    val disposable = flux.subscribe {
        consumer.invoke(it)
    }
    this.addDisposable {
        disposable.dispose()
    }
}

fun <T> DisposableContainer.subscribe(mono: Mono<T>, consumer: (T) -> Unit) {
    val disposable = mono.subscribe {
        consumer.invoke(it)
    }
    this.addDisposable {
        disposable.dispose()
    }
}