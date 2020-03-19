package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.objects.SWRServer;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void signPlaced(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].equalsIgnoreCase("[sw]") && lines.length >= 2) {
        	if (event.getPlayer().hasPermission("sw.signs")) {
        			Location signLocation = event.getBlock().getLocation();
                    World w = signLocation.getWorld();
                	Block b = w.getBlockAt(signLocation);
                	if(b.getType() == Material.WALL_SIGN || b.getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()) {
               			event.setCancelled(true);
               			String serverName = lines[1];
               			SWRServer server = SWRServer.getServer(serverName);
               			if (server != null) {
               				server.addSign(signLocation);
                       		event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.added"));
                       	} else {
                       		event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.no-map"));
                       	}
                	}
            	} else {
            		event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.signs-no-perm"));
        			event.setCancelled(true);
            } 
       }
    }
    
    @EventHandler
    public void signRemoved(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        World w = blockLocation.getWorld();
    	Block b = w.getBlockAt(blockLocation);
		if(b.getType() == Material.WALL_SIGN || b.getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()){
	    	Sign sign = (Sign) b.getState();
	    	Location loc = sign.getLocation();
	    	SWRServer server = SWRServer.getSign(loc);
	    	if (server != null) {
	    		server.removeSign(loc);
	    		event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.remove"));
	    	}
		}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
    	if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		 if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()) {
    				Sign s = (Sign) e.getClickedBlock().getState();
    			    Location loc = s.getLocation();
    			    SWRServer server = SWRServer.getSign(loc);
    			    if (server != null) {
    			    	if (server.getMatchState().equals(MatchState.WAITINGSTART) && server.getPlayerCount() < server.getMaxPlayers()) {
    			    		server.setPlayerCount(server.getPlayerCount() + 1);
        			    	server.updateSigns();
    			    		SkyWars.get().sendBungeeMsg(player, "Connect", server.getServerName());
    					}
    			    }
    		 }
    	}
	}
    
}
