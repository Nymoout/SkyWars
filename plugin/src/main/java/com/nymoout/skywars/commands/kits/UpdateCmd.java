package com.nymoout.skywars.commands.kits;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import com.nymoout.skywars.utilities.Messaging;

public class UpdateCmd extends BaseCmd { 
	
	public UpdateCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "update";
		alias = new String[]{"u"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		GameKit kit = GameKit.getKit(args[1]);
		if (kit == null) {
			player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
			return true;
		}
		kit.setArmor(player.getInventory().getArmorContents());
		kit.setInventory(player.getInventory().getContents());
		
		GameKit.saveKit(kit);
		
		player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).format("command.kit-update"));
		return true;
	}

}
