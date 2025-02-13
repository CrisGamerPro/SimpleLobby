package cgp.simplelobby.commands;

import cgp.simplelobby.SimpleLobby;
import cgp.simplelobby.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainCommands implements CommandExecutor {

    private SimpleLobby plugin;

    public MainCommands(SimpleLobby plugin) {
        this.plugin = plugin;
    }


    //A loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooot of code :)
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCant execute that command on thr console!"));
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("lobby")) {

                if (args.length>= 1) {

                    if (args[0].equalsIgnoreCase("get")) {

                        subCommandGetCommand(sender, args);

                    }else if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        FileConfiguration config = plugin.getConfig();
                        String prefix = "Config.prefix";
                        String reload = "Config.messages.reload-config";
                        sender.sendMessage(MessageUtils.getColoredMessage(config.getString(reload).replaceAll("%prefix%", config.getString(prefix))));
                    }

                } else {

                    FileConfiguration config = plugin.getConfig();


                    if (config.contains("Config.lobby.x")) {
                        double x = Double.valueOf(config.getString("Config.lobby.x"));
                        double y = Double.valueOf(config.getString("Config.lobby.y"));
                        double z = Double.valueOf(config.getString("Config.lobby.z"));
                        float yaw = Float.valueOf(config.getString("Config.lobby.yaw"));
                        float pitch = Float.valueOf(config.getString("Config.lobby.pitch"));
                        World world = plugin.getServer().getWorld(config.getString("Config.lobby.world"));
                        Location S = new Location(world, x, y, z, yaw, pitch);
                        player.teleport(S);
                        String text = "Config.messages.teleport-message";
                        String prefix = "Config.prefix";
                        sender.sendMessage(MessageUtils.getColoredMessage(config.getString(text).replaceAll("%prefix%", config.getString(prefix))));

                        return true;
                    } else {
                        String error = "Config.messages.teleport-error";
                        String prefix = "Config.prefix";
                        sender.sendMessage(MessageUtils.getColoredMessage(config.getString(error).replaceAll("%prefix%", config.getString(prefix))));
                        return true;
                    }
                }
            }

            if (cmd.getName().equalsIgnoreCase("setlobby")) {


                if (!sender.hasPermission("simplelobby.commands.set")) {
                    FileConfiguration config = plugin.getConfig();
                    String noperm = "Config.messages.no-permission";
                    String prefix = "Config.prefix";
                    sender.sendMessage(MessageUtils.getColoredMessage(config.getString(noperm).replaceAll("%prefix%", config.getString(prefix))));
                    return true;
                }

                Location S = player.getLocation();
                double x = S.getX();
                double y = S.getY();
                double z = S.getZ();
                String world = S.getWorld().getName();
                float yaw = S.getYaw();
                float pitch = S.getPitch();
                FileConfiguration config = plugin.getConfig();
                config.set("Config.lobby.x", x);
                config.set("Config.lobby.y", y);
                config.set("Config.lobby.z", z);
                config.set("Config.lobby.world", world);
                config.set("Config.lobby.yaw", yaw);
                config.set("Config.lobby.pitch", pitch);
                plugin.saveConfig();
                String prefix = "Config.prefix";
                String setlobby = "Config.messages.set-lobby";
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(setlobby).replaceAll("%prefix%", config.getString(prefix))));

                return true;

            }

            if(cmd.getName().equalsIgnoreCase("fly")){
                FileConfiguration config = plugin.getConfig();
                if(player.getAllowFlight()){
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    String prefix = "Config.prefix";
                    String flyoff = "Config.messages.fly-off";
                    sender.sendMessage(MessageUtils.getColoredMessage(config.getString(flyoff).replaceAll("%prefix%", config.getString(prefix))));
                    return true;
                }
                if(!player.getAllowFlight()){
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    String prefix = "Config.prefix";
                    String flyon = "Config.messages.fly-on";
                    sender.sendMessage(MessageUtils.getColoredMessage(config.getString(flyon).replaceAll("%prefix%", config.getString(prefix))));
                    return true;
                }
            }

        }
        return false;
    }

    public void help(CommandSender sender){

        if(!sender.hasPermission("simplelobby.commands.admin-help")){
            sender.sendMessage(" ");
        }else {

            sender.sendMessage(MessageUtils.getColoredMessage("&b&m----------------------Help----------------------"));
            sender.sendMessage(MessageUtils.getColoredMessage("&f/lobby [sends you to the lobby]"));
            sender.sendMessage(MessageUtils.getColoredMessage("&f/setlobby [sets the lobby]"));
            sender.sendMessage(MessageUtils.getColoredMessage("&b&m----------------------Args----------------------"));
            sender.sendMessage(MessageUtils.getColoredMessage("&f/lobby get <author/version> [get the versio/author of the plugin]"));
            sender.sendMessage(MessageUtils.getColoredMessage("&f/lobby reload [reloads the config of the plugin]"));
            sender.sendMessage(MessageUtils.getColoredMessage("&b&m------------------------------------------------"));

        }
    }

    public void subCommandGetCommand(CommandSender sender,String[] args) {

        if (sender.hasPermission("simplelobby.commands.get")) {

            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("version")) {
                    sender.sendMessage(MessageUtils.getColoredMessage("&fThe version of the plugin is: &e" + plugin.getDescription().getVersion()));
                } else {
                    if (args[1].equalsIgnoreCase("author")) {
                        sender.sendMessage(MessageUtils.getColoredMessage("&fThe author of the plugin is: &e" + plugin.getDescription().getAuthors()));
                    } else {
                        help(sender);
                    }
                }
            }else{

            }

        }else{

            FileConfiguration config = plugin.getConfig();
            String noperm = "Config.messages.no-permission";
            String prefix = "Config.prefix";
            sender.sendMessage(MessageUtils.getColoredMessage(config.getString(noperm).replaceAll("%prefix%", config.getString(prefix))));

        }
    }
}
