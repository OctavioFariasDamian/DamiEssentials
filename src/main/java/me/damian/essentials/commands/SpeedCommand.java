package me.damian.essentials.commands;

import me.damian.essentials.DamiEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class SpeedCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)){
            sendMessageWithPrefix(sender, "&cEste comando solo puede ser usado por jugadores.");
            return false;
        }

        if (!sender.hasPermission("dami-essentials.speed")) {
            sendMessageWithPrefix(sender, "&cNo tienes permisos.");
            return false;
        }

        if (args.length != 1) {
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /speed <velocidad>");
            return false;
        }

        float speed;
        try {
            speed = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            sendMessageWithPrefix(sender, "&cLa velocidad debe ser un número válido.");
            return false;
        }

        if (speed < 0) {
            sendMessageWithPrefix(sender, "&cLa velocidad debe ser un número positivo.");
            return false;
        }

        if(speed > 10){
            sendMessageWithPrefix(sender, "&cEl máximo de velocidad es de 10.0");
            return false;
        }

        if (player.isFlying()) {
            player.setFlySpeed(speed / 10);
            sendMessageWithPrefix(sender, "&fHas cambiado la velocidad de vuelo a &e" + speed + "&f.");
            DamiEssentials.sendLog("**"+player.getName() + "** ha cambiado su velocidad de vuelo a **" + speed + "**.");
        } else {
            player.setWalkSpeed(speed / 10);
            sendMessageWithPrefix(sender, "&fHas cambiado la velocidad de caminata a &e" + speed + "&f.");
            DamiEssentials.sendLog("**"+player.getName() + "** ha cambiado su velocidad de caminata a **" + speed + "**.");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
