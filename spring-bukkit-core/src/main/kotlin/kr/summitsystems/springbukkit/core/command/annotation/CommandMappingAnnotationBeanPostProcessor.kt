package kr.summitsystems.springbukkit.core.command.annotation

import kr.summitsystems.springbukkit.core.command.CommandMappingException
import kr.summitsystems.springbukkit.core.command.CommandMappingRegistry
import kr.summitsystems.springbukkit.core.command.RegistrableCommandMapping
import org.bukkit.plugin.PluginDescriptionFile
import org.springframework.aop.framework.AopInfrastructureBean
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodIntrospector
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method

class CommandMappingAnnotationBeanPostProcessor(
    private val applicationContext: ApplicationContext
) : BeanPostProcessor {
    private fun getCommandMappingRegistry(): CommandMappingRegistry {
        return applicationContext.getBean(CommandMappingRegistry::class.java)
    }

    private fun getPluginDescriptionFile(): PluginDescriptionFile {
        return applicationContext.getBean(PluginDescriptionFile::class.java)
    }

    private fun getPluginCommands(): Set<String> {
        return getPluginDescriptionFile().commands.keys
    }

    @Suppress("UNCHECKED_CAST")
    private fun getCommandAliases(command: String): List<String> {
        return getPluginDescriptionFile().commands[command]!!["aliases"] as? List<String> ?: emptyList()
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AopInfrastructureBean) {
            return bean
        }
        val targetClass = AopProxyUtils.ultimateTargetClass(bean)
        if (!AnnotationUtils.isCandidateClass(targetClass, listOf(CommandMapping::class.java))) {
            return bean
        }
        val annotatedMethods = MethodIntrospector.selectMethods(targetClass) { method ->
            AnnotatedElementUtils.isAnnotated(method, CommandMapping::class.java)
        }
        if (annotatedMethods.isEmpty()) {
            return bean
        }

        annotatedMethods.forEach { mappingMethod ->
            val command = getCommand(bean, mappingMethod, beanName)
            val qualifier = getCommandQualifier(command, bean, mappingMethod)
            val mayConflicting = getCommandMappingRegistry().findConflicting(qualifier)
            if (mayConflicting != null) {
                throw CommandMappingException.Conflict(qualifier, mayConflicting, bean::class.java, mappingMethod, beanName)
            }
            registerCommandMapping(qualifier, bean, mappingMethod)
            getCommandAliases(command).forEach { alias ->
                val aliasQualifier = getCommandQualifier(alias, bean, mappingMethod)
                registerCommandMapping(aliasQualifier, bean, mappingMethod)
            }
        }

        return bean
    }

    private fun registerCommandMapping(qualifier: String, instance: Any, mappingMethod: Method) {
        with(getCommandMappingRegistry()) {
            register(RegistrableCommandMapping(qualifier, instance, mappingMethod))
            val fallbackPrefixIncludedQualifier = "${getPluginDescriptionFile().name.lowercase()}:${qualifier}"
            register(RegistrableCommandMapping(fallbackPrefixIncludedQualifier, instance, mappingMethod))
        }
    }

    private fun getCommand(bean: Any, target: Method, beanName: String): String {
        val classMapping = AnnotationUtils.findAnnotation(bean::class.java, CommandMapping::class.java)
        val methodMapping = AnnotationUtils.findAnnotation(target, CommandMapping::class.java)
        requireNotNull(methodMapping)
        return if (classMapping == null || classMapping.command == "") {
            if (methodMapping.command == "") {
                val pluginCommands = getPluginCommands()
                if (pluginCommands.size == 1) {
                    pluginCommands.single()
                } else if (pluginCommands.size > 1) {
                    throw CommandMappingException.NoMapping(bean::class.java, target, beanName)
                } else {
                    throw CommandMappingException.NoMapping(bean::class.java, target, beanName)
                }
            } else {
                methodMapping.command
            }
        } else {
            if (methodMapping.command == "") {
                classMapping.command
            } else {
                throw CommandMappingException.Ambiguous(classMapping.command, methodMapping.command, bean::class.java, target, beanName)
            }
        }
    }

    private fun getCommandQualifier(command: String, bean: Any, target: Method): String {
        val classMapping = AnnotationUtils.findAnnotation(bean::class.java, CommandMapping::class.java)
        val methodMapping = AnnotationUtils.findAnnotation(target, CommandMapping::class.java)
        requireNotNull(methodMapping)

        fun getPathQualifier(pathInput: String): String {
            return pathInput
                .removeSuffix(" ")
                .replace(" ", ".")
        }

        return if (classMapping == null || classMapping.path == "") {
            if (methodMapping.path == "") {
                command
            } else {
                val methodMappingQualifier = getPathQualifier(methodMapping.path)
                "${command}.${methodMappingQualifier}"
            }
        } else {
            val classMappingQualifier = getPathQualifier(classMapping.path)
            if (methodMapping.path == "") {
                "${command}.${classMappingQualifier}"
            } else {
                val methodMappingQualifier = getPathQualifier(methodMapping.path)
                "${command}.${classMappingQualifier}.${methodMappingQualifier}"
            }
        }
    }
}