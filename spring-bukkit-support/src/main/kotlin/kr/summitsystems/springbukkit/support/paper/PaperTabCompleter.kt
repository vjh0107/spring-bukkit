package kr.summitsystems.springbukkit.support.paper

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
import kr.summitsystems.springbukkit.command.CommandTabCompletionProvider
import org.bukkit.event.EventHandler
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Role
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

@ConditionalOnClass(name = ["com.destroystokyo.paper.event.server.AsyncTabCompleteEvent"])
@Scope(proxyMode = ScopedProxyMode.DEFAULT)
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
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