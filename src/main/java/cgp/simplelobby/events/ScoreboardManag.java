package cgp.simplelobby.events;

import cgp.simplelobby.SimpleLobby;
import cgp.simplelobby.utils.MessageUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

import java.util.List;

public class ScoreboardManag {

    private SimpleLobby plugin;
    int taskID;
    public ScoreboardManag(SimpleLobby plugin){
        this.plugin = plugin;
    }

    public void createScoreboard(int ticks) {
        FileConfiguration config = plugin.getConfig();
        if (config.getString("Config.scoreboard.enabled").equals("true")) {
            BukkitScheduler schedule = Bukkit.getServer().getScheduler();
            taskID = schedule.scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {

                    FileConfiguration config = plugin.getConfig();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        updateScoreboard(player, config);
                    }

                }
            }, 0, ticks);
        }
    }

    private void updateScoreboard(Player player, FileConfiguration config) {
        if (config.getString("Config.scoreboard.enabled").equals("true")) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = manager.getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("SimpleLobby", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(MessageUtils.getColoredMessage(config.getString("Config.scoreboard.title")));
            List<String> lines = config.getStringList("Config.scoreboard.text");
            for (int i = 0; i < lines.size(); i++) {
                String newline = PlaceholderAPI.setPlaceholders(player, lines.get(i));
                lines.set(i, newline);
                Score score = objective.getScore(MessageUtils.getColoredMessage(lines.get(i)));
                score.setScore(lines.size() - (i));
                player.setScoreboard(scoreboard);

            }
            player.setScoreboard(scoreboard);
        }
    }

}
