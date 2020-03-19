package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWParticleCmd extends BaseCmd {

    public SWParticleCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "particle";
        alias = new String[]{"par"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.PARTICLEEFFECT, true);
        return true;
    }

}
