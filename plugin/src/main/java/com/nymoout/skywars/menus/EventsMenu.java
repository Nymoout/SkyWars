package com.nymoout.skywars.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.matchevents.MatchEvent;

class EventsMenu {

	private static final String menuName = ChatColor.DARK_PURPLE + "Events Manager: ";
    
    EventsMenu(final Player player, GameMap gMap) {

		int menuSize = 27;
		Inventory inv = Bukkit.createInventory(null, menuSize + 9, menuName + gMap.getName());
    	List<String> lores = new ArrayList<>();
    	for (MatchEvent event: gMap.getEvents()) {
    		lores.clear();
    		String part1;
    		String part2;
    		String part3;
    		String part4;
    		if (event.isEnabled()) {
    			part1 = ChatColor.GREEN + "Event Enabled";
    			part3 = ChatColor.AQUA + "Left Click to Disable";
    		} else {
    			part1 = ChatColor.RED + "Event Disabled";
    			part3 = ChatColor.AQUA + "Left Click to Enable";
    		}
    		if (event.hasFired()) {
    			part2 = ChatColor.GREEN + "Event is Ongoing";
    			part4 = ChatColor.GOLD + "Right Click to End the Event";
    		} else {
    			part2 = ChatColor.RED + "Event is not Running";
    			part4 = ChatColor.GOLD + "Right Click to Force Start Event";
    		}

    		lores.add(part1);
    		lores.add(part2);
    		lores.add(part3);
    		lores.add(part4);
    		ItemStack item = SkyWars.getNMS().getItemStack(event.getMaterial(), lores, event.getTitle());
    		inv.setItem(event.getSlot(), item);
    	}
               
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(inv);

        SkyWars.getIconMenuController().create(player, invs, event -> {
			String name = event.getName();
			if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
				SkyWars.getIconMenuController().show(player, gMap.getArenaKey());
				new BukkitRunnable() {
					@Override
					public void run() {
						gMap.updateArenaManager();
					}
				}.runTaskLater(SkyWars.get(), 2);
				return;
			}
			MatchEvent mEvent = null;
			for (MatchEvent e: gMap.getEvents()) {
				if(ChatColor.translateAlternateColorCodes('&', e.getTitle()).equals(name)) {
					mEvent = e;
					break;
				}
			}
			if (mEvent != null) {
				if (event.getClick().equals(ClickType.RIGHT)) {
					if (mEvent.hasFired()) {
						mEvent.endEvent(true);
					} else {
						mEvent.doEvent();
					}
					new EventsMenu(player, gMap);
				} else if (event.getClick().equals(ClickType.LEFT)) {
					mEvent.setEnabled(!mEvent.isEnabled());
					mEvent.saveEventData();
					new EventsMenu(player, gMap);
				}
			}
		});
                
        if (player != null) {
            SkyWars.getIconMenuController().show(player, null);
        }
    }
}
