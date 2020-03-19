package com.nymoout.skywars.listeners;

import com.nymoout.skywars.objects.PlayerStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener
{
	@EventHandler
    public void onJoin(final PlayerJoinEvent a1) {   	
   	 	if (PlayerStat.getPlayerStats(a1.getPlayer()) != null) {
   	 		PlayerStat.removePlayer(a1.getPlayer().getUniqueId().toString());
   	 	}
   	 	PlayerStat.getPlayers().add(new PlayerStat(a1.getPlayer()));
    }
}
