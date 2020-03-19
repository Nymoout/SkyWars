package com.nymoout.skywars.managers;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsManager {

    private final Map<String, ItemStack> gameItems = new HashMap<>();

    public ItemsManager() {
    	getMatchStartItems();
    	getChestVoteItems();
    	getHealthVoteItems();
    	getTimeVoteItems();
    	getWeatherVoteItems();
    	getModifierVoteItems();
    	getLobbyItem();
    	getOptionItems();
    	getSignItems();
    }

	private void addItem(String materialref, List<String> lore, String message) {
    	String mat = SkyWars.getConfigManager().getMaterial(materialref);
		int data = -1;
		String matWithData = "";
		String[] matParts = mat.split(":");
		if (matParts.length == 2) {
			matWithData = matParts[0];
			data = Integer.valueOf(matParts[1]);
		}
		ItemStack item;
		if (data != -1) {
			item = SkyWars.getNMS().getColorItem(matWithData, (byte) data);
		} else {
			item = new ItemStack(Material.valueOf(SkyWars.getConfigManager().getMaterial(materialref).toUpperCase()), 1);
		}
		
    	ItemStack addItem = SkyWars.getNMS().getItemStack(item, lore, new Messaging.MessageFormatter().format(message));
        gameItems.put(materialref, addItem);
    }
    
    private void getSignItems() {
        List<String> lore = new ArrayList<>();
        addItem("blockoffline", lore, "items.skywars-options");
        addItem("blockwaiting", lore, "items.skywars-options");
        addItem("blockplaying", lore, "items.skywars-options"); 
        addItem("blockending", lore, "items.skywars-options"); 
        addItem("almostfull", lore, "items.skywars-options"); 
        addItem("threefull", lore, "items.skywars-options");
        addItem("halffull", lore, "items.skywars-options"); 
        addItem("almostempty", lore, "items.skywars-options"); 
    }
    
    private void getLobbyItem() {
        List<String> lore = new ArrayList<>();
        lore.add(new Messaging.MessageFormatter().format("items.click-to-open"));
        
        addItem("optionselect", lore, "items.skywars-options"); 
        addItem("joinselect", lore, "items.joinmenu");
        addItem("spectateselect", lore, "items.spectatemenu");
		addItem("singlemenu", lore, "items.joinsingle");
		addItem("teammenu", lore, "items.jointeam");
    }

	private void getMatchStartItems() {
        List<String> lore = new ArrayList<>();
        lore.add(new Messaging.MessageFormatter().format("items.click-to-open"));
        
		if (SkyWars.getConfigManager().kitVotingEnabled()) {
	        addItem("kitvote", lore, "items.kit-vote-item");
		} else {
			addItem("kitvote", lore, "items.kit-select-item");
		}
		addItem("votingItem", lore, "items.voting-item");
		
		lore.clear();
		lore.add(new Messaging.MessageFormatter().format("items.lclick-to-open"));
		addItem("chestvote", lore, "items.chest-item");
		addItem("healthvote", lore, "items.health-item");
		addItem("nopermission", lore, "items.no-perm");
		addItem("timevote", lore, "items.time-item");
		addItem("weathervote", lore, "items.weather-item");
		addItem("modifiervote", lore, "items.modifier-item");
	
		lore.clear();
		lore.add(new Messaging.MessageFormatter().format("items.lclick-to-exit"));
		addItem("exitMenuItem", lore, "items.exit-menu-item");
		addItem("nextPageItem", lore, "items.next-page-item");
		addItem("prevPageItem", lore, "items.prev-page-item");
		
		lore.clear();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-exit"));
		addItem("exitGameItem", lore, "items.exit-door-item");
    }
    
    private void getChestVoteItems() {
    	List<String> lore = new ArrayList<>();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-vote"));
		
		addItem("chestrandom", lore, "items.chest-random");
		addItem("chestbasic", lore, "items.chest-basic");
		addItem("chestnormal", lore, "items.chest-normal");
		addItem("chestop", lore, "items.chest-op");
		addItem("chestscavenger", lore, "items.chest-scavenger");
	}
    
    private void getHealthVoteItems() {
    	List<String> lore = new ArrayList<>();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-vote"));
		
		addItem("healthrandom", lore, "items.health-random");
		addItem("healthfive", lore, "items.health-five");
		addItem("healthten", lore, "items.health-ten");
		addItem("healthfifteen", lore, "items.health-fifteen");
		addItem("healthtwenty", lore, "items.health-twenty");
	}
    
    private void getTimeVoteItems() {
    	List<String> lore = new ArrayList<>();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-vote"));
		
		addItem("timerandom", lore, "items.time-random");
		addItem("timedawn", lore, "items.time-dawn");
		addItem("timenoon", lore, "items.time-noon");
		addItem("timedusk", lore, "items.time-dusk");
		addItem("timemidnight", lore, "items.time-midnight");
    }
    
    private void getWeatherVoteItems() {
    	List<String> lore = new ArrayList<>();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-vote"));
		
		addItem("weatherrandom", lore, "items.weather-random");
		addItem("weathersunny", lore, "items.weather-sunny");
		addItem("weatherrain", lore, "items.weather-rain");
		addItem("weatherstorm", lore, "items.weather-storm");
		addItem("weathersnow", lore, "items.weather-snow");
    }

    private void getModifierVoteItems() {
    	List<String> lore = new ArrayList<>();
		lore.add(new Messaging.MessageFormatter().format("items.click-to-vote"));
		
		addItem("modifierrandom", lore, "items.modifier-random");
		addItem("modifierspeed", lore, "items.modifier-speed");
		addItem("modifierjump", lore, "items.modifier-jump");
		addItem("modifierstrength", lore, "items.modifier-strength");
		addItem("modifiernone", lore, "items.modifier-none");
    }
    
    private void getOptionItems() {
        List<String> lore = new ArrayList<>();
        lore.add(new Messaging.MessageFormatter().format("items.lclick-to-open"));
        
        addItem("particleselect", lore, "items.particle-effect-sel");
        addItem("projectileselect", lore, "items.projectile-effect-sel");
        addItem("killsoundselect", lore, "items.killsound-sel");
        addItem("winsoundselect", lore, "items.winsound-sel");
        addItem("glassselect", lore, "items.glass-sel");
        addItem("tauntselect", lore, "items.taunt-sel");
	}
    
	public ItemStack getItem(String item) {
    	return gameItems.get(item).clone();
    }
}