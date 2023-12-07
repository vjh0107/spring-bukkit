package kr.summitsystems.springbukkit.listener.annotation

import kr.summitsystems.springbukkit.listener.BukkitListenerRegistrar
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(javaClass)

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
                    logger.trace("listener named {} registered.", listenerMethod)
                }
            }
        }
        return bean
    }
}