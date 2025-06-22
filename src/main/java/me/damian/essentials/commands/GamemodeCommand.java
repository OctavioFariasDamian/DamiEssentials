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

public class GamemodeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /gamemode <modo> [jugador]");
            return false;
        }
        if (args.length == 1) {
            if(!(sender instanceof Player player)) {
                sendMessageWithPrefix(sender, "&cEste comando solo puede ser usado por jugadores.");
                return false;
            }
            if (!sender.hasPermission("dami-essentials.gamemode")) {
                sendMessageWithPrefix(sender, "&cNo tienes permisos.");
                return false;
            }
            String mode = args[0].toLowerCase();
            switch (mode) {
                case "survival", "s" -> player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                case "creative", "c" -> player.setGameMode(org.bukkit.GameMode.CREATIVE);
                case "adventure", "a" -> player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                case "spectator", "sp" -> player.setGameMode(org.bukkit.GameMode.SPECTATOR);
                default -> {
                    sendMessageWithPrefix(sender, "&cModo de juego desconocido. Usa: survival, creative, adventure o spectator.");
                    return false;
                }
            }
            String gamemode = switch (mode) {
                case "survival", "s" -> "Supervivencia";
                case "creative", "c" -> "Creativo";
                case "adventure", "a" -> "Aventura";
                case "spectator", "sp" -> "Espectador";
                default -> "";
            };
            sendMessageWithPrefix(sender, "&fHas cambiado tu modo de juego a &e" + gamemode + "&f.");
            return true;
        }
        if (!sender.hasPermission("dami-essentials.gamemode.others")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
            return false;
        }

        Player target = sender.getServer().getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            sendMessageWithPrefix(sender, "&cJugador desconocido.");
            return false;
        }

        String mode = args[0].toLowerCase();
        switch (mode) {
            case "survival", "s" -> target.setGameMode(org.bukkit.GameMode.SURVIVAL);
            case "creative", "c" -> target.setGameMode(org.bukkit.GameMode.CREATIVE);
            case "adventure", "a" -> target.setGameMode(org.bukkit.GameMode.ADVENTURE);
            case "spectator", "sp" -> target.setGameMode(org.bukkit.GameMode.SPECTATOR);
            default -> {
                sendMessageWithPrefix(sender, "&cModo de juego desconocido. Usa: survival, creative, adventure o spectator.");
                return false;
            }
        }

        String gamemode = switch (mode) {
            case "survival", "s" -> "Supervivencia";
            case "creative", "c" -> "Creativo";
            case "adventure", "a" -> "Aventura";
            case "spectator", "sp" -> "Espectador";
            default -> "";
        };

        sendMessageWithPrefix(sender, "&fHas cambiado el modo de juego de &e" + target.getName() + " &f a &e" + gamemode + "&f.");
        sendMessageWithPrefix(target, "&fTu modo de juego ha sido cambiado a &e" + gamemode + "&f por &e" + sender.getName() + "&f.");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("survival", "s", "creative", "c", "adventure", "a", "spectator", "sp");
        } else if (args.length == 2 && sender.hasPermission("dami-essentials.gamemode.others")) {
            return filterSuggestions(sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList(), args[1]);
        }

        return List.of();
    }
}
