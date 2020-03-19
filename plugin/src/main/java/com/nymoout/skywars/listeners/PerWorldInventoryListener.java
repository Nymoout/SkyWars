package com.nymoout.skywars.listeners;

import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import me.ebonjaeger.perworldinventory.event.InventoryLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PerWorldInventoryListener implements Listener {

	@EventHandler
	public void perWorldInventoryLoad(InventoryLoadEvent e) {
		GameMap gMap = MatchManager.get().getPlayerMap(e.getPlayer());
		if (gMap != null) {
			e.setCancelled(true);
		}
	}

}
