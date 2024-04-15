package kr.junhyung.springbukkit.listener;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

@AutoConfiguration
public class BukkitListenerConfiguration {
    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    static BukkitListenerBeanPostProcessor listenerBeanPostProcessor() {
        return new BukkitListenerBeanPostProcessor();
    }
}