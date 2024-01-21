package kr.summitsystems.springbukkit.kotlinx.coroutines.command.annotation

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.command.annotation.CommandConfigurer

interface CoroutinesCommandConfigurer : CommandConfigurer {
    fun getCoroutineScope(): CoroutineScope? { return null }
}