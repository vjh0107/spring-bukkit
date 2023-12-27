package kr.summitsystems.springbukkit.core.command

interface CommandTabCompletionProvider {
    fun provideTabComplete(inputBuffer: String): List<String>
}