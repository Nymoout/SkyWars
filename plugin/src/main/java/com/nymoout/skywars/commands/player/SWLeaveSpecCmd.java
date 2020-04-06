package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;

public class SWLeaveSpecCmd extends BaseCmd {

    public SWLeaveSpecCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "leave";
        alias = new String[]{"l"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        GameMap map = MatchManager.get().getSpectatorMap(player);
        if (map != null) {
            map.getSpectators().remove(player.getUniqueId());
            MatchManager.get().removeSpectator(player);
        }
        return true;
    }
}

