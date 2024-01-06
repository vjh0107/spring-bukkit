package kr.summitsystems.springbukkit.core.listener

import org.apache.commons.logging.LogFactory
import org.bukkit.event.EventHandler
import org.springframework.aop.framework.AopInfrastructureBean
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodIntrospector
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils

class BukkitListenerAnnotationBeanPostProcessor(
    private val applicationContext: ApplicationContext
) : BeanPostProcessor {
    private val logger = LogFactory.getLog(javaClass)

    private fun getListenerRegistrar(): BukkitListenerRegistrar {
        return applicationContext.getBean(BukkitListenerRegistrar::class.java)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AopInfrastructureBean) {
            return bean
        }

        val targetClass = AopProxyUtils.ultimateTargetClass(bean)
        if (AnnotationUtils.isCandidateClass(targetClass, listOf(EventHandler::class.java))) {
            val annotatedMethods = MethodIntrospector.selectMethods(targetClass) { method ->
                AnnotatedElementUtils.isAnnotated(method, EventHandler::class.java)
            }
            if (annotatedMethods.isNotEmpty()) {
                annotatedMethods.forEach { listenerMethod ->
                    val event = listenerMethod.parameters.firstOrNull()?.type
                        ?: throw IllegalStateException("first parameter of event handler must be event but null. method: $listenerMethod")
                    val annotation = AnnotationUtils.getAnnotation(listenerMethod, EventHandler::class.java) ?: throw IllegalStateException("Annotation @EventHandler not found.")
                    getListenerRegistrar().registerListener(event, bean, listenerMethod, annotation.priority, annotation.ignoreCancelled)
                    logger.trace("listener named ${listenerMethod.name} registered.")
                }
            }
        }
        return bean
    }
}