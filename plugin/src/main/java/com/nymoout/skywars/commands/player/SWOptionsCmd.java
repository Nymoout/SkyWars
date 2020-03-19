package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.menus.playeroptions.OptionsSelectionMenu;

public class SWOptionsCmd extends BaseCmd {

    public SWOptionsCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "options";
        alias = new String[]{"o"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionsSelectionMenu(player);
        return true;
    }

}
