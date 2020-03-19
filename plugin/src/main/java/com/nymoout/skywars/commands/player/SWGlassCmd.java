package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWGlassCmd extends BaseCmd {

    public SWGlassCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "glass";
        alias = new String[]{"g"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.GLASSCOLOR, true);
        return true;
    }

}
