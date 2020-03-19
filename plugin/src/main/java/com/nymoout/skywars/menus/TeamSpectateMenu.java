package com.nymoout.skywars.menus;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TeamSpectateMenu {

    public TeamSpectateMenu(GameMap gMap) {
        String menuName = new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("menu.teamspectate-menu-title");
        int menuSize = 27;
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        SkyWars.getIconMenuController().create(gMap.getName() + "teamspectate", invs, event -> {
            Player player = event.getPlayer();

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                if (!SkyWars.getIconMenuController().hasViewers("spectateteammenu")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWars.getIconMenuController().getMenu("jointeammenu").update();
                        }
                    }.runTaskLater(SkyWars.get(), 5);
                }
                SkyWars.getIconMenuController().show(player, "spectateteammenu");
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
