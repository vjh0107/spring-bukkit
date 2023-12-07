package kr.summitsystems.springbukkit.command

interface CommandContextHolder {
    fun getCurrent(): CommandContext

    fun setContext(context: CommandContext)

    /**
     * @throws CommandContextException.NotFound
     */
    fun findById(id: Long): CommandContext?
}