package me.damian.essentials.commands;

import me.damian.essentials.DamiEssentials;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class FeedCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-essentials.feed")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos.");
            return false;
        }
        if (args.length == 0) {
            if (commandSender instanceof Player player) {
                player.setFoodLevel(20);
                sendMessageWithPrefix(commandSender, "&aHas satisfacido tu hambre.");
            } else {
                sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /feel <jugador>");
            }
            return true;
        }

        Player target = org.bukkit.Bukkit.getPlayer(args[0]);

        if(!commandSender.hasPermission("dami-essentials.feed.others")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permiso para satisfacer el hambre a otros jugadores.");
            return false;
        }
        if (target == null || !target.isOnline()) {
            sendMessageWithPrefix(commandSender, "&cJugador desconocido.");
            return false;
        }
        target.setFoodLevel(20);
        sendMessageWithPrefix(commandSender, "&fHas satisfacido el hambre de &e" + target.getName() + "&f.");
        DamiEssentials.sendLog("**"+(commandSender instanceof Player ? commandSender.getName() : "Consola") + "** ha satisfacido el hambre de **"+target.getName()+"**");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return filterSuggestions(commandSender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList(), args[0]);
        }
        return List.of();
    }
}
