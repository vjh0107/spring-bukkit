package kr.summitsystems.springbukkit.core.checker

sealed class ThreadCheckFailureException(
    message: String,
    val thread: Thread
) : IllegalStateException(message) {
    class MainThread(methodName: String, thread: Thread) : ThreadCheckFailureException(
        "Main-thread dedicated method invoked on async-thread. (method: ${methodName}, thread: ${thread.name})",
        thread
    )

    class AsyncThread(methodName: String, thread: Thread) : ThreadCheckFailureException(
        "Async-thread dedicated method invoked on main-thread. (method: ${methodName}, thread: ${thread.name})",
        thread
    )
}