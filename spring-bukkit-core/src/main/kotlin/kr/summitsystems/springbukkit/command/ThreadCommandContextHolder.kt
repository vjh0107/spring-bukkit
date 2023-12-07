package kr.summitsystems.springbukkit.command

import java.util.concurrent.ConcurrentHashMap

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