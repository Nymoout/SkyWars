package com.nymoout.skywars.commands.admin;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.utilities.Messaging;

public class ReloadCmd extends BaseCmd { 
	
	public ReloadCmd(String t) {
		type = t;
		forcePlayer = false;
		cmdName = "reload";
		alias = new String[]{"r"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
        SkyWars.get().onDisable();
        SkyWars.get().load();
        sender.sendMessage(new Messaging.MessageFormatter().format("command.reload"));
		return true;
	}

}
