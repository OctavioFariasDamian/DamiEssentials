package me.damian.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.damian.core.DamiUtils.filterSuggestions;
import static me.damian.core.DamiUtils.sendMessageWithPrefix;

public class RepairCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0) {
            if (sender instanceof org.bukkit.entity.Player player) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if(repairItem(itemInHand)) {
                    sendMessageWithPrefix(sender, "&fEl item de tu mano ha sido reparado.");
                } else {
                    sendMessageWithPrefix(sender, "&cEl item de tu mano no es reparable.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        switch (args[0].toLowerCase()){
            case "todo":
            case "all": {
                boolean repaired = false;
                for (ItemStack item : player.getInventory().getContents()) {
                    if (repairItem(item)) {
                        repaired = true;
                    }
                }

                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (repairItem(item)) {
                        repaired = true;
                    }
                }

                if (repairItem(player.getInventory().getItemInOffHand())) {
                    repaired = true;
                }

                if (repaired) {
                    sendMessageWithPrefix(sender, "&fTodos los items en tu inventario han sido reparados.");
                } else {
                    sendMessageWithPrefix(sender, "&cNo hay items reparables en tu inventario.");
                }

                return true;
            }
            case "casco":
            case "helmet":  {
                ItemStack helmet = player.getInventory().getHelmet();
                if (repairItem(helmet)) {
                    sendMessageWithPrefix(sender, "&fTu casco ha sido reparado.");
                } else {
                    sendMessageWithPrefix(sender, "&cTu casco no es reparable.");
                }
                return true;
            }
            case "pechera":
            case  "chestplate": {
                ItemStack chestplate = player.getInventory().getChestplate();
                if (repairItem(chestplate)) {
                    sendMessageWithPrefix(sender, "&fTu pechera ha sido reparada.");
                } else {
                    sendMessageWithPrefix(sender, "&cTu pechera no es reparable.");
                }
                return true;
            }
            case "pantalones":
            case "leggings": {
                ItemStack leggings = player.getInventory().getLeggings();
                if (repairItem(leggings)) {
                    sendMessageWithPrefix(sender, "&fTus pantalones han sido reparados.");
                } else {
                    sendMessageWithPrefix(sender, "&cTus pantalones no son reparables.");
                }
                return true;
            }
            case "botas":
            case "boots": {
                ItemStack boots = player.getInventory().getBoots();
                if (repairItem(boots)) {
                    sendMessageWithPrefix(sender, "&fTus botas han sido reparadas.");
                } else {
                    sendMessageWithPrefix(sender, "&cTus botas no son reparables.");
                }
                return true;
            }
            case "mano":
            case "hand": {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (repairItem(itemInHand)) {
                    sendMessageWithPrefix(sender, "&fEl item de tu mano ha sido reparado.");
                } else {
                    sendMessageWithPrefix(sender, "&cEl item de tu mano no es reparable.");
                }
                return true;
            }
            case "offhand": {
                ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
                if (repairItem(itemInOffHand)) {
                    sendMessageWithPrefix(sender, "&fEl item de tu mano secundaria ha sido reparado.");
                } else {
                    sendMessageWithPrefix(sender, "&cEl item de tu mano secundaria no es reparable.");
                }
                return true;
            }
            default:
                sendMessageWithPrefix(sender, "&cEl uso del comando es: /repair <todo|all|casco|helmet|pechera|chestplate|pantalones|leggings|botas|boots|mano|hand|offhand>");
                return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return filterSuggestions(List.of("todo", "all", "casco", "helmet", "pechera", "chestplate",
                    "pantalones", "leggings", "botas", "boots", "mano", "hand", "offhand"), args[0]);
        }
        return List.of();
    }

    private boolean repairItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable) {
            if (damageable.hasDamage()) {
                damageable.setDamage(0);
                item.setItemMeta(meta);
                return true;
            }
            return true;
        }
        return false;
    }
}
