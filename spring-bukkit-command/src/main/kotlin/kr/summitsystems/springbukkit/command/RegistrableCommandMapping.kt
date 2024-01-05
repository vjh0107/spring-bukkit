package kr.summitsystems.springbukkit.command

import java.lang.reflect.Method

class RegistrableCommandMapping(
    val qualifier: String,
    val controllerInstance: Any,
    val mappingMethod: Method
) {
    fun getSize(): Int {
        return qualifier.split(".").size
    }
}