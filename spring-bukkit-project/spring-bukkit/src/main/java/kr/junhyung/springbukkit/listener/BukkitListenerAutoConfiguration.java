package kr.junhyung.springbukkit.listener;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BukkitListenerAutoConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BukkitListenerBeanPostProcessor bukkitListenerAdvisor() {
        return new BukkitListenerBeanPostProcessor();
    }
}