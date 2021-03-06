package com.nymoout.skywars.commands.admin;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearStatsCmd extends BaseCmd {

    public ClearStatsCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "clearstats";
        alias = new String[]{"cs"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        Player swPlayer = null;
        for (Player playerMatch : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                swPlayer = playerMatch;
            }
        }

        if (swPlayer != null) {
            PlayerStat pStat = PlayerStat.getPlayerStats(swPlayer);
            if (pStat != null) {
                pStat.clear();
                DataStorage.get().saveStats(pStat);
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1]).format("command.stats-cleared"));
                return true;
            }
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    OfflinePlayer offlinePlayer = null;
                    for (OfflinePlayer playerMatch : Bukkit.getOfflinePlayers()) {
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
                                sender.sendMessage(new Messaging.MessageFormatter().setVariable("player", args[1]).format("command.stats-cleared"));
                            }
                        }.runTask(SkyWars.get());
                    } else {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sender.sendMessage(new Messaging.MessageFormatter().format("command.player-not-found"));
                            }
                        }.runTask(SkyWars.get());
                    }
                }
            }.runTaskAsynchronously(SkyWars.get());
        }
        return true;
    }

}
