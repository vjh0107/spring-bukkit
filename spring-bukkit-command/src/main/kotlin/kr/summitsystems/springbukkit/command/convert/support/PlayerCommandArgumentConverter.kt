package kr.summitsystems.springbukkit.command.convert.support

import kr.summitsystems.springbukkit.command.CommandArgument
import kr.summitsystems.springbukkit.command.convert.CommandArgumentConverter
import org.bukkit.Server
import org.bukkit.entity.Player

class PlayerCommandArgumentConverter(private val server: Server) : CommandArgumentConverter<Player> {
    override fun provideCompletes(): Collection<String> {
        return server.onlinePlayers.map { player ->
            player.name
        }
    }

    override fun convert(source: CommandArgument): Player {
        val value = source.value
        return server.onlinePlayers.single { player ->
            player.name == value
        }
    }
}