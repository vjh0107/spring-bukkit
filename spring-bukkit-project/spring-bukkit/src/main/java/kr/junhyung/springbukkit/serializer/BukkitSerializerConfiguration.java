package kr.junhyung.springbukkit.serializer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

@AutoConfiguration
public class BukkitSerializerConfiguration {
    @Role(BeanDefinition.ROLE_SUPPORT)
    @Bean
    BukkitSerializer bukkitSerializer() {
        return new BukkitSerializer();
    }
}