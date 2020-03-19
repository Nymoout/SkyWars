package com.nymoout.skywars.commands;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;

public class ReloadCmd extends BaseCmd { 
	
	public ReloadCmd() {
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
