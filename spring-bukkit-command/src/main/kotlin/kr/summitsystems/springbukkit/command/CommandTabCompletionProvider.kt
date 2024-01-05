package kr.summitsystems.springbukkit.command

interface CommandTabCompletionProvider {
    fun provideTabComplete(inputBuffer: String): List<String>
}