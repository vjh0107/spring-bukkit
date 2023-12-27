package kr.summitsystems.springbukkit.core.command.convert

interface CommandArgumentConverterAdapterRegistry {
    fun addConverterAdapter(adapter: CommandArgumentConverterAdapter<*>)

    fun removeConverterAdapter(targetType: Class<*>)
}