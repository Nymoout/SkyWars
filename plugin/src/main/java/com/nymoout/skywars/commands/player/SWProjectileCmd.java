package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.menus.playeroptions.OptionSelectionMenu;

public class SWProjectileCmd extends BaseCmd {

    public SWProjectileCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "projectile";
        alias = new String[]{"proj"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        new OptionSelectionMenu(player, PlayerOptions.PROJECTILEEFFECT, true);
        return true;
    }

}
