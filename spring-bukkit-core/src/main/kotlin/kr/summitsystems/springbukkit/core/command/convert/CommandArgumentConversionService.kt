package kr.summitsystems.springbukkit.core.command.convert

import kr.summitsystems.springbukkit.core.command.convert.support.EnumCommandArgumentConverterAdapter
import kr.summitsystems.springbukkit.core.command.convert.support.PlayerCommandArgumentConverter
import kr.summitsystems.springbukkit.core.support.SpringBukkitConversionService
import org.bukkit.Server
import org.springframework.core.DecoratingProxy
import org.springframework.core.ResolvableType
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
            getRequiredTypeInfo(converter.decoratedClass, CommandArgumentConverter::class.java)
        } else {
            getRequiredTypeInfo(converter.javaClass, CommandArgumentConverter::class.java)
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
            getRequiredTypeInfo(adapter.decoratedClass, CommandArgumentConverterAdapter::class.java)
        } else {
            getRequiredTypeInfo(adapter.javaClass, CommandArgumentConverterAdapter::class.java)
        }

        converterAdapters[TypeDescriptor.valueOf(typeInfo)] = adapter
    }

    override fun removeConverterAdapter(targetType: Class<*>) {
        converterAdapters.remove(TypeDescriptor.valueOf(targetType))
    }

    private fun getRequiredTypeInfo(adapterClass: Class<*>, genericInterface: Class<*>): Class<*> {
        val resolvableType = ResolvableType.forClass(adapterClass).`as`(genericInterface)
        val generics = resolvableType.getGenerics()
        require(generics.size == 1) {
            "Unable to determine source type <T> for your " +
                    "CommandArgumentCompleterAdapter [" + adapterClass.getName() + "]; does the class parameterize those types?"
        }
        return generics[0].resolve()!!
    }
}