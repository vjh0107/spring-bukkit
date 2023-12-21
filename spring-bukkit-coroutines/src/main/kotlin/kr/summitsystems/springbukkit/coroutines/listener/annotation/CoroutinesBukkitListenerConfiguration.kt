package kr.summitsystems.springbukkit.coroutines.listener.annotation

import kotlinx.coroutines.CoroutineScope
import kr.summitsystems.springbukkit.coroutines.listener.CoroutinesEventExecutorFactory
import kr.summitsystems.springbukkit.listener.EventExecutorFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Configuration
class CoroutinesBukkitListenerConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun coroutinesEventExecutorFactory(
        coroutineScope: CoroutineScope
    ): EventExecutorFactory {
        return CoroutinesEventExecutorFactory(coroutineScope)
    }
}