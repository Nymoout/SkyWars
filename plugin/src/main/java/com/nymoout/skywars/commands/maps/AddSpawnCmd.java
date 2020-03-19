package com.nymoout.skywars.commands.maps;

import org.bukkit.Material;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;

import net.md_5.bungee.api.ChatColor;


public class AddSpawnCmd extends BaseCmd { 

	public AddSpawnCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "spawn";
		alias = new String[]{"sp"};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		if (SkyWars.getConfigManager().getSpawn() != null) {
			final String type = args[1];
			GameMap gMap = GameMap.getMap(player.getLocation().getWorld().getName());
			if (gMap == null || !gMap.isEditing()) {
				player.sendMessage(new Messaging.MessageFormatter().format("error.map-not-editing"));
				return true;
			}
			if (type.equalsIgnoreCase("player") || type.equalsIgnoreCase("p")) {
				gMap.addTeamCard(player.getLocation());
				player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
				player.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + gMap.getMaxPlayers()).setVariable("mapname", gMap.getDisplayName()).format("maps.addSpawn"));
			} else if (type.equalsIgnoreCase("spec") || type.equalsIgnoreCase("s")) {
				gMap.setSpectateSpawn(player.getLocation());
				player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("maps.specSpawn"));
			} else if (type.equalsIgnoreCase("deathmatch") || type.equalsIgnoreCase("dm") || type.equalsIgnoreCase("d")) {
				gMap.addDeathMatchSpawn(player.getLocation());
				player.getLocation().getBlock().setType(Material.EMERALD_BLOCK);
				player.sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + gMap.getDeathMatchSpawns().size()).setVariable("mapname", gMap.getDisplayName()).format("maps.addDeathSpawn"));
			} else {
				player.sendMessage(ChatColor.RED + "Type must be: " + "player OR spec");
			}
			return true;
		}
		return true; 
	}
}
