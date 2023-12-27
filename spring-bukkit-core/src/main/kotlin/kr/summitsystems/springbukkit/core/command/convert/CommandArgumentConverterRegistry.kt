package kr.summitsystems.springbukkit.core.command.convert

interface CommandArgumentConverterRegistry {
    fun addConverter(converter: CommandArgumentConverter<*>)

    fun removeConverter(targetType: Class<*>)
}