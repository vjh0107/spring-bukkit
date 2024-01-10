package kr.summitsystems.springbukkit.command

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Role
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
class ThreadCommandContextHolder : CommandContextHolder {
    private val contextsByThreadId: MutableMap<Long, CommandContext> = ConcurrentHashMap()

    override fun setContext(context: CommandContext) {
        contextsByThreadId[getThreadId()] = context
    }

    override fun getCurrent(): CommandContext {
        return findById(getThreadId()) ?: throw CommandContextException.NotFound(getThreadId())
    }

    override fun findById(id: Long): CommandContext? {
        return contextsByThreadId[id]
    }

    private fun getThreadId(): Long {
        return Thread.currentThread().id
    }
}