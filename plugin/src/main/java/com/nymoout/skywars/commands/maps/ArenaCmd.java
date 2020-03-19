package com.nymoout.skywars.commands.maps;


import org.bukkit.scheduler.BukkitRunnable;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;

public class ArenaCmd extends BaseCmd {

	public ArenaCmd(String t) {
		type = t;
		forcePlayer = true;
		cmdName = "arenas";
		alias = new String[]{"a"};
		argLength = 1; //counting cmdName
	}

	@Override
	public boolean run() {
		GameMap.openArenasManager(player);
    	new BukkitRunnable() {
			@Override
			public void run() {
				GameMap.updateArenasManager();
			}
		}.runTaskLater(SkyWars.get(), 2);
		return true;
	}
}
