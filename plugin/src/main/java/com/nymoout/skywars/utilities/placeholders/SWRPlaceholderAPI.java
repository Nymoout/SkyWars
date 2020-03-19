package com.nymoout.skywars.utilities.placeholders;

import com.nymoout.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class SWRPlaceholderAPI extends EZPlaceholderHook {

	private SkyWars sw;
	
	public SWRPlaceholderAPI(Plugin plugin) {
		super(plugin, "sw");
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
			double stat = (double)sw.getPlayerStat(player).getKills()/(double)sw.getPlayerStat(player).getDeaths();
			return String.format("%1$,.2f", stat);
		}
		
		if (identifier.equals("win_loss")) {
			double stat = (double)sw.getPlayerStat(player).getWins()/(double)sw.getPlayerStat(player).getLosses();
			return String.format("%1$,.2f", stat);
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
