package com.nymoout.skywars.commands;

import org.bukkit.Location;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;

public class SetSpawnCmd extends BaseCmd { 
	
	public SetSpawnCmd() {
		forcePlayer = true;
		cmdName = "setspawn";
		alias = new String[]{"sspawn"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
	    Location spawn = player.getLocation();
	    SkyWars.getCfg().setSpawn(spawn);
	    SkyWars.getCfg().save();
	    player.sendMessage(new Messaging.MessageFormatter().format("command.spawnset"));
		return true;
	}

}
