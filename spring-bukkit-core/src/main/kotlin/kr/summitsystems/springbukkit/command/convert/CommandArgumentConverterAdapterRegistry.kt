package kr.summitsystems.springbukkit.command.convert

interface CommandArgumentConverterAdapterRegistry {
    fun addConverterAdapter(adapter: CommandArgumentConverterAdapter<*>)

    fun removeConverterAdapter(targetType: Class<*>)
}