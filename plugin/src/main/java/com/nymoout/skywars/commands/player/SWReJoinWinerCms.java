package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SWReJoinWinerCms {

    public class SWReJoinCmd extends BaseCmd {
        public SWReJoinCmd(String t) {
            this.type = t;
            this.forcePlayer = true;
            this.cmdName = "rejoinwiner";
            this.alias = new String[]{"rjw"};
            this.argLength = 1;
        }

        public boolean run() {
            this.player.performCommand("sw leave");
            this.reJoinGame(this.player);
            return true;
        }

        public void reJoinGame(Player p) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWars.get(), () -> {
                if (p.isOnline()) {
                    p.performCommand("sw join");
                }
            }, 5L);
        }
    }
}
