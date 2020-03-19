package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWTauntCmd extends BaseCmd {

    public SWTauntCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "taunt";
        alias = new String[]{"t"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.TAUNT, true);
        return true;
    }

}
