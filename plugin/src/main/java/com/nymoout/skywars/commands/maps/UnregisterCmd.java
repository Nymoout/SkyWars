package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;

public class UnregisterCmd extends BaseCmd {

    public UnregisterCmd(String t) {
        type = t;
        forcePlayer = false;
        cmdName = "unregister";
        alias = new String[]{"unreg"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        String worldName = args[1];
        GameMap map = GameMap.getMap(worldName);
        if (map != null) {
            map.unregister(true);
            sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.unregistered"));
            return true;
        } else {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
            return true;
        }
    }
}
