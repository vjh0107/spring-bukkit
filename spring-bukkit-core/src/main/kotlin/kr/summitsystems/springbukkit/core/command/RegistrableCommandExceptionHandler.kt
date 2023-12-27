package kr.summitsystems.springbukkit.core.command

import org.springframework.core.convert.TypeDescriptor
import java.lang.reflect.Method

class RegistrableCommandExceptionHandler(
    val throwableType: TypeDescriptor,
    val exceptionHandlerInstance: Any,
    val handlerMethod: Method,
    val order: Int?
)