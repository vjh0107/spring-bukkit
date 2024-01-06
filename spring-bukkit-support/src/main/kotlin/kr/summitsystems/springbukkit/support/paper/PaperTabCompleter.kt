package kr.summitsystems.springbukkit.support.paper

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
import kr.summitsystems.springbukkit.command.CommandTabCompletionProvider
import org.bukkit.event.EventHandler

class PaperTabCompleter(
    private val commandTabCompletionProvider: CommandTabCompletionProvider
) {
    @EventHandler
    fun onTabComplete(event: AsyncTabCompleteEvent) {
        if (!event.isCommand || event.buffer.indexOf(' ') == -1) {
            return
        }

        val completions = commandTabCompletionProvider.provideTabComplete(event.buffer)
        if (completions.isEmpty()) {
            return
        }

        val wrappedCompletions = completions.map { completion ->
            AsyncTabCompleteEvent.Completion.completion(completion)
        }

        event.completions(wrappedCompletions)
        event.isHandled = true
    }
}