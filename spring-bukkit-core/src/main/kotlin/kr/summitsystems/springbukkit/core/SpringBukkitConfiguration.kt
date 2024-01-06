package kr.summitsystems.springbukkit.core

import kr.summitsystems.springbukkit.core.checker.annotation.CheckerConfiguration
import kr.summitsystems.springbukkit.core.listener.BukkitListenerConfiguration
import kr.summitsystems.springbukkit.core.support.config.BukkitConfiguration
import kr.summitsystems.springbukkit.core.support.config.DefaultMessageSourceConfiguration
import org.springframework.context.annotation.*

@Suppress("SpringComponentScan")
@ComponentScans(
    ComponentScan(basePackages = ["kr.summitsystems.springbukkit.coroutines"]),
    ComponentScan(basePackages = ["kr.summitsystems.springbukkit.support"]),
    ComponentScan(basePackages = ["kr.summitsystems.springbukkit.command"]),
    ComponentScan(basePackages = ["kr.summitsystems.springbukkit.view"])
)
@Import(
    BukkitConfiguration::class,
    CheckerConfiguration::class,
    BukkitListenerConfiguration::class,
    DefaultMessageSourceConfiguration::class
)
@Configuration
class SpringBukkitConfiguration