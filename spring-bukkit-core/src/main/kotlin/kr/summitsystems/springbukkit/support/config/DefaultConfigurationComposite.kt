package kr.summitsystems.springbukkit.support.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    DefaultAsyncConfiguration::class,
    DefaultMessageSourceConfiguration::class,
    DefaultCoroutineConfiguration::class
)
@Configuration
class DefaultConfigurationComposite