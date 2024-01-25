package kr.summitsystems.springbukkit.support.folia

import kr.summitsystems.springbukkit.core.scheduler.BukkitTaskScheduler
import org.bukkit.plugin.Plugin
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FoliaConfiguration {
    @ConditionalOnClass(name = ["io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler"])
    @Bean
    fun foliaBukkitTaskScheduler(plugin: Plugin): BukkitTaskScheduler {
        return FoliaBukkitTaskScheduler(plugin)
    }
}