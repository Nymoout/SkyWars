package com.nymoout.skywars.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.objects.GlassColor;
import com.nymoout.skywars.objects.PlayerStat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.VaultUtils;

public class ColorSelectionMenu {

    private static final int menuSlotsPerRow = 9;
    private static int menuSize = 45;
    private static final String menuName = new Messaging.MessageFormatter().format("menu.usecolor-menu-title");
    
    public ColorSelectionMenu(final Player player) {
        List<GlassColor> availableItems = SkyWars.getLM().getColorItems();

        int rowCount = menuSlotsPerRow;
        double numRows = availableItems.size() / 5.0;
        for (int i = 0; i < Math.ceil(numRows)-1; i++) {
        	rowCount += menuSlotsPerRow;
        }
        menuSize = rowCount;
        final int level = com.nymoout.skywars.utilities.Util.get().getPlayerLevel(player);
        SkyWars.getIC().create(player, menuName, rowCount, new IconMenu.OptionClickEventHandler() {
			@Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                
                String name = event.getName();
            	
                GlassColor glass = SkyWars.getLM().getGlassByName(name);
                if (glass == null) {
                    return;
                } 
                                
                if (SkyWars.getCfg().economyEnabled()) {
               	 	if (level < glass.getLevel() && !player.hasPermission("sw.glasscolor." + glass.getKey())) {
                 		com.nymoout.skywars.utilities.Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
                 		return;
               	 	} else if (level >= glass.getLevel() && !player.hasPermission("sw.glasscolor." + glass.getKey()) && !VaultUtils.get().canBuy(player, glass.getCost())) {
               	 		com.nymoout.skywars.utilities.Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
               	 		player.sendMessage(new Messaging.MessageFormatter().format("menu.insufficientfunds"));
                        event.setWillClose(true);
                        event.setWillDestroy(true);
               	 		return;
                    }
                } else {
               		if (level < glass.getLevel() && !player.hasPermission("sw.glasscolor." + glass.getKey())) {
               		com.nymoout.skywars.utilities.Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
                    return;
               		}
                }
       
                if (SkyWars.getCfg().economyEnabled() && !player.hasPermission("sw.glasscolor." + glass.getKey())) {
               		boolean result = VaultUtils.get().payCost(player, glass.getCost());
               		if (!result) {
               			return;
               		} else {
               			PlayerStat ps = PlayerStat.getPlayerStats(player);
               			ps.addPerm("sw.glasscolor." + glass.getKey(), true);
               			player.sendMessage(new Messaging.MessageFormatter().setVariable("cost", "" + glass.getCost())
               					.setVariable("item", glass.getName()).format("menu.purchase-glass"));
               		}
                }
			
                event.setWillClose(true);
                event.setWillDestroy(true);
       
                PlayerStat ps = PlayerStat.getPlayerStats(player);
                ps.setGlassColor(glass.getKey());
                DataStorage.get().saveStats(ps);
                com.nymoout.skywars.utilities.Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getConfirmeSelctionSound(), 1, 1);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("color", glass.getName()).format("menu.usecolor-playermsg"));      	
            }
        });

        ArrayList<Integer> placement = new ArrayList<Integer>(Arrays.asList(menuSize-1, 0, 2, 4, 6, 8, 9, 11, 13, 15, 17, 18, 20, 22, 24, 26, 27, 29, 31, 33, 35, 
        		36, 38, 40, 42, 44, 45, 47, 49, 51, 53));
        
        for (int iii = 0; iii < availableItems.size(); iii ++) {
            if (iii >= menuSize || iii > 21) {
                break;
            }
            
            GlassColor glass = availableItems.get(iii);
            List<String> loreList = Lists.newLinkedList();
            ItemStack item = new ItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("nopermission")), 1);
            
            if (level >= glass.getLevel() || player.hasPermission("sw.glasscolor." + glass.getKey())) {
            	if (SkyWars.getCfg().economyEnabled()) {
            		if (player.hasPermission("sw.glasscolor." + glass.getKey()) || glass.getCost() == 0) {
            			loreList.add(new Messaging.MessageFormatter().format("menu.usecolor-setcolor"));
            			item = glass.getItem().clone();
            		} else {
            			loreList.add(new Messaging.MessageFormatter().setVariable("cost", "" + glass.getCost()).format("menu.cost"));
            			item = glass.getItem().clone();
            		}
            	} else {
                	loreList.add(new Messaging.MessageFormatter().format("menu.usecolor-setcolor"));
                	item = glass.getItem().clone();
            	}
            } else {
            	loreList.add(new Messaging.MessageFormatter().setVariable("level", "" + glass.getLevel()).format("menu.no-use"));
            }
            
            if (player != null) {
                SkyWars.getIC().setOption(
                        player,
                        placement.get(iii),
                        item,
                        glass.getName(),
                        loreList.toArray(new String[loreList.size()]));
            }
         }
                
        if (player != null) {
            SkyWars.getIC().show(player);
        }
    }
}
