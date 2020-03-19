package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class SWQuitCmd extends BaseCmd {

    public SWQuitCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "quit";
        alias = new String[]{"q", "leave", "l"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        GameMap map = MatchManager.get().getPlayerMap(player);
        if (map == null) {
            return false;
        }
        MatchManager.get().playerLeave(player, DamageCause.CUSTOM, true, true, true);
        return true;
    }

}
