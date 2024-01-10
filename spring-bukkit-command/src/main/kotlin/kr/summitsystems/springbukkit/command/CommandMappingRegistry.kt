package kr.summitsystems.springbukkit.command

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Component
class CommandMappingRegistry {
    private val nodes: MutableMap<String, RegistrableCommandMapping> = mutableMapOf()

    fun register(mapping: RegistrableCommandMapping) {
        nodes[mapping.qualifier] = mapping
    }

    fun findAllMappingQualifierStartsWith(string: String): Collection<RegistrableCommandMapping> {
        return nodes.filter { it.key.startsWith(string) }.values
    }

    fun findConflicting(qualifier: String): String? {
        if (nodes[qualifier] != null) {
            return nodes[qualifier]!!.qualifier
        }
        return nodes.keys.firstOrNull { registeredQualifier ->
            if (isRoot(registeredQualifier) || isRoot(qualifier)) {
                return@firstOrNull false
            }
            val qualifierPackage = qualifier
                .split(".")
                .dropLast(1)
                .joinToString(".")
            registeredQualifier == qualifierPackage
        }
    }

    private fun isRoot(qualifier: String): Boolean {
        return !qualifier.contains(".")
    }

    fun find(qualifier: String): RegistrableCommandMapping? {
        return nodes[qualifier]
    }

    fun findAllByRoot(name: String): Collection<RegistrableCommandMapping> {
        return nodes.filter { it.key.startsWith("$name.") }.values
    }
}