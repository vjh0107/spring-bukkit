package kr.summitsystems.springbukkit.support.folia

import kr.summitsystems.springbukkit.core.scheduler.BukkitTaskScheduler
import org.bukkit.plugin.Plugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class FoliaConfiguration {
    @Primary
    @ConditionalOnFolia
    @Bean
    fun foliaBukkitTaskScheduler(
        plugin: Plugin
    ): BukkitTaskScheduler {
        return FoliaBukkitTaskScheduler(plugin)
    }
}