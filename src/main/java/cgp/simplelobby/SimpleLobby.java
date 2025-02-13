package cgp.simplelobby;

import cgp.simplelobby.commands.MainCommands;
import cgp.simplelobby.events.Join;
import cgp.simplelobby.events.PlayerEvents;
import cgp.simplelobby.events.ScoreboardManag;
import cgp.simplelobby.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleLobby extends JavaPlugin {

    //Variables
    public static String prefix = "&bSimpleLobby";
    public String version = getDescription().getVersion();
    public String pathConfig;

    //Plugin start logic
    public void onEnable() {
        registerCommands();
        registerEvents();
        registerConfig();
        ScoreboardManag scoreboard = new ScoreboardManag(this);
        scoreboard.createScoreboard(Integer.valueOf(getConfig().getString("Config.scoreboard.ticks")));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+" &fHas been enabled! on version: &e"+version));
    }

    //Plugin shutdown logic
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+" &fHas been disabled! on version: &e"+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&fGoodBye :)"));
    }

    //Register the commands :)
    public void registerCommands(){
        this.getCommand("lobby").setExecutor(new MainCommands(this));
        this.getCommand("setlobby").setExecutor(new MainCommands(this));
        this.getCommand("fly").setExecutor(new MainCommands(this));
    }

    //Register the events
    public void registerEvents(){

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new Join(this),this);
        pm.registerEvents(new PlayerEvents(this), this);

    }

    //Bro the name says what do this :/
    public void registerConfig(){
        File config = new File(this.getDataFolder(), "config.yml");
        this.pathConfig = config.getPath();
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
    }
}
