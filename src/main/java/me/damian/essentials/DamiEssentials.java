package me.damian.essentials;

import com.supreme.bot.SupremeBot;
import lombok.Getter;
import me.damian.essentials.commands.*;
import me.damian.essentials.commands.privatemessages.IgnoreCommand;
import me.damian.essentials.commands.privatemessages.PrivateMessageCommand;
import me.damian.essentials.commands.privatemessages.ReplyCommand;
import me.damian.essentials.commands.privatemessages.SocialSpyCommand;
import me.damian.essentials.commands.warps.*;
import me.damian.essentials.listeners.ChatListener;
import me.damian.essentials.listeners.WarpsListener;
import me.damian.essentials.managers.WarpsManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public final class DamiEssentials extends JavaPlugin {

    @Getter
    private static Economy vaultEconomy;
    @Getter
    private static DamiEssentials instance;
    private TextChannel staffActivityLogs;

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

        getCommand("lightning").setExecutor(new LightningCommand());
        getCommand("lightning").setTabCompleter(new LightningCommand());

        getCommand("damiessentials").setExecutor(new MainCommand());
        getCommand("damiessentials").setTabCompleter(new MainCommand());

        getCommand("pm").setExecutor(new PrivateMessageCommand());
        getCommand("pm").setTabCompleter(new PrivateMessageCommand());

        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("reply").setTabCompleter(new ReplyCommand());

        getCommand("ignore").setExecutor(new IgnoreCommand());
        getCommand("ignore").setTabCompleter(new IgnoreCommand());

        getCommand("heal").setExecutor(new HealCommand());
        getCommand("heal").setTabCompleter(new HealCommand());

        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("feed").setTabCompleter(new FeedCommand());

        getCommand("anvil").setExecutor(new AnvilCommand());
        getCommand("anvil").setTabCompleter(new AnvilCommand());

        getCommand("socialspy").setExecutor(new SocialSpyCommand());
        getCommand("socialspy").setTabCompleter(new SocialSpyCommand());

        staffActivityLogs = SupremeBot.getJda().getTextChannelById(1361900904554696825L);
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

    public static void sendLog(String s){
        if (instance.staffActivityLogs != null) {
            instance.staffActivityLogs
                    .sendMessageEmbeds(new EmbedBuilder()
                            .setColor(0xff0000)
                            .setDescription(s)
                            .setTimestamp(Instant.now()).build()).queue();
        } else {
            instance.getLogger().warning("Staff activity logs channel is not set. Please check the configuration.");
        }
    }
}
