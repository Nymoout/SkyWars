package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SWStatsCmd extends BaseCmd {

    public SWStatsCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "stats";
        alias = new String[]{"s"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        Player statPlayer = player;
        if (args.length > 1) {
            for (Player playerMatch : Bukkit.getOnlinePlayers()) {
                if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                    statPlayer = playerMatch;
                }
            }
        }

        final PlayerStat playerData = PlayerStat.getPlayerStats(statPlayer);
        if (playerData != null && playerData.isInitialized()) {
            player.sendMessage(new Messaging.MessageFormatter().format("stats.header"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("player", playerData.getPlayerName()).format("stats.name"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("elo", "" + playerData.getElo()).format("stats.elo"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("wins", "" + playerData.getWins()).
                    setVariable("losses", "" + playerData.getLosses()).format("stats.win-loss"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kills", "" + playerData.getKills()).
                    setVariable("deaths", "" + playerData.getDeaths()).format("stats.kill-death"));
            player.sendMessage(new Messaging.MessageFormatter().setVariable("xp", "" + playerData.getXp()).
                    format("stats.xp"));
            player.sendMessage(new Messaging.MessageFormatter().format("stats.header"));
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("stats.not-ready"));
        }
        return true;
    }

}
