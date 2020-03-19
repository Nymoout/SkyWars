package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;

public class DeleteCmd extends BaseCmd {

    public DeleteCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "delete";
        alias = new String[]{"d", "remove"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        String worldName = args[1];
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            boolean result = map.removeMap();
            if (result) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("maps.deleted"));
                return true;
            }
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-remove"));
            return true;
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }
    }
}
