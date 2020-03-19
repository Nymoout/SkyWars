package com.nymoout.skywars.commands;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.objects.SWRServer;

public class SWJoinCmd extends BaseCmd {
	
	public SWJoinCmd() {
		forcePlayer = true;
		cmdName = "join";
		alias = new String[]{"j"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
		SWRServer server = SWRServer.getAvailableServer();
		if (server != null) {
			server.setPlayerCount(server.getPlayerCount() + 1);
	    	server.updateSigns();
			SkyWars.get().sendBungeeMsg(player, "Connect", server.getServerName());
		}
		return true;
	}

}
