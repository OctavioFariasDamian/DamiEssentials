package me.damian.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class GamemodeCreativeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            Bukkit.dispatchCommand(commandSender, "gamemode creative");
        }else{
            if(args.length == 1){
                Bukkit.dispatchCommand(commandSender, "gamemode creative " + args[0]);
            }else{
                sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /gmc [jugador]");
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return filterSuggestions(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList(), args[0]);
        }
        return List.of();
    }
}
