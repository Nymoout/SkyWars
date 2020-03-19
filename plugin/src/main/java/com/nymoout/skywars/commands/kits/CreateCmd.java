package com.nymoout.skywars.commands.kits;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import com.nymoout.skywars.utilities.Messaging;

public class CreateCmd extends BaseCmd { 
	
	public CreateCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "create";
		alias = new String[]{"c"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		GameKit.newKit(player, args[1]);
		player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.kit-create"));
		return true;
	}

}
