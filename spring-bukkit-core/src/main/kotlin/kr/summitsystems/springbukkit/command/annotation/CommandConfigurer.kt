package kr.summitsystems.springbukkit.command.annotation

import kr.summitsystems.springbukkit.command.CommandFeedbackSource
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverterAdapterRegistry
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverterRegistry

interface CommandConfigurer {
    fun addArgumentConverter(registry: CommandArgumentConverterRegistry) {}

    fun addCommandArgumentCompleterAdapter(registry: CommandArgumentConverterAdapterRegistry) {}

    fun getCommandFeedbackSource(): CommandFeedbackSource? { return null }
}