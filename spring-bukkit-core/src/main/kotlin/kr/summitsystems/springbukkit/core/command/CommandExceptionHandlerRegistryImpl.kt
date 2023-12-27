package kr.summitsystems.springbukkit.core.command

import org.springframework.core.convert.TypeDescriptor

internal class CommandExceptionHandlerRegistryImpl : CommandExceptionHandlerRegistry {
    private val handlers: MutableMap<TypeDescriptor, RegistrableCommandExceptionHandler> = mutableMapOf()

    override fun addExceptionHandler(registrableCommandExceptionHandler: RegistrableCommandExceptionHandler) {
        handlers[registrableCommandExceptionHandler.throwableType] = registrableCommandExceptionHandler
    }

    override fun find(throwableType: TypeDescriptor): RegistrableCommandExceptionHandler? {
        return handlers[throwableType]
    }

    override fun getOrderedExceptionHandlers(): List<RegistrableCommandExceptionHandler> {
        return handlers
            .values
            .sortedBy {
                it.order
            }
    }
}
