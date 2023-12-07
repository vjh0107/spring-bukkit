package kr.summitsystems.springbukkit.checker.annotation

import org.bukkit.Server
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
class CheckerConfiguration {
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    fun threadCheckerAdvisor(server: Server): ThreadCheckerAdvisor {
        return ThreadCheckerAdvisor(server)
    }
}