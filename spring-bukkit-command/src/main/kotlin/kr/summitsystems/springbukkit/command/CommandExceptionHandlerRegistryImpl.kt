package kr.summitsystems.springbukkit.command

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.core.convert.TypeDescriptor
import org.springframework.stereotype.Component

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
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
