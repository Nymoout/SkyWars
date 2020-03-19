package com.nymoout.skywars.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nymoout.skywars.objects.PlayerStat;
import com.nymoout.skywars.objects.Taunt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import com.nymoout.skywars.utilities.VaultUtils;

public class TauntSelectionMenu {

    private static final int menuSlotsPerRow = 9;
    private static int menuSize = 45;
    private static final String menuName = new Messaging.MessageFormatter().format("menu.usetaunt-menu-title");
    
    public TauntSelectionMenu(final Player player) {
    	List<Taunt> availableItems = SkyWars.getLM().getTaunts();

        int rowCount = menuSlotsPerRow;
        double numRows = availableItems.size() / 5.0;
        for (int i = 0; i < Math.ceil(numRows)-1; i++) {
        	rowCount += menuSlotsPerRow;
        }
        menuSize = rowCount;
        int level = Util.get().getPlayerLevel(player);
        SkyWars.getIC().create(player, menuName, rowCount, new IconMenu.OptionClickEventHandler() {
			@Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                           	
				String tauntName = event.getName();
				Taunt taunt = SkyWars.getLM().getTauntFromName(tauntName);
	            if (taunt == null) {
	              	return;
	            }           
	            
	            if (SkyWars.getCfg().economyEnabled()) {
               	 	if (level < taunt.getLevel() && !player.hasPermission("sw.taunt." + taunt.getKey())) {
                 		Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
                 		return;
               	 	} else if (level >= taunt.getLevel() && !player.hasPermission("sw.taunt."+ taunt.getKey()) && !VaultUtils.get().canBuy(player, taunt.getCost())) {
               	 		Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
               	 		player.sendMessage(new Messaging.MessageFormatter().format("menu.insufficientfunds"));
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                      	return;
                    }
                } else {
               		if (level < taunt.getLevel() && !player.hasPermission("sw.taunt." + taunt.getKey())) {
               		Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
                    return;
               		}
                }
       
                if (SkyWars.getCfg().economyEnabled() && !player.hasPermission("sw.taunt." + taunt.getKey())) {
               		boolean result = VaultUtils.get().payCost(player, taunt.getCost());
               		if (!result) {
               			return;
               		} else {
               			PlayerStat ps = PlayerStat.getPlayerStats(player);
               			ps.addPerm("sw.taunt." + taunt.getKey(), true);
               			player.sendMessage(new Messaging.MessageFormatter().setVariable("cost", "" + taunt.getCost())
               					.setVariable("item", taunt.getName()).format("menu.purchase-taunt"));
               		}
                }
                
                event.setWillClose(true);
                event.setWillDestroy(true);
            
           		PlayerStat ps = PlayerStat.getPlayerStats(player);
               	ps.setTaunt(taunt.getKey());
               	DataStorage.get().saveStats(ps);
               	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getConfirmeSelctionSound(), 1, 1);
               	player.sendMessage(new Messaging.MessageFormatter().setVariable("taunt", taunt.getName()).format("menu.usetaunt-playermsg"));
            }
        });

        ArrayList<Integer> placement = new ArrayList<Integer>(Arrays.asList(menuSize-1, 0, 2, 4, 6, 8, 9, 11, 13, 15, 17, 18, 20, 22, 24, 26, 27, 29, 31, 33, 35, 
        		36, 38, 40, 42, 44, 45, 47, 49, 51, 53));
        
        for (int iii = 0; iii < availableItems.size(); iii ++) {
        	if (iii >= menuSize || iii > 21) {
                break;
            }

            Taunt taunt = availableItems.get(iii);
            List<String> loreList = Lists.newLinkedList();
            ItemStack item = new ItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("nopermission")), 1);
            
            if (level >= taunt.getLevel() || player.hasPermission("sw.taunt." + taunt.getKey())) {
            	if (SkyWars.getCfg().economyEnabled()) {
            		if (player.hasPermission("sw.taunt." + taunt.getKey()) || taunt.getCost() == 0) {
            			loreList.add(new Messaging.MessageFormatter().format("menu.usetaunt-settaunt"));
            			item = new ItemStack(taunt.getMaterial(), 1);
            		} else {
            			loreList.add(new Messaging.MessageFormatter().setVariable("cost", "" + taunt.getCost()).format("menu.cost"));
            			item = new ItemStack(taunt.getMaterial(), 1);
            		}
            	} else {
                	loreList.add(new Messaging.MessageFormatter().format("menu.usetaunt-settaunt"));
                	item = new ItemStack(taunt.getMaterial(), 1);
            	}
            } else {
            	loreList.add(new Messaging.MessageFormatter().setVariable("level", "" + taunt.getLevel()).format("menu.no-use"));
            }
  
            if (player != null) {
                SkyWars.getIC().setOption(
                        player,
                        placement.get(iii),
                        item,
                        taunt.getName(),
                        loreList.toArray(new String[loreList.size()]));
            }
         }
                
        if (player != null) {
            SkyWars.getIC().show(player);
        }
    }
}
