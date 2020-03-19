package com.nymoout.skywars.commands.kits;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import com.nymoout.skywars.utilities.Messaging;

public class IconCmd extends BaseCmd { 
	
	public IconCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "icon";
		alias = new String[]{"i"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		GameKit kit = GameKit.getKit(args[1]);
		if (kit == null) {
			player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", args[1]).format("command.no-kit"));
			return true;
		}
		kit.setIcon(SkyWars.getNMS().getMainHandItem(player).clone());
		
		GameKit.saveKit(kit);
		
		player.sendMessage(new Messaging.MessageFormatter().setVariable("icon", kit.getIcon().getType().toString()).format("command.kit-icon"));
		return true;
	}

}
