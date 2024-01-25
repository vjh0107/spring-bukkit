package kr.summitsystems.springbukkit.core.scheduler

import org.bukkit.plugin.Plugin
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BukkitSchedulerConfiguration {
    @ConditionalOnMissingBean(BukkitTaskScheduler::class)
    @Bean
    fun defaultBukkitTaskScheduler(
        plugin: Plugin
    ): BukkitTaskScheduler {
        return DefaultBukkitTaskScheduler(plugin)
    }
}