package me.damian.essentials.commands;

import me.damian.essentials.DamiEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class TpCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /tp <jugador1> [jugador2]");
            return false;
        }
        if(args.length == 1){
            if(!(sender instanceof Player player)){
                sendMessageWithPrefix(sender, "&cEste comando solo puede ser usado por jugadores.");
                return false;
            }
            if(!sender.hasPermission("dami-essentials.tp")){
                sendMessageWithPrefix(sender, "&cNo tienes permisos.");
                return false;
            }

            Player target = player.getServer().getPlayer(args[0]);
            if(target == null || !target.isOnline()){
                sendMessageWithPrefix(sender, "&cJugador desconocido.");
                return false;
            }

            player.teleport(target.getLocation());
            sendMessageWithPrefix(sender, "&fTe has teletransportado a &e" + target.getName() + "&f.");
            DamiEssentials.sendLog("**"+player + "** se teletransportó hacia **"+player.getName()+"**");
            return true;
        }

        if(!sender.hasPermission("dami-essentials.tp.others")){
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
            return false;
        }
        Player target1 = sender.getServer().getPlayer(args[0]);
        Player target2 = sender.getServer().getPlayer(args[1]);
        if(target1 == null || !target1.isOnline()){
            sendMessageWithPrefix(sender, "&cJugador 1 desconocido.");
            return false;
        }
        if(target2 == null || !target2.isOnline()){
            sendMessageWithPrefix(sender, "&cJugador 2 desconocido.");
            return false;
        }

        target1.teleport(target2.getLocation());
        sendMessageWithPrefix(sender, "&fHas teletransportado a &e" + target1.getName() + " &f a &e" + target2.getName() + "&f.");
        DamiEssentials.sendLog("**"+(sender instanceof Player ? sender.getName() : "Consola") + "** hizo que **"+target1+"** se teletransporte hacia **"+target2.getName()+"**");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList(), args[0]);
        } else if (args.length == 2 && sender.hasPermission("dami-essentials.tp.others")) {
            return filterSuggestions(sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList(), args[1]);
        }
        return List.of();
    }
}
