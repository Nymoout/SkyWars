package com.nymoout.skywars.utilities.placeholders;

import com.nymoout.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import be.maximvdw.placeholderapi.PlaceholderAPI;

public class SWMVdWPlaceholder {

	public SWMVdWPlaceholder(Plugin p) {

		PlaceholderAPI.registerPlaceholder(p, "sw_elo", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getElo();
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_wins", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getWins();
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_losses", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getLosses();
        });

        PlaceholderAPI.registerPlaceholder(p, "sw_kills", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getKills();
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_deaths", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getDeaths();
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_xp", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWars) p).getPlayerStat(player).getXp();
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_games_played", e -> {
            Player player = e.getPlayer();
            return "" + (((SkyWars) p).getPlayerStat(player).getLosses() + ((SkyWars) p).getPlayerStat(player).getWins());
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_kill_death", e -> {
            Player player = e.getPlayer();
            double stat = (double)((SkyWars) p).getPlayerStat(player).getKills()/(double)((SkyWars) p).getPlayerStat(player).getDeaths();
              return String.format("%1$,.2f", stat);
        });
		
		PlaceholderAPI.registerPlaceholder(p, "sw_win_loss", e -> {
            Player player = e.getPlayer();
            double stat = (double)((SkyWars) p).getPlayerStat(player).getWins()/(double)((SkyWars) p).getPlayerStat(player).getLosses();
            return String.format("%1$,.2f", stat);
        });

		PlaceholderAPI.registerPlaceholder(p, "sw_player_name", e ->{
            Player player = e.getPlayer();
		    String playerName = "" + ((SkyWars) p).getPlayerStat(player).getDeaths();
            return playerName;
        });

        PlaceholderAPI.registerPlaceholder(p, "sw_online_players", e ->{
            int online = Bukkit.getServer().getOnlinePlayers().size();
            String onlineString = String.valueOf(online);
            return onlineString;
        });
	}
}
