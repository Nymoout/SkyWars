package com.nymoout.skywars.menus;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SpectateTeamMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.spectateteamgame-menu-title");

    public SpectateTeamMenu() {
        int menuSize = 45;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
    	ArrayList<Inventory> invs = new ArrayList<>();
    	invs.add(menu);
    	 
        SkyWars.getIconMenuController().create("spectateteammenu", invs, event -> {
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

            if (event.getClick() == ClickType.RIGHT) {
                final String n = gMap.getName();
                if (!SkyWars.getIconMenuController().hasViewers(n + "teamspectate")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWars.getIconMenuController().getMenu( n + "teamselect").update();
                        }
                    }.runTaskLater(SkyWars.get(), 5);
                }
                SkyWars.getIconMenuController().show(player, n + "teamspectate");
            } else {
                if (player.hasPermission("sw.spectate")) {
                    player.closeInventory();
                    if (gMap.getMatchState() != MatchState.OFFLINE && gMap.getMatchState() != MatchState.ENDING) {
                        MatchManager.get().addSpectator(gMap, player);
                    }
                }
            }
        });
    }

}
