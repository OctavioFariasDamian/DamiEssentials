package me.damian.essentials.commands;

import me.damian.essentials.DamiEssentials;
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

public class LightningCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("dami-essentials.lightning")){
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
            return false;
        }

        if(args.length == 0) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /shock <jugador>");
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null || !player.isOnline()){
            sendMessageWithPrefix(sender, "&cJugador desconocido.");
            return false;
        }

        player.getWorld().strikeLightning(player.getLocation());
        sendMessageWithPrefix(sender, "&fLe cayó un rayo a &e"+player.getName()+"&f. UWU");
        sendMessageWithPrefix(player, "&cTe ha caído un rayo.");
        DamiEssentials.sendLog("**"+(sender instanceof Player ? sender.getName() : "Consola") + "** ha lazado un rayo sobre **"+player.getName()+"**");
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
