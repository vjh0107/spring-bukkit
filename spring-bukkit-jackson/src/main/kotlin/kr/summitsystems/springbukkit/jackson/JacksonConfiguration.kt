package kr.summitsystems.springbukkit.jackson

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {
    @ConditionalOnMissingBean(ObjectMapper::class)
    @Bean
    fun objectMapper(
        parameterNamesModule: ParameterNamesModule,
        kotlinModule: KotlinModule
    ): ObjectMapper {
        return ObjectMapper().apply {
            registerModules(
                parameterNamesModule,
                kotlinModule,
                Jdk8Module(),
                JavaTimeModule(),
            )
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun parameterNamesModule(): ParameterNamesModule {
        return ParameterNamesModule(JsonCreator.Mode.DEFAULT)
    }

    @Bean
    @ConditionalOnMissingBean
    fun kotlinModule(): KotlinModule {
        return KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, true)
            .configure(KotlinFeature.NullToEmptyMap, true)
            .configure(KotlinFeature.NullIsSameAsDefault, true)
            .configure(KotlinFeature.SingletonSupport, true)
            .configure(KotlinFeature.StrictNullChecks, true)
            .build()
    }
}