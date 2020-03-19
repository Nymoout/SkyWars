package com.nymoout.skywars.menus.gameoptions;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class VotingMenu {
	
    private static final String menuName = new Messaging.MessageFormatter().format("menu.options-menu-title");
    
    public VotingMenu(final Player player) {
        int menuSize = 27;
    	GameMap gMap = MatchManager.get().getPlayerMap(player);
    	if (gMap != null && player != null) {
    		Inventory inv = Bukkit.createInventory(null, menuSize + 9, menuName);

            if (SkyWars.getConfigManager().isChestVoteEnabled()) {
                if (player.hasPermission("sw.chestvote")) {
                    inv.setItem(SkyWars.getConfigManager().getChestVotePos(), SkyWars.getItemsManager().getItem("chestvote"));
                } else {
                    inv.setItem(SkyWars.getConfigManager().getChestVotePos(), SkyWars.getItemsManager().getItem("nopermission"));
                }
            }
            if (SkyWars.getConfigManager().isHealthVoteEnabled()) {
                if (player.hasPermission("sw.healthvote")) {
                    inv.setItem(SkyWars.getConfigManager().getHealthVotePos(), SkyWars.getItemsManager().getItem("healthvote"));
                } else {
                    inv.setItem(SkyWars.getConfigManager().getHealthVotePos(), SkyWars.getItemsManager().getItem("nopermission"));
                }
            }
            if (SkyWars.getConfigManager().isTimeVoteEnabled()) {
                if (player.hasPermission("sw.timevote")) {
                    inv.setItem(SkyWars.getConfigManager().getTimeVotePos(), SkyWars.getItemsManager().getItem("timevote"));
                } else {
                    inv.setItem(SkyWars.getConfigManager().getTimeVotePos(), SkyWars.getItemsManager().getItem("nopermission"));
                }
            }
            if (SkyWars.getConfigManager().isWeatherVoteEnabled()) {
                if (player.hasPermission("sw.weathervote")) {
                    inv.setItem(SkyWars.getConfigManager().getWeatherVotePos(), SkyWars.getItemsManager().getItem("weathervote"));
                } else {
                    inv.setItem(SkyWars.getConfigManager().getWeatherVotePos(), SkyWars.getItemsManager().getItem("nopermission"));
                }
            }
            if (SkyWars.getConfigManager().isModifierVoteEnabled()) {
                if (player.hasPermission("sw.modifiervote")) {
                    inv.setItem(SkyWars.getConfigManager().getModifierVotePos(), SkyWars.getItemsManager().getItem("modifiervote"));
                } else {
                    inv.setItem(SkyWars.getConfigManager().getModifierVotePos(), SkyWars.getItemsManager().getItem("nopermission"));
                }
            }
                 	
            ArrayList<Inventory> invs = new ArrayList<>();
            invs.add(inv);
            
            SkyWars.getIconMenuController().create(player, invs, event -> {
                String name = event.getName();

                if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.chest-item"))) {
                    SkyWars.getIconMenuController().show(player, gMap.getChestOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenChestMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.health-item"))) {
                    SkyWars.getIconMenuController().show(player, gMap.getHealthOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenHealthMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.time-item"))) {
                    SkyWars.getIconMenuController().show(player, gMap.getTimeOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenTimeMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.weather-item"))) {
                    SkyWars.getIconMenuController().show(player, gMap.getWeatherOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenWeatherMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.modifier-item"))) {
                    SkyWars.getIconMenuController().show(player, gMap.getModifierOption().getKey());
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenModifierMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.exit-menu-item"))) {
                    player.closeInventory();
                }
            });

            SkyWars.getIconMenuController().show(player, null);
    	}
    	
    }
}
