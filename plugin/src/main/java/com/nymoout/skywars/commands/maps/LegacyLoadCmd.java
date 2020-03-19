package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;

public class LegacyLoadCmd extends BaseCmd {

    public LegacyLoadCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "legacyload";
        alias = new String[]{"ll"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        String worldName = args[1];
        GameMap gMap = GameMap.getMap(worldName);
        if (gMap != null) {
            gMap.scanWorld(true, player);
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-register-not-exist"));
        }
        return false;
    }
}
