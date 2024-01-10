package kr.summitsystems.springbukkit.core.listener

import org.bukkit.plugin.EventExecutor
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@ConditionalOnMissingBean(EventExecutorFactory::class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Component
class SimpleEventExecutorFactory : EventExecutorFactory {
    override fun create(eventClass: Class<*>, listenerInstance: Any, method: Method): EventExecutor {
        return SimpleEventExecutor(eventClass, listenerInstance, method)
    }
}