package me.damian.essentials.commands.privatemessages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class SocialSpyCommand implements TabExecutor {

    public static final List<Player> socialSpyEnabledPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(sender.hasPermission("dami-essentials.socialspy")) {
            if (sender instanceof Player player) {
                if (socialSpyEnabledPlayers.contains(player)) {
                    socialSpyEnabledPlayers.remove(player);
                    sendMessageWithPrefix(sender, "&cSocial Spy deshabilitado.");
                } else {
                    socialSpyEnabledPlayers.add(player);
                    sendMessageWithPrefix(sender, "&aSocial Spy habilitado. Ahora verás los mensajes privados de otros jugadores.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }

        } else {
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
