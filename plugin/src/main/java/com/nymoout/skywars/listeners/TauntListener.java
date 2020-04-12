package com.nymoout.skywars.listeners;

import java.util.HashMap;

import com.nymoout.skywars.enums.MatchState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.menus.playeroptions.TauntOption;
import com.nymoout.skywars.utilities.Messaging;

public class TauntListener implements Listener {

	private final HashMap<String, Long> lastHandSwap = new HashMap<>();
	private final HashMap<String, Long> lastTaunt = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void pressedTauntKey(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap == null) {
        	return;
        }
        if (gameMap.getMatchState().equals(MatchState.WAITINGSTART)){
        	return;
		}
		String uuid = e.getPlayer().getUniqueId().toString();
		if (!player.isSneaking()) {
			if (lastHandSwap.containsKey(uuid)) {
				if (System.currentTimeMillis() - lastHandSwap.get(uuid) < 500) {
					if (lastTaunt.containsKey(uuid)) {
						if (System.currentTimeMillis() - lastTaunt.get(uuid) < SkyWars.getConfigManager().getCooldown()*1000) {
							int cooldown = (int) ((SkyWars.getConfigManager().getCooldown()*1000 - (System.currentTimeMillis() - lastTaunt.get(uuid)))/1000);
							int seconds = (cooldown % 60) + 1;
							int minutes = (cooldown-(seconds - 1)) / 60;
							String cooldownText = "";
							if (minutes > 0) {
								cooldownText = cooldownText + minutes + " Minutes ";
							} 
							if (seconds > 0) {
								cooldownText = cooldownText + seconds + " Seconds";
							}
							
							e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("timeleft", cooldownText).format("error.cooldown"));
							return;
						} else {
							lastTaunt.remove(uuid);
						}
					}
					PlayerStat ps = PlayerStat.getPlayerStats(e.getPlayer());
					if (ps != null) {
                        String tauntName = ps.getTaunt();
                        TauntOption taunt = (TauntOption) TauntOption.getPlayerOptionByKey(tauntName);
                        if (taunt != null) {
                            if (!taunt.getKey().equals("none")) {
									taunt.performTaunt(e.getPlayer());
                            }
                        }
                        lastHandSwap.remove(uuid);
                        lastTaunt.put(uuid, System.currentTimeMillis());
                    }
				} else {
					lastHandSwap.put(uuid, System.currentTimeMillis());
				}
			} else {
				lastHandSwap.put(uuid, System.currentTimeMillis());
			}
		}	
	}	
}
