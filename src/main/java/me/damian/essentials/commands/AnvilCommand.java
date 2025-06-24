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

public class AnvilCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("dami-essentials.anvil")){
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos.");
            return false;
        }

        if(args.length == 0) {
            if (!(commandSender instanceof Player player)) {
                sendMessageWithPrefix(commandSender, "&cEl uso del comando es: /anvil <jugador>");
                return false;
            }

            player.openAnvil(null, true);
            return true;
        }
        if(!commandSender.hasPermission("dami-essentials.anvil.others")){
            sendMessageWithPrefix(commandSender, "&cNo tienes permisos de abrirle un yunque a otro jugador, solo a tí.");
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);

        if(player == null || !player.isOnline()){
            sendMessageWithPrefix(commandSender, "&cJugador desconocido.");
            return false;
        }

        player.openAnvil(null, true);
        sendMessageWithPrefix(player, "&fUn &cadministrador &fte abrió un yunque.");
        sendMessageWithPrefix(commandSender, "&fAbriste un yunque a &e"+player.getName()+"&f.");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return filterSuggestions(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[0]);
        }
        return List.of();
    }
}
