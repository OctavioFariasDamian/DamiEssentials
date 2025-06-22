package me.damian.essentials.commands.warps;

import me.damian.essentials.DamiEssentials;
import me.damian.essentials.managers.WarpsManager;
import me.damian.essentials.model.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class WarpCommand implements TabExecutor {

    public static final List<Player> queue = new ArrayList<>();
    public static final Map<Player, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        if(args.length == 0){
            sendMessageWithPrefix(sender, "&cEl uso del comando es: /warp <nombre>", WarpsManager.prefix);
            return false;
        }

        if(queue.contains((Player) sender)){
            sendMessageWithPrefix(sender, "&cYa estás siendo enviado a un warp.", WarpsManager.prefix);
            return false;
        }

        String name = args[0];

        if(WarpsManager.getWarpByName(name).isEmpty()){
            sendMessageWithPrefix(sender, "&cWarp desconocido.", WarpsManager.prefix);
            return false;
        }

        if(cooldowns.containsKey((Player) sender)){
            if(System.currentTimeMillis() <= cooldowns.get((Player) sender) && !sender.hasPermission("warp.bypass-cooldown")){
                sendMessageWithPrefix(sender, "&cSolo puedes ir a warps cada 3 segundos.", WarpsManager.prefix);
                return false;
            }
            cooldowns.remove((Player) sender);
        }

        Warp warp = WarpsManager.getWarpByName(name).get();

        if(warp.getCost() > 0 && !DamiEssentials.getVaultEconomy().has((Player) sender,warp.getCost())){
            sendMessageWithPrefix(sender, "&c¡No tienes suficiente dinero para entrar a ese warp!", WarpsManager.prefix);
            return false;
        }

        if(warp.getLocations().isEmpty()){
            sendMessageWithPrefix(sender, "&c¡Ese warp no tiene localizaciones actualmente!", WarpsManager.prefix);
            return false;
        }

        Location loc = warp.getLocations().get(new Random().nextInt(warp.getLocations().size()));

        if(!sender.hasPermission("warp.bypass-delay")){
            queue.add((Player) sender);
            sendMessageWithPrefix(sender, "&7Serás telentrasportado en &e3 segundos&7...", WarpsManager.prefix);
            Bukkit.getScheduler().runTaskLater(DamiEssentials.getInstance(), () -> {
                if (!((Player) sender).isOnline()) {
                    WarpCommand.queue.remove((Player) sender);
                    return;
                }
                if (WarpCommand.queue.contains((Player) sender)) {
                    WarpCommand.queue.remove((Player) sender);

                    if (warp.getCost() > 0 && !DamiEssentials.getVaultEconomy().has((Player) sender, warp.getCost())) {
                        sendMessageWithPrefix(sender, "&c¡No tienes suficiente dinero para entrar a ese warp!", WarpsManager.prefix);
                        return;
                    }

                    if (warp.getCost() > 0) {
                        DamiEssentials.getVaultEconomy().withdrawPlayer((Player) sender, warp.getCost());
                    }

                    sendMessageWithPrefix(sender, "&7Teletransportando al warp &e" + warp.getDisplayName() + "&7...", WarpsManager.prefix);
                    ((Player) sender).teleport(loc.clone().add(0, 0.5, 0));

                    if (!sender.hasPermission("warp.bypass-cooldown")) {
                        cooldowns.put((Player) sender, System.currentTimeMillis() + 3000L);
                    }
                }
            }, 60L);
            return false;
        }
        if (warp.getCost() > 0) {
            DamiEssentials.getVaultEconomy().withdrawPlayer((Player) sender, warp.getCost());
        }
        sendMessageWithPrefix(sender, "&7Teletransportando al warp &e" + warp.getDisplayName() + "&7...", WarpsManager.prefix);
        ((Player) sender).teleport(loc.clone().add(0, 0.5, 0));
        if(!sender.hasPermission("warp.bypass-cooldown")) cooldowns.put((Player) sender, System.currentTimeMillis() + 3000L);
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
