package com.nymoout.skywars.utilities;

import com.nymoout.skywars.SkyWars;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SWRPlaceholders extends EZPlaceholderHook {

    private SkyWars sw;

    public SWRPlaceholders(Plugin plugin) {
        super(plugin, "swr");
        this.sw = (SkyWars) plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("elo")) {
            return "" + sw.getPlayerStat(player).getElo();
        }

        if (identifier.equals("wins")) {
            return "" + sw.getPlayerStat(player).getWins();
        }

        if (identifier.equals("losses")) {
            return "" + sw.getPlayerStat(player).getLosses();
        }

        if (identifier.equals("kills")) {
            return "" + sw.getPlayerStat(player).getKills();
        }

        if (identifier.equals("deaths")) {
            return "" + sw.getPlayerStat(player).getDeaths();
        }

        if (identifier.equals("xp")) {
            return "" + sw.getPlayerStat(player).getXp();
        }

        if (identifier.equals("games_played")) {
            return "" + (sw.getPlayerStat(player).getLosses() + sw.getPlayerStat(player).getWins());
        }

        if (identifier.equals("kill_death")) {
            double stat = (double) ((double) sw.getPlayerStat(player).getKills() / (double) sw.getPlayerStat(player).getDeaths());
            String statString = String.format("%1$,.2f", stat);
            return statString;
        }

        if (identifier.equals("win_loss")) {
            double stat = (double) ((double) sw.getPlayerStat(player).getWins() / (double) sw.getPlayerStat(player).getLosses());
            String statString = String.format("%1$,.2f", stat);
            return statString;

        }
        if (identifier.equals("player_name")) {
            return "" + (sw.getPlayerStat(player).getPlayerName());
        }
        if (identifier.equals("online_players")) {
            int online = Bukkit.getServer().getOnlinePlayers().size();
            String onlineString = String.valueOf(online);
            return onlineString;
        }

        return null;
    }
}
