package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SWReJoinCmd extends BaseCmd {

    public SWReJoinCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "rejoin";
        alias = new String[]{"rj"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        player.performCommand("sw quit");
        reJoinGame(player);
        return true;
    }

    public void reJoinGame(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWars.get(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.performCommand("sw join");
        }, 5L);
    }
}