package com.nymoout.skywars.commands.kits;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class PositionCmd extends BaseCmd {

    public PositionCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "position";
        alias = new String[]{"pos"};
        argLength = 3; //counting cmdName
    }

    @Override
    public boolean run() {
        GameKit kit = GameKit.getKit(args[1]);
        if (kit == null) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
            return true;
        }
        int position;
        if (Util.get().isInteger(args[2])) {
            position = Integer.valueOf(args[2]);
        } else {
            player.sendMessage(new Messaging.MessageFormatter().format("error.position"));
            return false;
        }

        if (!(position >= 0 && position <= 35)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.position"));
            return false;
        }

        kit.setPosition(position);

        GameKit.saveKit(kit);

        player.sendMessage(new Messaging.MessageFormatter().setVariable("position", args[2]).format("command.kit-position"));
        return true;
    }

}
