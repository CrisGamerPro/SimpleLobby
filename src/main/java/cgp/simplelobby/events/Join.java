package cgp.simplelobby.events;

import cgp.simplelobby.SimpleLobby;
import cgp.simplelobby.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class Join implements Listener {

    private SimpleLobby plugin;
    public Join(SimpleLobby plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (config.contains("Config.lobby.x")) {
            double x = Double.valueOf(config.getString("Config.lobby.x"));
            double y = Double.valueOf(config.getString("Config.lobby.y"));
            double z = Double.valueOf(config.getString("Config.lobby.z"));
            float yaw = Float.valueOf(config.getString("Config.lobby.yaw"));
            float pitch = Float.valueOf(config.getString("Config.lobby.pitch"));
            World world = this.plugin.getServer().getWorld(config.getString("Config.lobby.world"));
            Location S = new Location(world, x, y, z, yaw, pitch);
            player.teleport(S);

        }

        event.setJoinMessage(null);
        String enabled = "Config.join-events.join-message.enabled";

        if(config.getString(enabled).equals("true")){
            List<String> message = config.getStringList("Config.join-events.join-message.message-strings");
            for(int i=0;i< message.size();i++){
                String text = message.get(i);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', text.replaceAll("%player%", player.getName())));

            }
        }

        String fwenabled = "Config.join-events.firework.enabled";

        if(config.getString(fwenabled).equals("true")){

            Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
            meta.setPower(1);
            List<Color> colors = new ArrayList<Color>();
            colors.add(Color.BLUE);
            colors.add(Color.LIME);
            meta.addEffect(FireworkEffect.builder().flicker(true).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(colors).build());
            firework.setFireworkMeta(meta);

        }

        String jpmessage = "Config.join-events.on-join-broadcast";
        Bukkit.broadcastMessage(MessageUtils.getColoredMessage(config.getString(jpmessage)).replaceAll("%player%", player.getName()));


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        String qpmessage = "Config.join-events.on-quit-broadcast";
        Bukkit.broadcastMessage(MessageUtils.getColoredMessage(config.getString(qpmessage)).replaceAll("%player%", player.getName()));

    }

}
