package kr.summitsystems.springbukkit.coroutines.listener

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.core.listener.EventExecutorFactory
import org.bukkit.plugin.EventExecutor
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
class CoroutinesEventExecutorFactory(private val coroutineScope: CoroutineScope) : EventExecutorFactory {
    override fun create(eventClass: Class<*>, listenerInstance: Any, method: Method): EventExecutor {
        return CoroutinesEventExecutor(coroutineScope, eventClass, listenerInstance, method)
    }
}