package com.nymoout.skywars.commands.maps;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
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
		if (SkyWars.getConfigManager().getSpawn() != null) {
			String worldName = args[1];
			World.Environment environment = World.Environment.NORMAL;
			if (args.length > 2) {
				if (args[2].equalsIgnoreCase("the_end")) {
					environment = World.Environment.THE_END;
				} else if (args[2].equalsIgnoreCase("nether")) {
					environment = World.Environment.NETHER;
				}
			}

			GameMap gMap = GameMap.getMap(worldName);
			if (gMap != null) {
				player.sendMessage(new Messaging.MessageFormatter().format("error.map-exists"));
				return true;
			}
				World result = GameMap.createNewMap(worldName, environment);
				if (result == null) {
					player.sendMessage(new Messaging.MessageFormatter().format("error.map-world-exists"));
					return true;
				} else {
					player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", worldName).format("maps.created"));
					gMap = GameMap.getMap(worldName);
					if (gMap != null) {
                        gMap.setEditing(true);
						World editWorld = SkyWars.get().getServer().getWorld(worldName);
						editWorld.setAutoSave(true);
                        player.setGameMode(GameMode.CREATIVE);
                        result.getBlockAt(0, 75, 0).setType(Material.STONE);
                        player.teleport(new Location(result, 0, 76, 0), TeleportCause.PLUGIN);
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }
                    return true;
				}
		} else {
			sender.sendMessage(new Messaging.MessageFormatter().format("error.nospawn"));
			return false;
		}
	}
}
