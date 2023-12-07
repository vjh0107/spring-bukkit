package kr.summitsystems.springbukkit.command

sealed class CommandContextException(message: String) : CommandException(message) {
    class NotFound(val id: Long) : CommandContextException("CommandContext not found for id: $id")
}