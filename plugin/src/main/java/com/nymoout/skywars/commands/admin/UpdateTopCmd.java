package com.nymoout.skywars.commands.admin;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.enums.LeaderType;

public class UpdateTopCmd extends BaseCmd { 
	
	public UpdateTopCmd(String t) {
		type = t;
		forcePlayer = false;
		cmdName = "updatetop";
		alias = new String[]{"ut"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
		for (LeaderType type: LeaderType.values()) {
			if (SkyWars.getConfigManager().isTypeEnabled(type)) {
				DataStorage.get().updateTop(type, SkyWars.getConfigManager().getLeaderSize());
			}
		}
		return true;
	}
}
