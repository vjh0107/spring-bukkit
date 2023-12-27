package kr.summitsystems.springbukkit.core.listener.annotation

import kr.summitsystems.springbukkit.core.listener.BukkitListenerRegistrar
import org.apache.commons.logging.LogFactory
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

    private fun getBukkitListenerRegistrar(): BukkitListenerRegistrar {
        return applicationContext.getBean(BukkitListenerRegistrar::class.java)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is AopInfrastructureBean) {
            return bean
        }

        val targetClass = AopProxyUtils.ultimateTargetClass(bean)
        if (AnnotationUtils.isCandidateClass(targetClass, listOf(BukkitListener::class.java))) {
            val annotatedMethods = MethodIntrospector.selectMethods(targetClass) { method ->
                AnnotatedElementUtils.isAnnotated(method, BukkitListener::class.java)
            }
            if (annotatedMethods.isNotEmpty()) {
                annotatedMethods.forEach { listenerMethod ->
                    val event = listenerMethod.parameters.firstOrNull()?.type
                        ?: throw IllegalStateException("first parameter of event handler must be event but null. method: $listenerMethod")

                    val annotation = AnnotationUtils.getAnnotation(listenerMethod, BukkitListener::class.java) ?: throw IllegalStateException("Annotation @Listener not found.")
                    getBukkitListenerRegistrar().registerListener(event, bean, listenerMethod, annotation.handleOrder, annotation.ignoreCancelled)
                    logger.trace("listener named ${listenerMethod.name} registered.")
                }
            }
        }
        return bean
    }
}