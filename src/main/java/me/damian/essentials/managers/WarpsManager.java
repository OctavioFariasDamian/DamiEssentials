package me.damian.essentials.managers;

import me.damian.essentials.model.Warp;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WarpsManager {

    public static final List<Warp> warps = new ArrayList<>();
    public static final String prefix = "#BF29EE&lL#BF51EA&lu#C079E5&lg#C0A1E1&la#C0A1E1&lr#C0A1E1&le#C0A1E1&ls &7» &f";

    public static void load(FileConfiguration config){
        warps.clear();
        for(String key : Objects.requireNonNull(config.getConfigurationSection("Warps")).getKeys(false)){
            warps.add(Warp.fromSection(Objects.requireNonNull(config.getConfigurationSection("Warps." + key))));
        }
    }

    public static Optional<Warp> getWarpByName(String name) {
        return warps.stream()
                .filter(warp -> warp.getName().equalsIgnoreCase(name) ||
                        warp.getAliases().stream()
                                .anyMatch(alias -> alias.equalsIgnoreCase(name)))
                .findAny();
    }

}
