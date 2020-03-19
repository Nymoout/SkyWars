package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;

public class SWJoinCmd extends BaseCmd {

    public SWJoinCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "join";
        alias = new String[]{"j"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        GameType type = GameType.ALL;
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("single") || args[1].equalsIgnoreCase("solo")) {
                type = GameType.SINGLE;
            } else if (args[1].equalsIgnoreCase("team")) {
                type = GameType.TEAM;
            }
        }
        boolean joined = MatchManager.get().joinGame(player, type);
        int count = 0;
        while (count < 4 && !joined) {
            joined = MatchManager.get().joinGame(player, type);
            count++;
        }
        if (!joined) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join"));
        }
        return true;
    }

}
