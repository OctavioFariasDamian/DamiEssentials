package me.damian.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class WorkbenchCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0) {
            if (sender instanceof org.bukkit.entity.Player player) {
                if (player.hasPermission("dami-essentials.workbench")) {
                    player.openWorkbench(null, true);
                    return true;
                }
                sendMessageWithPrefix(sender, "&cNo tienes permisos.");
                return false;
            } else {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if(target == null || !target.isOnline()) {
            sendMessageWithPrefix(sender, "&cJugador desconocido.");
            return false;
        }

        if (!sender.hasPermission("dami-essentials.workbench.others")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
            return false;
        }

        target.openWorkbench(null, true);
        sendMessageWithPrefix(sender, "&fHas abierto el banco de trabajo para &e" + target.getName() + "&f.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList(), args[0]);
        }
        return List.of();
    }
}
