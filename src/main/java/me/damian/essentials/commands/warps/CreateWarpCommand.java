package me.damian.essentials.commands.warps;

import me.damian.essentials.managers.WarpsManager;
import me.damian.essentials.model.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class CreateWarpCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("warp.admin")){
            sendMessageWithPrefix(sender, "&c¡No tienes permisos!", WarpsManager.prefix);
            return false;
        }
        if(args.length == 0){
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /createwarp <nombre>", WarpsManager.prefix);
            return false;
        }

        String name = args[0];

        if(WarpsManager.getWarpByName(name).isPresent()){
            sendMessageWithPrefix(sender, "&c¡Ya existe un warp llamado así!", WarpsManager.prefix);
            return false;
        }

        Warp warp = new Warp(name, name, new ArrayList<>(), new ArrayList<>(), 0);

        if(sender instanceof Player)
            warp.getLocations().add(((Player) sender).getLocation());

        sendMessageWithPrefix(sender, "&fHas creado el warp &e"+name+"&f.", WarpsManager.prefix);
        warp.save();
        WarpsManager.warps.add(warp);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
