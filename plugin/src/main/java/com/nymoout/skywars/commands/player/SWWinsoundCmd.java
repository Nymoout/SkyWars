package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWWinsoundCmd extends BaseCmd {

    public SWWinsoundCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "winsound";
        alias = new String[]{"ws"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.WINSOUND, true);
        return true;
    }

}
