package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.objects.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;

public class PlayerTeleportListener implements Listener
{
	
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerTeleport(final PlayerTeleportEvent a1) {
    		Player player = a1.getPlayer();
        		if (SkyWars.getCfg().getSpawn() != null) {
        			if (!a1.getFrom().getWorld().equals(SkyWars.getCfg().getSpawn().getWorld()) && a1.getTo().getWorld().equals(SkyWars.getCfg().getSpawn().getWorld())) {
                		PlayerStat.updatePlayer(a1.getPlayer().getUniqueId().toString());
                		return;
                    }
                	if (a1.getFrom().getWorld().equals(SkyWars.getCfg().getSpawn().getWorld()) && !a1.getTo().getWorld().equals(SkyWars.getCfg().getSpawn().getWorld())) {
                		if (SkyWars.getCfg().lobbyBoardEnabled()) {
            		        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        		        }
        		        if (SkyWars.getCfg().joinMenuEnabled() && player.hasPermission("sw.join")) {
        		        	if (player.getInventory().getItem(SkyWars.getCfg().getJoinSlot()) != null) {
            		        	if (player.getInventory().getItem(SkyWars.getCfg().getJoinSlot()).equals(SkyWars.getIM().getItem("joinselect"))) {
            		        		player.getInventory().setItem(SkyWars.getCfg().getJoinSlot(),  new ItemStack(Material.AIR, 1));
            		        	}
        		        	}
        		        }
        		        if (SkyWars.getCfg().optionsMenuEnabled()) {
        		        	if (player.getInventory().getItem(SkyWars.getCfg().getOptionsSlot()) != null) {
            		        	if (player.getInventory().getItem(SkyWars.getCfg().getOptionsSlot()).equals(SkyWars.getIM().getItem("optionselect"))) {
                    		        player.getInventory().setItem(SkyWars.getCfg().getOptionsSlot(), new ItemStack(Material.AIR, 1));
            		        	}
        		        	}
        		        }
                	}
                    return;
        		}
    }
    
    
}
