package kr.summitsystems.springbukkit.core.command.annotation

import kr.summitsystems.springbukkit.core.command.CommandExceptionHandlerRegistry
import kr.summitsystems.springbukkit.core.command.RegistrableCommandExceptionHandler
import org.springframework.aop.framework.AopInfrastructureBean
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodIntrospector
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.annotation.OrderUtils
import org.springframework.core.convert.TypeDescriptor
import java.lang.reflect.Method
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

class CommandControllerAdviceAnnotationBeanPostProcessor(
    private val applicationContext: ApplicationContext
) : BeanPostProcessor {
    private fun getCommandExceptionHandlerRegistry(): CommandExceptionHandlerRegistry {
        return applicationContext.getBean(CommandExceptionHandlerRegistry::class.java)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AopInfrastructureBean) {
            return bean
        }
        val targetClass = AopProxyUtils.ultimateTargetClass(bean)
        if (!AnnotationUtils.isCandidateClass(targetClass, listOf(ExceptionHandler::class.java))) {
            return bean
        }
        val annotatedMethods = MethodIntrospector.selectMethods(targetClass) { method ->
            AnnotatedElementUtils.isAnnotated(method, ExceptionHandler::class.java)
        }
        if (annotatedMethods.isEmpty()) {
            return bean
        }
        annotatedMethods.forEach { method ->
            val annotation = AnnotationUtils.getAnnotation(method, ExceptionHandler::class.java)
            if (annotation?.value == null || annotation.value.isEmpty()) {
                val throwable = method.kotlinFunction!!.valueParameters.firstOrNull()?.type?.jvmErasure?.javaObjectType
                    ?: throw IllegalStateException("The first parameter must be throwable.")
                registerExceptionHandler(throwable, bean, method)
            } else {
                annotation.value.forEach { throwable ->
                    registerExceptionHandler(throwable.javaObjectType, bean, method)
                }
            }
        }
        return bean
    }

    private fun registerExceptionHandler(throwable: Class<*>, bean: Any, method: Method) {
        val order = OrderUtils.getOrder(method)
        getCommandExceptionHandlerRegistry().addExceptionHandler(
            RegistrableCommandExceptionHandler(
                TypeDescriptor.valueOf(throwable),
                bean,
                method,
                order
            )
        )
    }
}