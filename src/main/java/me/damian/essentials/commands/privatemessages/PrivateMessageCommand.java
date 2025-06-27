package me.damian.essentials.commands.privatemessages;

import me.damian.essentials.DamiEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class PrivateMessageCommand implements TabExecutor {
    
    static final String prefix = "#099BFF&lS#10AFF7&lu#18C3EE&ls#1FD7E6&lu#27EBDD&lr#2EFFD5&lr#2EFFD5&lo &7» &f";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /msg <jugador> <mensaje>", prefix);
            return false;
        }

        Player target = sender.getServer().getPlayer(args[0]);
        
        if (target == null || !target.isOnline()) {
            sendMessageWithPrefix(sender, "&cJugador desconocido.", prefix);
            return false;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        
        String message = sb.toString().trim();

        if (sender instanceof Player player) {
            List<String> ignoredPlayers = new ArrayList<>(DamiEssentials.getInstance().getConfig().getStringList("IgnoredPlayers." + player.getName()));

            if(ignoredPlayers.contains(target.getName())){
                sendMessageWithPrefix(player, "&cNo puedes susurrar a jugadores que ignores.", prefix);
                return false;
            }

            String messageToSender = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Sender"))
                    .replace("%receiver%", target.getName())
                    .replace("%message%", message);
            sendMessageWithPrefix(player, messageToSender, prefix);

            List<String> ignoredPlayers2 = new ArrayList<>(DamiEssentials.getInstance().getConfig().getStringList("IgnoredPlayers." + target.getName()));

            if(!ignoredPlayers2.contains(player.getName())){
                String messageToTarget = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Receiver"))
                        .replace("%sender%", player.getName())
                        .replace("%message%", message);

                sendMessageWithPrefix(target, messageToTarget, prefix);
                String messageToSpy = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Spy"))
                        .replace("%sender%", player.getName())
                        .replace("%receiver%", target.getName())
                        .replace("%message%", message);

                sendMessageWithPrefix(Bukkit.getConsoleSender(), messageToSpy, prefix);
                DamiEssentials.getInstance().getServer().getOnlinePlayers().forEach(player1 ->
                        {
                            if(SocialSpyCommand.socialSpyEnabledPlayers.contains(player1)){
                                if (player1.hasPermission("dami-essentials.spy")) {
                                    if(player1 != player && player1 != target) {
                                        sendMessageWithPrefix(player1, messageToSpy, prefix);
                                    }
                                }else{
                                    sendMessageWithPrefix(player1, "&cNo tienes permisos para ver los mensajes privados.", prefix);
                                    SocialSpyCommand.socialSpyEnabledPlayers.remove(player1);
                                }

                            }
                        });
                target.playSound(target.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
                ReplyCommand.lastPlayerMessages.put(target.getName(), player.getName());
            }

            ReplyCommand.lastPlayerMessages.put(player.getName(), target.getName());
        }else{
            String messageToSpy = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Spy"))
                    .replace("%sender%", "Consola")
                    .replace("%receiver%", target.getName())
                    .replace("%message%", message);

            DamiEssentials.getInstance().getServer().getOnlinePlayers().forEach(player1 ->
            {
                if(SocialSpyCommand.socialSpyEnabledPlayers.contains(player1) ){
                    if (player1.hasPermission("dami-essentials.spy")) {
                        if (player1 != target)
                            sendMessageWithPrefix(player1, messageToSpy, prefix);
                    }else{
                        sendMessageWithPrefix(player1, "&cNo tienes permisos para ver los mensajes privados.", prefix);
                        SocialSpyCommand.socialSpyEnabledPlayers.remove(player1);
                    }

                }
            });
            String messageToSender = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Sender"))
                    .replace("%receiver%", target.getName())
                    .replace("%message%", message);
            sendMessageWithPrefix(sender, messageToSender, prefix);

            String messageToTarget = Objects.requireNonNull(DamiEssentials.getInstance().getConfig().getString("PM-Format.Receiver"))
                    .replace("%sender%", "Consola")
                    .replace("%message%", message);

            sendMessageWithPrefix(target, messageToTarget, prefix);
            target.playSound(target.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
            ReplyCommand.lastPlayerMessages.put(target.getName(), "Consola");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(DamiEssentials.getInstance().getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList(), args[0]);
        }
        return List.of();
    }
}
