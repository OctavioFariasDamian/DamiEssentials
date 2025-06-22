package me.damian.essentials.commands.privatemessages;

import me.damian.essentials.DamiEssentials;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;
import static me.damian.essentials.commands.privatemessages.PrivateMessageCommand.prefix;

public class IgnoreCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)){
            sendMessageWithPrefix(sender, "&cEste comando solo puede ser usado por jugadores.", prefix);
            return false;
        }

        if(args.length == 0){
            sendMessageWithPrefix(sender, "&cÍndica a quien quieres ignorar", prefix);
            return false;
        }

        OfflinePlayer targetPlayer = player.getServer().getOfflinePlayer(args[0]);
        if (!targetPlayer.hasPlayedBefore()) {
            sendMessageWithPrefix(sender, "&cEse jugador nunca jugó en el servidor.", prefix);
            return false;
        }

        List<String> ignoredPlayers = new ArrayList<>(DamiEssentials.getInstance().getConfig().getStringList("IgnoredPlayers." + player.getName()));
        if(ignoredPlayers.contains(targetPlayer.getName())){
            ignoredPlayers.remove(targetPlayer.getName());
            DamiEssentials.getInstance().getConfig().set("IgnoredPlayers." + player.getName(), ignoredPlayers);
            sendMessageWithPrefix(sender, "&fHas dejado de ignorar a &e" + targetPlayer.getName() + "&f.", prefix);
        } else {
            ignoredPlayers.add(targetPlayer.getName());
            DamiEssentials.getInstance().getConfig().set("IgnoredPlayers." + player.getName(), ignoredPlayers);
            sendMessageWithPrefix(sender, "&fAhora estás ignorando a &e" + targetPlayer.getName() + "&f.", prefix);
        }
        DamiEssentials.getInstance().saveConfig();
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
