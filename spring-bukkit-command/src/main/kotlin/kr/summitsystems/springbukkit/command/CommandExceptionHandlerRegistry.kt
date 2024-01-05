package kr.summitsystems.springbukkit.command

import org.springframework.core.convert.TypeDescriptor

interface CommandExceptionHandlerRegistry {
    fun addExceptionHandler(registrableCommandExceptionHandler: RegistrableCommandExceptionHandler)

    fun find(throwableType: TypeDescriptor): RegistrableCommandExceptionHandler?

    fun getOrderedExceptionHandlers(): List<RegistrableCommandExceptionHandler>
}