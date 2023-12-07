package kr.summitsystems.springbukkit.command.convert

interface CommandArgumentConverterRegistry {
    fun addConverter(converter: CommandArgumentConverter<*>)

    fun removeConverter(targetType: Class<*>)
}