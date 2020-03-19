package com.nymoout.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.objects.PlayerStat;
import com.nymoout.skywars.utilities.Messaging;

public class ClearStatsCmd extends BaseCmd { 
	
	public ClearStatsCmd() {
		forcePlayer = false;
		cmdName = "clearstats";
		alias = new String[]{"cs", "cstats"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		Player swPlayer = null;
		for (Player playerMatch: Bukkit.getOnlinePlayers()) {
			if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
				swPlayer = playerMatch;
			}
		}
		
		if (swPlayer != null) {
			PlayerStat pStat = PlayerStat.getPlayerStats(swPlayer);
			pStat.clear();
			DataStorage.get().saveStats(pStat);
			player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.stats-cleared"));
			return true;
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					OfflinePlayer offlinePlayer = null;
					for (OfflinePlayer playerMatch: Bukkit.getOfflinePlayers()) {
						if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
							offlinePlayer = playerMatch;
						}
					}
					if (offlinePlayer != null) {
						final String uuid = offlinePlayer.getUniqueId().toString();
						new BukkitRunnable() {
							@Override
							public void run() {
									DataStorage.get().removePlayerData(uuid);
									player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.stats-cleared"));
							}
						}.runTask(SkyWars.get());
					} else {
						new BukkitRunnable() {
							@Override
							public void run() {
								player.sendMessage(new Messaging.MessageFormatter().format("command.player-not-found"));
							}
						}.runTask(SkyWars.get());
					}
				}
			}.runTaskAsynchronously(SkyWars.get());
		}
		return true;
	}

}
