package me.damian.essentials;

import lombok.Getter;
import me.damian.essentials.commands.*;
import me.damian.essentials.commands.privatemessages.IgnoreCommand;
import me.damian.essentials.commands.privatemessages.PrivateMessageCommand;
import me.damian.essentials.commands.privatemessages.ReplyCommand;
import me.damian.essentials.commands.warps.*;
import me.damian.essentials.listeners.ChatListener;
import me.damian.essentials.listeners.WarpsListener;
import me.damian.essentials.managers.WarpsManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamiEssentials extends JavaPlugin {

    @Getter
    private static Economy vaultEconomy;
    @Getter
    private static DamiEssentials instance;

    @Override
    public void onEnable() {
        instance = this;

        setupEconomy();
        saveDefaultConfig();

        registerListeners();
        registerCommands();

        WarpsManager.load(getConfig());
    }

    @SuppressWarnings("DataFlowIssue")
    private void registerCommands() {
        getCommand("addwarplocation").setExecutor(new AddWarpLocationCommand());
        getCommand("addwarplocation").setTabCompleter(new AddWarpLocationCommand());
        getCommand("deletewarp").setExecutor(new DeleteWarpCommand());
        getCommand("deletewarp").setTabCompleter(new DeleteWarpCommand());
        getCommand("createwarp").setExecutor(new CreateWarpCommand());
        getCommand("createwarp").setTabCompleter(new CreateWarpCommand());
        getCommand("setwarpdisplayname").setExecutor(new SetWarpDisplayName());
        getCommand("setwarpdisplayname").setTabCompleter(new SetWarpDisplayName());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setTabCompleter(new WarpCommand());
        getCommand("setwarpprice").setExecutor(new SetWarpPriceCommand());
        getCommand("setwarpprice").setTabCompleter(new SetWarpPriceCommand());
        getCommand("addwarpalias").setExecutor(new AddWarpAliasCommand());
        getCommand("addwarpalias").setTabCompleter(new AddWarpAliasCommand());

        getCommand("flyspeed").setExecutor(new FlySpeedCommand());
        getCommand("flyspeed").setTabCompleter(new FlySpeedCommand());

        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("speed").setTabCompleter(new SpeedCommand());

        getCommand("workbench").setExecutor(new WorkbenchCommand());
        getCommand("workbench").setTabCompleter(new WorkbenchCommand());

        getCommand("fix").setExecutor(new RepairCommand());
        getCommand("fix").setTabCompleter(new RepairCommand());

        getCommand("teleport").setExecutor(new TpCommand());
        getCommand("teleport").setTabCompleter(new TpCommand());

        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("gamemode").setTabCompleter(new GamemodeCommand());

        getCommand("gmc").setExecutor(new GamemodeCreativeCommand());
        getCommand("gmc").setTabCompleter(new GamemodeCreativeCommand());

        getCommand("gms").setExecutor(new GamemodeSurvivalCommand());
        getCommand("gms").setTabCompleter(new GamemodeSurvivalCommand());

        getCommand("gma").setExecutor(new GamemodeAdventureCommand());
        getCommand("gma").setTabCompleter(new GamemodeAdventureCommand());

        getCommand("gmsp").setExecutor(new GamemodeSpectatorCommand());
        getCommand("gmsp").setTabCompleter(new GamemodeSpectatorCommand());

        getCommand("damiessentials").setExecutor(new MainCommand());
        getCommand("damiessentials").setTabCompleter(new MainCommand());

        getCommand("pm").setExecutor(new PrivateMessageCommand());
        getCommand("pm").setTabCompleter(new PrivateMessageCommand());

        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("reply").setTabCompleter(new ReplyCommand());

        getCommand("ignore").setExecutor(new IgnoreCommand());
        getCommand("ignore").setTabCompleter(new IgnoreCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new WarpsListener(), this);
        pm.registerEvents(new ChatListener(), this);
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        vaultEconomy = rsp.getProvider();
    }
}
