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

public class DeleteWarpCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("warp.admin")){
            sendMessageWithPrefix(sender, "&c¡No tienes permisos!", WarpsManager.prefix);
            return false;
        }
        if(args.length == 0){
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /deletewarp <nombre>", WarpsManager.prefix);
            return false;
        }

        String name = args[0];

        if(WarpsManager.getWarpByName(name).isEmpty()){
            sendMessageWithPrefix(sender, "&cWarp desconocido.", WarpsManager.prefix);
            return false;
        }

        Warp warp = WarpsManager.getWarpByName(name).get();
        WarpsManager.warps.remove(warp);

        sendMessageWithPrefix(sender, "&fEliminaste el warp &e"+name+"&f.", WarpsManager.prefix);
        warp.delete();
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> ret = new ArrayList<>(WarpsManager.warps.stream().map(Warp::getName).toList());
        for (Warp warp : WarpsManager.warps) {
            ret.addAll(warp.getAliases());
        }
        if(args.length == 1)
            return filterSuggestions(ret, args[0]);
        return List.of();
    }
}
