package kr.summitsystems.springbukkit.core.command

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import kotlin.coroutines.Continuation
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters

object CommandAopUtils {
    /**
     * <Dropped parameters info>
     *
     * first parameter: CommandContext parameter
     * last parameter: Continuation parameter (Coroutine)
     */
    internal fun extractCommandParameters(method: Method): List<Parameter> {
        return method.parameters
            .filter { parameter ->
                parameter.type != Continuation::class.java && parameter.type != CommandContext::class.java
            }
    }

    internal fun extractCommandParameters(method: KFunction<*>): List<KParameter> {
        return method.valueParameters
    }
}