package com.nymoout.skywars.commands.admin;

import org.bukkit.Location;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.utilities.Messaging;

public class SetSpawnCmd extends BaseCmd { 
	
	public SetSpawnCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "setspawn";
		alias = new String[]{"sspawn"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
	    Location spawn = player.getLocation();
	    SkyWars.getConfigManager().setSpawn(spawn);
	    SkyWars.getConfigManager().save();
	    player.sendMessage(new Messaging.MessageFormatter().format("command.spawnset"));
		return true;
	}

}
