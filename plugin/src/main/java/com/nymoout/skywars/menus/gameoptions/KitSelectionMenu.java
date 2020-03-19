package com.nymoout.skywars.menus.gameoptions;

import java.util.ArrayList;
import java.util.List;

import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class KitSelectionMenu {

    private static int menuSize = SkyWars.getConfigManager().getKitMenuSize();
    private static final String menuName = new Messaging.MessageFormatter().format("menu.kit-section-menu");
    
    public KitSelectionMenu(final Player player) {
    	GameMap gMap = MatchManager.get().getPlayerMap(player);
        List<GameKit> availableItems = GameKit.getAvailableKits();
        if (availableItems.size() > 0) {
        	ArrayList<Inventory> invs = new ArrayList<>();

        	for (GameKit kit: availableItems) {
                int pos = kit.getPosition();
                int page = kit.getPage() - 1;

                if(invs.isEmpty() || invs.size() < page + 1) {
                    while (invs.size() < page + 1) {
                        invs.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }
                }
                List<String> loreList = Lists.newLinkedList();
                ItemStack item = kit.getLIcon();
                boolean hasPermission = true;
                if (kit.needPermission()) {
                    if (!player.hasPermission("sw.kit." + kit.getFilename())) {
                        loreList.add(kit.getColoredLockedLore());
                        hasPermission = false;
                    }
                }
                if (hasPermission) {
                    loreList.addAll(kit.getColorLores());
                    item = kit.getIcon();
                }
                invs.get(page).setItem(pos, SkyWars.getNMS().getItemStack(item, loreList, ChatColor.translateAlternateColorCodes('&', kit.getName())));
            }
            if (gMap != null) {
            	SkyWars.getIconMenuController().create(player, invs, event -> {
                    String name = event.getName();
                    if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                        player.closeInventory();
                        return;
                    }
                    GameKit kit = GameKit.getKit(name);
                    if (kit == null) {
                        return;
                    }

                    if (kit.needPermission()) {
                        if (!player.hasPermission("sw.kit." + kit.getFilename())) {
                            Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getErrorSound(), 1, 1);
                            return;
                        }
                    }

                    player.closeInventory();
                    Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getConfirmeSelctionSound(), 1, 1);
                    gMap.setKitVote(player, kit);
                    player.sendMessage(new Messaging.MessageFormatter().setVariable("kit", kit.getColorName()).format("game.select-kit"));
                });
            }
            if (player != null) {
                SkyWars.getIconMenuController().show(player, null);
            }
        }
    }
}
