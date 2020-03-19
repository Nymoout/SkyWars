package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;


public class EditCmd extends BaseCmd {

    public EditCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "edit";
        alias = new String[]{"e"};
        argLength = 2; //counting cmdName

    }

    @Override
    public boolean run() {
        if (SkyWars.getConfigManager().getSpawn() != null) {
            final String worldName = args[1];
            GameMap gMap = GameMap.getMap(worldName);
            if (gMap == null) {
                sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
                return true;
            }
            GameMap.editMap(gMap, player);
            return true;
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.nospawn"));
            return false;
        }
    }
}
