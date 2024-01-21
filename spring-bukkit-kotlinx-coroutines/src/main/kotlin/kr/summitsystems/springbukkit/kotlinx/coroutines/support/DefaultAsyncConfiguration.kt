package kr.summitsystems.springbukkit.kotlinx.coroutines.support

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import java.util.concurrent.Executor

@ConditionalOnMissingBean(AsyncConfigurer::class)
@Configuration
class DefaultAsyncConfiguration : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor? {
        return Dispatchers.Default.asExecutor()
    }
}