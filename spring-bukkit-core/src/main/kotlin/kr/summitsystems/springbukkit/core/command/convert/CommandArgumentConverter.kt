package kr.summitsystems.springbukkit.core.command.convert

import kr.summitsystems.springbukkit.core.command.CommandArgument
import org.springframework.core.convert.converter.Converter

interface CommandArgumentConverter<T> : Converter<CommandArgument, T> {
    fun provideCompletes(): Collection<String>
}