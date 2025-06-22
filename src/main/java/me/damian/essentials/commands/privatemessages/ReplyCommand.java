package me.damian.essentials.commands.privatemessages;

import me.damian.essentials.DamiEssentials;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;
import static me.damian.essentials.commands.privatemessages.PrivateMessageCommand.prefix;

public class ReplyCommand implements TabExecutor {

    static final Map<String, String> lastPlayerMessages = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /r <mensaje>", prefix);
            return false;
        }
        if (!(sender instanceof Player player)) {
            sendMessageWithPrefix(sender, "&c¡Este comando solo puede ser usado por jugadores!", prefix);
            return false;
        }

        if(!lastPlayerMessages.containsKey(player.getName())) {
            sendMessageWithPrefix(sender, "&cNo tienes mensajes recientes para responder.", prefix);
            return false;
        }

        String targetPlayerName = lastPlayerMessages.get(player.getName());
        Player targetPlayer = player.getServer().getPlayer(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sendMessageWithPrefix(sender, "&cJugador desconectado.", prefix);
            return false;
        }

        String message = String.join(" ", args);

        List<String> ignoredPlayers = new ArrayList<>(DamiEssentials.getInstance().getConfig().getStringList("IgnoredPlayers." + player.getName()));

        if(ignoredPlayers.contains(targetPlayer.getName())){
            sendMessageWithPrefix(player, "&cNo puedes susurrar a jugadores que ignores.", prefix);
            return false;
        }

        String messageToSender = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Sender"))
                .replace("%receiver%", targetPlayer.getName())
                .replace("%message%", message);
        sendMessageWithPrefix(player, messageToSender, prefix);

        List<String> ignoredPlayers2 = new ArrayList<>(DamiEssentials.getInstance().getConfig().getStringList("IgnoredPlayers." + targetPlayer.getName()));

        if(!ignoredPlayers2.contains(player.getName())){
            String messageToTarget = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Receiver"))
                    .replace("%sender%", player.getName())
                    .replace("%message%", message);

            sendMessageWithPrefix(targetPlayer, messageToTarget, prefix);
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
            ReplyCommand.lastPlayerMessages.put(targetPlayer.getName(), player.getName());
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
