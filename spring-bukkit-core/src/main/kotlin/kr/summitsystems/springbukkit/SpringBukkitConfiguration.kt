package kr.summitsystems.springbukkit

import kr.summitsystems.springbukkit.checker.annotation.CheckerConfiguration
import kr.summitsystems.springbukkit.command.annotation.CommandConfiguration
import kr.summitsystems.springbukkit.listener.annotation.BukkitListenerConfiguration
import kr.summitsystems.springbukkit.support.config.BukkitConfiguration
import kr.summitsystems.springbukkit.support.config.DefaultConfigurationComposite
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    BukkitConfiguration::class,
    CheckerConfiguration::class,
    CommandConfiguration::class,
    BukkitListenerConfiguration::class,
    DefaultConfigurationComposite::class
)
@Configuration
class SpringBukkitConfiguration