package com.nymoout.skywars.menus.gameoptions;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.menus.IconMenu;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitVoteOption {

	private GameMap gameMap;
	private String key;
	private IconMenu iconMenu;
    private Map<GameKit, Boolean> availableKits = new HashMap<>();
	
	public KitVoteOption (GameMap gameMap, String key) {
		this.gameMap = gameMap;
		createMenu(key, new Messaging.MessageFormatter().format("menu.kit-voting-menu"));
		

	}
	
	private void createMenu(String key, String name) {
		this.key = key;
		int menuSize = SkyWars.getConfigManager().getKitMenuSize();
		ArrayList<Inventory> invs = new ArrayList<>();
		for (GameKit gameKit: GameKit.getAvailableKits()) {
        	int page = gameKit.getPage() - 1;
            if(invs.isEmpty() || invs.size() < page + 1) {
            	while (invs.size() < page + 1) {
            		invs.add(Bukkit.createInventory(null, menuSize + 9, name));
            	}
            }
		}
		
		SkyWars.getIconMenuController().create(key, invs, event -> {
            String itemName = event.getName();
            if (itemName.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                event.getPlayer().closeInventory();
                return;
            }
            GameKit kit = GameKit.getKit(itemName);
            if(kit != null) {
                if (isKitLocked(kit)) {
                    if (event.getPlayer().hasPermission("sw.kit." + kit.getFilename())) {
                        loadKit(event.getPlayer(), gameMap, kit);
                    } else {
                        return;
                    }
                }
                gameMap.setKitVote(event.getPlayer(), kit);
                updateKitVotes();
                Util.get().playSound(event.getPlayer(), event.getPlayer().getLocation(), SkyWars.getConfigManager().getConfirmeSelctionSound(), 1, 1);
                event.getPlayer().closeInventory();
                MatchManager.get().message(gameMap, new Messaging.MessageFormatter()
                        .setVariable("player", event.getPlayer().getName())
                        .setVariable("kit", kit.getColorName()).format("game.votekit"));
            }
        });
        iconMenu = SkyWars.getIconMenuController().getMenu(key);
        for (GameKit gameKit: GameKit.getAvailableKits()) {
            loadKit(null, null, gameKit);
        }
	}
	
	public void restore() {
		availableKits.clear();
		for (GameKit gameKit: GameKit.getAvailableKits()) {
            loadKit(null, null, gameKit); 
		}
	}
	   
    private void loadKit(Player player, GameMap gMap, GameKit gameKit) {
    	List<String> lores = gameKit.getColorLores();
    	ItemStack kit;
    	boolean state = false;
    	if (player == null) {
    		if (gameKit.needPermission()) {
    			state = true;
    		}
    	}    	
      	if (!state) {
    		kit = gameKit.getIcon();
    		kit.setAmount(1);
		} else {
    		kit = gameKit.getLIcon();
    		kit.setAmount(1);
    		lores.add(" ");
    		lores.add(gameKit.getColoredLockedLore());
		}
		kit = SkyWars.getNMS().getItemStack(kit, lores, gameKit.getColorName());
		iconMenu.getInventories().get(gameKit.getPage()-1).setItem(gameKit.getPosition(), kit);
		availableKits.put(gameKit, state);
		if (player != null && gMap != null) {
			MatchManager.get().message(gMap, new Messaging.MessageFormatter().setVariable("player", player.getName()).setVariable("kit", gameKit.getColorName()).format("game.unlock-kit"));
		}
    }
    
	public void updateKitVotes() {
		HashMap <GameKit, Integer> votes = new HashMap<>();
		for (GameKit gKit: availableKits.keySet()) {
			votes.put(gKit, 0);
		}
		
		for (PlayerCard pCard: gameMap.getPlayerCards()) {
			Player player = pCard.getPlayer();
			if (player != null) {
				GameKit gKit = pCard.getKitVote();
				if (gKit != null) {
					int multiplier = Util.get().getMultiplier(player);
					votes.put(gKit, votes.get(gKit) + (multiplier));
				}
			}	
		}
		for (GameKit gKit: votes.keySet()) {
			boolean locked = availableKits.get(gKit);
			ItemStack kit;
			List<String> lores = gKit.getColorLores(); 
			if (locked) {
				kit = gKit.getLIcon();
				lores.add(" ");
				lores.add(gKit.getColoredLockedLore());
			} else {
				kit = gKit.getIcon();
				lores.add(" ");
				lores.add(new Messaging.MessageFormatter().setVariable("number", "" + votes.get(gKit)).format("game.vote-display"));
			}
			kit.setAmount(votes.get(gKit) == 0 ? 1 : votes.get(gKit));
			kit = SkyWars.getNMS().getItemStack(kit, lores, ChatColor.translateAlternateColorCodes('&', gKit.getColorName()));

			iconMenu.getInventories().get(gKit.getPage()-1).setItem(gKit.getPosition(), kit);
		}
		
	}
	
    public void getVotedKit() {
    	HashMap <GameKit, Integer> votes = new HashMap<>();
		for (GameKit gKit: availableKits.keySet()) {
			votes.put(gKit, 0);
		}
		
		for (PlayerCard pCard: gameMap.getPlayerCards()) {
			Player player = pCard.getPlayer();
			if (player != null) {
				GameKit gKit = pCard.getKitVote();
				if (gKit != null) {
					int multiplier = Util.get().getMultiplier(player);
					while (gKit.getName().equalsIgnoreCase(new Messaging.MessageFormatter().format("kit.vote-random"))) {
						Random rand = new Random();
						int n = rand.nextInt(GameKit.getAvailableKits().size());
						gKit = GameKit.getAvailableKits().get(n);
					}
					votes.put(gKit, votes.get(gKit) + (multiplier));
				}
			}	
		}
		int highest = 0;
		GameKit voted = null;
		for (GameKit gKit: votes.keySet()) {
			if (votes.get(gKit) >= highest) {
				highest = votes.get(gKit);
				voted = gKit;
			}
		}
		gameMap.setKit(voted);
    }
    
	private boolean isKitLocked(GameKit kit2) {
		return availableKits.get(kit2);
	}

	public String getKey() {
		return key;
	}


}
