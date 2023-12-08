package kr.summitsystems.springbukkit.jpa

import jakarta.persistence.AttributeConverter
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@ComponentScan(
    includeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = [AttributeConverter::class])]
)
@Configuration
class SpringBukkitJpaConfiguration