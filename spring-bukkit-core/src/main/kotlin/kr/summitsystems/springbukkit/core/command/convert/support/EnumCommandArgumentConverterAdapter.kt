package kr.summitsystems.springbukkit.core.command.convert.support

import kr.summitsystems.springbukkit.core.command.convert.CommandArgumentConverterAdapter

class EnumCommandArgumentConverterAdapter : CommandArgumentConverterAdapter<Enum<*>> {
    override fun provideCompletes(targetType: Class<Enum<*>>): List<String> {
        return targetType.enumConstants.map { it.toString() }
    }
}