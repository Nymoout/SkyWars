package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.ChestPlacementType;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;

public class ChestTypeCmd extends BaseCmd {

	public ChestTypeCmd(String t) {
		type = t;
		forcePlayer = false;
		cmdName = "chesttype";
		alias = new String[]{"ct"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
			String worldName = args[1];
			GameMap map = GameMap.getMap(worldName);
			if (map != null) {
				if(map.getChestPlacementType() == ChestPlacementType.NORMAL) {
					map.setChestPlacementType(ChestPlacementType.CENTER);
				} else {
					map.setChestPlacementType(ChestPlacementType.NORMAL);
				}
				sender.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName)
						.setVariable("type", map.getChestPlacementType().toString())
						.format("maps.chestPlacementType"));
				return true;
			} else {
				sender.sendMessage(new Messaging.MessageFormatter().format("error.map-does-not-exist"));
				return true;
			}
	}
}
