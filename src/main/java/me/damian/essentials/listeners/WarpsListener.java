package me.damian.essentials.listeners;

import me.damian.essentials.commands.warps.WarpCommand;
import me.damian.essentials.managers.WarpsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class WarpsListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(e.hasExplicitlyChangedBlock() && WarpCommand.queue.contains(e.getPlayer())){
            sendMessageWithPrefix(e.getPlayer(), "&c¡Te has movido! Teletransporte cancelado.", WarpsManager.prefix);
            WarpCommand.queue.remove(e.getPlayer());
        }
    }
}
