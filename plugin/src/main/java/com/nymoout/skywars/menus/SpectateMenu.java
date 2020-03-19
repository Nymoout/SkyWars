package com.nymoout.skywars.menus;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SpectateMenu {

	private static final String menuName = new Messaging.MessageFormatter().format("menu.spectategame-menu-title");

    public SpectateMenu() {
		int menuSize = 27;
		Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
    	ArrayList<Inventory> invs = new ArrayList<>();
    	invs.add(menu);

        List<String> lores = new ArrayList<>();
        lores.add(new Messaging.MessageFormatter().format("menu.joinloresingle1"));
        lores.add(new Messaging.MessageFormatter().format("menu.joinloresingle2"));
        ItemStack single = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("singlemenu"), lores,
                new Messaging.MessageFormatter().format("items.joinsingle"));

        lores.clear();
        lores.add(new Messaging.MessageFormatter().format("menu.joinloreteam1"));
        lores.add(new Messaging.MessageFormatter().format("menu.joinloreteam2"));
        ItemStack team = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("teammenu"), lores,
                new Messaging.MessageFormatter().format("items.jointeam"));

        invs.get(0).setItem(SkyWars.getConfigManager().getSingleSlot(), single);
        invs.get(0).setItem(SkyWars.getConfigManager().getTeamSlot(), team);

        SkyWars.getIconMenuController().create("spectatemenu", invs, event -> {
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
			if (event.getSlot() == SkyWars.getConfigManager().getSingleSlot()) {
				if (!SkyWars.getIconMenuController().hasViewers("joinsinglemenu")) {
					new BukkitRunnable() {
						@Override
						public void run() {
							SkyWars.getIconMenuController().getMenu("joinsinglemenu").update();
						}
					}.runTaskLater(SkyWars.get(), 5);
				}
				SkyWars.getIconMenuController().show(player, "spectatesinglemenu");
				return;
			}

			if (event.getSlot() == SkyWars.getConfigManager().getTeamSlot()) {
				if (!SkyWars.getIconMenuController().hasViewers("jointeammenu")) {
					new BukkitRunnable() {
						@Override
						public void run() {
							SkyWars.getIconMenuController().getMenu("jointeammenu").update();
						}
					}.runTaskLater(SkyWars.get(), 5);
				}
				SkyWars.getIconMenuController().show(player, "spectateteammenu");
			}
		});
    }
}
