package kr.summitsystems.springbukkit.coroutines

import kr.summitsystems.springbukkit.core.SpringBukkitAutoConfiguration
import kr.summitsystems.springbukkit.coroutines.command.annotation.CoroutinesCommandConfiguration
import kr.summitsystems.springbukkit.coroutines.listener.annotation.CoroutinesBukkitListenerConfiguration
import kr.summitsystems.springbukkit.coroutines.support.DefaultAsyncConfiguration
import kr.summitsystems.springbukkit.coroutines.support.DefaultCoroutineConfiguration
import org.springframework.context.annotation.Import

@Import(
    DefaultCoroutineConfiguration::class,
    DefaultAsyncConfiguration::class,
    CoroutinesCommandConfiguration::class,
    CoroutinesBukkitListenerConfiguration::class
)
@SpringBukkitAutoConfiguration
class SpringBukkitCoroutinesConfiguration