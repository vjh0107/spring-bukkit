package kr.summitsystems.springbukkit.command.convert

import kr.summitsystems.springbukkit.command.CommandArgument
import org.springframework.core.convert.converter.Converter

interface CommandArgumentConverter<T> : Converter<CommandArgument, T> {
    fun provideCompletes(): Collection<String>
}