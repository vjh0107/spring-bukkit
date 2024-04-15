package kr.junhyung.testplugin;

import jakarta.annotation.PostConstruct;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestComponent {
    private final Plugin plugin;

    @Autowired
    public TestComponent(Plugin plugin) {
        this.plugin = plugin;
    }

    @PostConstruct
    void postConstruct() {
        plugin.getLogger().info("Hello World!");
    }

    @EventHandler
    public void onEvent(AsyncPlayerChatEvent event) {
        plugin.getLogger().info("pong!");
    }
}
