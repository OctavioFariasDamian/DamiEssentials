package me.damian.essentials.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.damian.essentials.DamiEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

import static me.damian.core.DamiUtils.locationToString;

@Data
@AllArgsConstructor
public class Warp {
    private final String name;
    private String displayName;
    private final List<Location> locations;
    private final List<String> aliases;
    private double cost;

    public static Warp fromSection(ConfigurationSection section){
        List<Location> locations = new ArrayList<>();
        for (String s : section.getStringList("locations")) {
            locations.add(
                    new Location(
                            Bukkit.getWorld(s.split(";")[5]),
                            Double.parseDouble(s.split(";")[0]),
                            Double.parseDouble(s.split(";")[1]),
                            Double.parseDouble(s.split(";")[2]),
                            Float.parseFloat(s.split(";")[3]),
                            Float.parseFloat(s.split(";")[4])
                    )
            );
        }
        List<String> aliases = new ArrayList<>(section.getStringList("aliases"));

        double cost = section.getDouble("cost", 0);
        String displayName = section.getString("displayName", section.getName());
        return new Warp(section.getName(), displayName, locations, aliases, cost);
    }

    public void save(){
        DamiEssentials.getInstance().getConfig().set("Warps."+name+".displayName", displayName);
        List<String> loc = new ArrayList<>();
        for (Location location : locations) {
            loc.add(locationToString(location));
        }
        DamiEssentials.getInstance().getConfig().set("Warps."+name+".aliases", aliases);
        DamiEssentials.getInstance().getConfig().set("Warps."+name+".locations", loc);
        DamiEssentials.getInstance().getConfig().set("Warps."+name+".cost", cost);
        DamiEssentials.getInstance().saveConfig();
    }

    public void delete() {
        DamiEssentials.getInstance().getConfig().set("Warps."+name, null);
        DamiEssentials.getInstance().saveConfig();
    }
}
