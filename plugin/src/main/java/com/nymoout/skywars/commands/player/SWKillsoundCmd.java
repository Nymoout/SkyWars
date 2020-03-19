package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWKillsoundCmd extends BaseCmd {

    public SWKillsoundCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "killsound";
        alias = new String[]{"ks"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.KILLSOUND, true);
        return true;
    }

}
