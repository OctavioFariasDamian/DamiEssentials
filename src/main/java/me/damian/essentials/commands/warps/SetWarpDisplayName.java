package me.damian.essentials.commands.warps;

import me.damian.essentials.managers.WarpsManager;
import me.damian.essentials.model.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;


public class SetWarpDisplayName implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("warp.admin")){
            sendMessageWithPrefix(sender, "&c¡No tienes permisos!", WarpsManager.prefix);
            return false;
        }
        if(args.length < 2){
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /setwarpdisplayname <nombre> <nombreVisible>", WarpsManager.prefix);
            return false;
        }

        String name = args[0];

        if(WarpsManager.getWarpByName(name).isEmpty()){
            sendMessageWithPrefix(sender, "&cWarp desconocido.", WarpsManager.prefix);
            return false;
        }

        StringBuilder displayName = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            displayName.append(args[i]).append(" ");
        }

        displayName = new StringBuilder(displayName.toString().trim());

        Warp warp = WarpsManager.getWarpByName(name).get();
        warp.setDisplayName(displayName.toString());

        sendMessageWithPrefix(sender, "&fCambiaste el nombre visible del warp &e"+name+"&f a '"+displayName+"'", WarpsManager.prefix);
        warp.save();
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> ret = new ArrayList<>(WarpsManager.warps.stream().map(Warp::getName).toList());
        for (Warp warp : WarpsManager.warps) {
            ret.addAll(warp.getAliases());
        }
        if(args.length == 1)
            return filterSuggestions(ret, args[0]);
        return List.of();
    }
}
