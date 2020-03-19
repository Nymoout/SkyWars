package com.nymoout.skywars.commands;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.enums.LeaderType;

public class SWUpdateTopCmd extends BaseCmd { 
	
	public SWUpdateTopCmd() {
		forcePlayer = false;
		cmdName = "updatetop";
		alias = new String[]{"ut"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
		if (SkyWars.getCfg().eloEnabled()) {
			DataStorage.get().updateTop(LeaderType.ELO, SkyWars.getCfg().getLeaderSize());
		} 
		if (SkyWars.getCfg().winsEnabled()) {
			DataStorage.get().updateTop(LeaderType.WINS, SkyWars.getCfg().getLeaderSize());
		}
		if (SkyWars.getCfg().lossesEnabled()) {
			DataStorage.get().updateTop(LeaderType.LOSSES, SkyWars.getCfg().getLeaderSize());
		}
		if (SkyWars.getCfg().killsEnabled()) {
			DataStorage.get().updateTop(LeaderType.KILLS, SkyWars.getCfg().getLeaderSize());
		}
		if (SkyWars.getCfg().deathsEnabled()) {
			DataStorage.get().updateTop(LeaderType.DEATHS, SkyWars.getCfg().getLeaderSize());
		}
		if (SkyWars.getCfg().xpEnabled()) {
			DataStorage.get().updateTop(LeaderType.XP, SkyWars.getCfg().getLeaderSize());
		}
		return true;
	}
}
