package com.nymoout.skywars.menus;

import java.util.ArrayList;

import com.nymoout.skywars.enums.MatchState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;

public class SpectateSingleMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.spectatesinglegame-menu-title");
    
    public SpectateSingleMenu() {
        int menuSize = 45;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
    	ArrayList<Inventory> invs = new ArrayList<>();
    	invs.add(menu);
    	 
        SkyWars.getIconMenuController().create("spectatesinglemenu", invs, event -> {
            Player player = event.getPlayer();
            GameMap gMap = MatchManager.get().getPlayerMap(player);
            if (gMap != null) {
                return;
            }

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                player.closeInventory();
                return;
            }
            gMap = GameMap.getMapByDisplayName(ChatColor.stripColor(name));
            if (gMap == null) {
                return;
            }

            if (player.hasPermission("sw.spectate")) {
                player.closeInventory();
                if (gMap.getMatchState() != MatchState.OFFLINE && gMap.getMatchState() != MatchState.ENDING) {
                    MatchManager.get().addSpectator(gMap, player);
                }
            }
        });
    }

}
