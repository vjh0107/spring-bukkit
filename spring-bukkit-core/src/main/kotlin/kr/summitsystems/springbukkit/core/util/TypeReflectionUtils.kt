package kr.summitsystems.springbukkit.core.util

import org.springframework.core.ResolvableType

object TypeReflectionUtils {
    fun getSingleGenericTypeInfo(targetClass: Class<*>, genericInterface: Class<*>): Class<*> {
        val resolvableType = ResolvableType.forClass(targetClass).`as`(genericInterface)
        val generics = resolvableType.getGenerics()
        require(generics.size == 1) {
            "Unable to determine source type <T> for your class [${targetClass.getName()}]; does the class parameterize those types?"
        }
        return generics[0].resolve()!!
    }

    fun getPairedGenericTypeInfo(targetClass: Class<*>, genericInterface: Class<*>): Pair<Class<*>, Class<*>> {
        val resolvableType = ResolvableType.forClass(targetClass).`as`(genericInterface)
        val generics = resolvableType.getGenerics()
        require(generics.size == 2) {
            "Unable to determine source type <T1, T2> for your class [${targetClass.getName()}]; does the class parameterize those types?"
        }
        val type1 = generics[0].resolve()!!
        val type2 = generics[1].resolve()!!
        return type1 to type2
    }
}