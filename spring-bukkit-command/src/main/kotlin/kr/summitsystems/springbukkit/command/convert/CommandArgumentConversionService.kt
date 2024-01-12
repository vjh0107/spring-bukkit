package kr.summitsystems.springbukkit.command.convert

import kr.summitsystems.springbukkit.command.convert.support.EnumCommandArgumentConverterAdapter
import kr.summitsystems.springbukkit.command.convert.support.PlayerCommandArgumentConverter
import kr.summitsystems.springbukkit.core.support.SpringBukkitConversionService
import kr.summitsystems.springbukkit.core.util.TypeReflectionUtils
import org.bukkit.Server
import org.springframework.core.DecoratingProxy
import org.springframework.core.convert.TypeDescriptor

open class CommandArgumentConversionService(
    private val server: Server
) : SpringBukkitConversionService(), CommandArgumentConverterAdapterRegistry, CommandArgumentConverterRegistry {
    private val converterAdapters: MutableMap<TypeDescriptor, CommandArgumentConverterAdapter<*>> = mutableMapOf()
    private val converters: MutableMap<TypeDescriptor, CommandArgumentConverter<*>> = mutableMapOf()

    init {
        addDefaults()
    }

    private fun addDefaults() {
        addConverter(PlayerCommandArgumentConverter(server))
        addConverterAdapter(EnumCommandArgumentConverterAdapter())
    }

    override fun addConverter(converter: CommandArgumentConverter<*>) {
        val typeInfo = if (converter is DecoratingProxy) {
            TypeReflectionUtils.getSingleGenericTypeInfo(converter.decoratedClass, CommandArgumentConverter::class.java)
        } else {
            TypeReflectionUtils.getSingleGenericTypeInfo(converter.javaClass, CommandArgumentConverter::class.java)
        }
        converters[TypeDescriptor.valueOf(typeInfo)] = converter
        super.addConverter(converter)
    }

    override fun removeConverter(targetType: Class<*>) {
        converters.remove(TypeDescriptor.valueOf(targetType))
    }

    fun findCommandArgumentConverter(typeDescriptor: TypeDescriptor): CommandArgumentConverter<*>? {
        return converters[typeDescriptor]
    }

    fun findCommandArgumentConverterAdapter(typeDescriptor: TypeDescriptor): CommandArgumentConverterAdapter<*>? {
        return converterAdapters[typeDescriptor]
    }

    override fun addConverterAdapter(adapter: CommandArgumentConverterAdapter<*>) {
        val typeInfo = if (adapter is DecoratingProxy) {
            TypeReflectionUtils.getSingleGenericTypeInfo(adapter.decoratedClass, CommandArgumentConverterAdapter::class.java)
        } else {
            TypeReflectionUtils.getSingleGenericTypeInfo(adapter.javaClass, CommandArgumentConverterAdapter::class.java)
        }

        converterAdapters[TypeDescriptor.valueOf(typeInfo)] = adapter
    }

    override fun removeConverterAdapter(targetType: Class<*>) {
        converterAdapters.remove(TypeDescriptor.valueOf(targetType))
    }
}