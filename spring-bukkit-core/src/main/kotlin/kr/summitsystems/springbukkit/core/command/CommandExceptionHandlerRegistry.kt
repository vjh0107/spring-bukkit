package kr.summitsystems.springbukkit.core.command

import org.springframework.core.convert.TypeDescriptor

interface CommandExceptionHandlerRegistry {
    fun addExceptionHandler(registrableCommandExceptionHandler: RegistrableCommandExceptionHandler)

    fun find(throwableType: TypeDescriptor): RegistrableCommandExceptionHandler?

    fun getOrderedExceptionHandlers(): List<RegistrableCommandExceptionHandler>
}