package kr.summitsystems.springbukkit.command.annotation

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverterAdapterRegistry
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverterRegistry

interface CommandConfigurer {
    fun addArgumentConverter(registry: CommandArgumentConverterRegistry) {}

    fun addCommandArgumentCompleterAdapter(registry: CommandArgumentConverterAdapterRegistry) {}

    fun getCoroutineScope(): CoroutineScope? { return null }

    fun getCommandUsageSource(): CommandFeedbackSource? { return null }
}