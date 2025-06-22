package me.damian.essentials.commands;

import me.damian.essentials.DamiEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class MainCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-essentials.command")) {
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos.");
            return false;
        }

        if (args.length == 0) {
            sendMessageWithPrefix(commandSender, "&aComandos disponibles:");
            sendMessageWithPrefix(commandSender, "&7/damiessentials reload &8- &fRecarga la configuración del plugin.");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            if(commandSender.hasPermission("dami-essentials.command.reload")) {
                long startTime = System.currentTimeMillis();
                DamiEssentials.getInstance().reloadConfig();
                long endTime = System.currentTimeMillis();
                sendMessageWithPrefix(commandSender, "&fConfiguración recargada en &e"+(endTime - startTime) + "ms&f.");
            } else {
                sendMessageWithPrefix(commandSender, "&cNo tienes permisos.");
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(commandSender.hasPermission("dami-essentials.command")) {
            if (args.length == 1) {
                return filterSuggestions(List.of("reload"), args[0]);
            }
        }
        return List.of();
    }
}
