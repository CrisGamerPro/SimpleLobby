package cgp.simplelobby.events;

import cgp.simplelobby.SimpleLobby;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PlayerEvents implements Listener {

    private SimpleLobby plugin;
    public PlayerEvents(SimpleLobby plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        List<String> worlds = config.getStringList("Config.lobby-blocked-events.enabled-worlds");
        String world = player.getWorld().getName();
        if (config.getString("Config.lobby-blocked-events.block-protection-enabled").equals("true")){
            if(player.hasPermission("simplelobby.bypass.break-place-blocks")) {
                for (int i = 0; i < worlds.size(); i++) {
                    if (worlds.get(i).equals(world)){

                        event.setCancelled(true);
                        String cbreak = "Config.messages.cant-break";
                        String prefix = "Config.prefix";
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(cbreak).replaceAll("%prefix%", config.getString(prefix))));
                        return;

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        List<String> worlds = config.getStringList("Config.lobby-blocked-events.enabled-worlds");
        String world = player.getWorld().getName();
        if (config.getString("Config.lobby-blocked-events.block-protection-enabled").equals("true")) {
            if (player.hasPermission("simplelobby.bypass.break-place-blocks")) {
                for (int i = 0; i < worlds.size(); i++) {
                    if (worlds.get(i).equals(world)){

                        event.setCancelled(true);
                        String cplace = "Config.messages.cant-place";
                        String prefix = "Config.prefix";
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(cplace).replaceAll("%prefix%", config.getString(prefix))));
                        return;

                    }
                }
            }
        }
    }
    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event){

        Entity entity = event.getDamager();
        Entity damagedEntity = event.getEntity();
        if(entity.getType().equals(EntityType.PLAYER) && damagedEntity.getType().equals(EntityType.PLAYER)) {

            FileConfiguration config = plugin.getConfig();
            Player player = (Player) entity;
            List<String> worlds = config.getStringList("Config.lobby-blocked-events.enabled-worlds");
            String world = player.getWorld().getName();
            String pvpenabled = "Config.lobby-blocked-events.pvp-enabled";

            if (config.getString(pvpenabled).equals("false")) {
                if (!player.hasPermission("simplelobby.bypass.pvp")) {

                    for (int i = 0; i < worlds.size(); i++) {
                        if (worlds.get(i).equals(world)) {

                            event.setCancelled(true);
                            String cpvp = "Config.messages.no-pvp";
                            String prefix = "Config.prefix";
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString(cpvp).replaceAll("%prefix%", config.getString(prefix))));
                            return;

                        }
                    }
                }
            }
        }

    }

}
