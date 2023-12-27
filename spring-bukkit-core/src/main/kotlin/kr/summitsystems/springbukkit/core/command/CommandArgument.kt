package kr.summitsystems.springbukkit.core.command

class CommandArgument(
    val value: String,
    val context: CommandContext
) {
    /**
     * for FailureAnalyzer
     */
    override fun toString(): String {
        return value
    }
}