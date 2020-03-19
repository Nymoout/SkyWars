package com.nymoout.skywars.commands.admin;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;

public class StartCmd extends BaseCmd { 
	
	public StartCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "start";
		alias = new String[]{"s"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
		GameMap gMap = MatchManager.get().getPlayerMap(player);
		if (gMap != null) {
			MatchManager.get().forceStart(player);
		}
		return true;
	}

}
