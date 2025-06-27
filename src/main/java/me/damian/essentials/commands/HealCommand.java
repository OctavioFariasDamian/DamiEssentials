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

public class HealCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-essentials.heal")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos.");
            return false;
        }
        if (args.length == 0) {
            if (commandSender instanceof Player player) {
                player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue());
                player.setFoodLevel(20);
                sendMessageWithPrefix(commandSender, "&aTe has curado completamente.");
            } else {
                sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /heal <jugador>");
            }
            return true;
        }

        Player target = org.bukkit.Bukkit.getPlayer(args[0]);

        if(!commandSender.hasPermission("dami-essentials.heal.others")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permiso para curar a otros jugadores.");
            return false;
        }
        if (target == null || !target.isOnline()) {
            sendMessageWithPrefix(commandSender, "&cJugador desconocido o no está en línea.");
            return false;
        }
        target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.MAX_HEALTH)).getValue());
        target.setFoodLevel(20);
        sendMessageWithPrefix(commandSender, "&fHas curado a &e" + target.getName() + "&f.");
        DamiEssentials.sendLog("**"+(commandSender instanceof Player ? commandSender.getName() : "Consola") + "** ha curado a **"+target.getName()+"**");
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
