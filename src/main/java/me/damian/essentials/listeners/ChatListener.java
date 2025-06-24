package me.damian.essentials.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.damian.essentials.DamiEssentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

import static me.damian.core.DamiUtils.colorize;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class ChatListener implements Listener {

    private static final Map<Player, Long> chatCooldowns = new HashMap<>();
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent event) {
        if(System.currentTimeMillis() > chatCooldowns.getOrDefault(event.getPlayer(), 0L) || event.getPlayer().hasPermission("dami-essentials.bypass-chat-delay")) {
            String safeMessage = event.getMessage().replace("%", "%%");
            String format = DamiEssentials.getInstance().getConfig().getString("ChatFormat", "%vault_prefix% %player_name%&8: %message%")
                    .replace("%message%", safeMessage);
            event.setFormat(colorize(PlaceholderAPI.setPlaceholders(event.getPlayer(), format)));
            chatCooldowns.put(event.getPlayer(), System.currentTimeMillis() + (DamiEssentials.getInstance().getConfig().getLong("ChatCooldown", 0) * 1000));
        }else{
            event.setCancelled(true);
            sendMessageWithPrefix(event.getPlayer(), "&c¡Espera un poco antes de volver a escribir!", "#DE09FF&lC#EF5BF4&lh#FFACE9&la#FFACE9&lt &7» ");
        }
    }
}
