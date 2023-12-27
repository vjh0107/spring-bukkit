package kr.summitsystems.springbukkit.core.command.annotation

import kr.summitsystems.springbukkit.core.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConverterAdapterRegistry
import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConverterRegistry

interface CommandConfigurer {
    fun addArgumentConverter(registry: CommandArgumentConverterRegistry) {}

    fun addCommandArgumentCompleterAdapter(registry: CommandArgumentConverterAdapterRegistry) {}

    fun getCommandFeedbackSource(): CommandFeedbackSource? { return null }
}