package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SWSpectateCmd extends BaseCmd {

    public SWSpectateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "spectate";
        alias = new String[]{"spec"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        GameMap gMap;
        gMap = GameMap.getMap(ChatColor.stripColor(args[1]));
        if (gMap != null) {
            sendSpectator(gMap);
            return true;
        } else {
            gMap = GameMap.getMapByDisplayName(ChatColor.stripColor(args[1]));
            if (gMap != null) {
                sendSpectator(gMap);
                return true;
            } else {
                Player swPlayer = null;
                for (Player playerMatch : Bukkit.getOnlinePlayers()) {
                    if (ChatColor.stripColor(playerMatch.getName()).equalsIgnoreCase(ChatColor.stripColor(args[1]))) {
                        swPlayer = playerMatch;
                    }
                }
                if (swPlayer != null) {
                    gMap = MatchManager.get().getPlayerMap(swPlayer);
                    sendSpectator(gMap);
                    return true;
                }
            }
        }
        return true;
    }

    private void sendSpectator(GameMap gMap) {
        if (gMap != null) {
            if (gMap.getMatchState() == MatchState.WAITINGSTART || gMap.getMatchState() == MatchState.PLAYING) {
                MatchManager.get().addSpectator(gMap, player);
            } else {
                player.sendMessage(new Messaging.MessageFormatter().format("error.spectate-notatthistime"));
            }
        }
    }
}
