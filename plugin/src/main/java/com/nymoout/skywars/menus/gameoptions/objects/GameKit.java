package com.nymoout.skywars.menus.gameoptions.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class GameKit {

	private static ArrayList<GameKit> kits = new ArrayList<>();
	private ItemStack[] inventory;
	private ItemStack[] armor;
	private ItemStack icon;
	private ItemStack lIcon;
	private String name;
	private String filename;
	private int position;
	private int page;
	private Map<Integer, String> lores = Maps.newHashMap();
	private String lockedLore;
	private boolean enabled;
	private boolean requirePermission;

    @SuppressWarnings("unchecked")
	public GameKit(File kitFile) {
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        List<ItemStack> inventoryItems = (List<ItemStack>) storage.getList("inventory");
        if (inventoryItems != null) {
			inventory = inventoryItems.toArray(new ItemStack[0]);
		}

        List<ItemStack> armorItems = (List<ItemStack>) storage.getList("armor");

        if (armorItems != null) {
			armor = armorItems.toArray(new ItemStack[0]);
		}

        icon = storage.getItemStack("icon");
        
        lIcon = storage.getItemStack("lockedIcon");
        
        name = storage.getString("name");
        
        position = storage.getInt("position");
        
        page = storage.getInt("page");
        
        if (page == 0) {
        	page = 1;
        	storage.set("page", page);
        }
        
        for (int x = 1; x < 17; x++) {
        	lores.put(x, storage.getString("lores.line" + x, " "));
        } 
        
        lockedLore = storage.getString("lores.locked", "");
        
        enabled = storage.getBoolean("enabled");
        
        requirePermission = storage.getBoolean("requirePermission");
        
        filename = storage.getString("filename");
                
        try {
        	storage.save(kitFile);
		} catch (IOException e) {
        	SkyWars.get().getLogger().info("Failed to save kit file!");
		}
	}
	
	private GameKit(String fnam, String nam, int pos, int pag, ItemStack ico, String lore) {
        inventory = new ItemStack[41];
        armor = new ItemStack[4];
        icon = ico;    
        lIcon = new ItemStack(Material.BARRIER, 1);
        name = nam;
        filename = fnam;
        position = pos;
        page = pag;
       	lores.put(1, lore);
        for (int x = 2; x < 17; x++) {
        	lores.put(x, " ");
        } 
        lockedLore = "";
        enabled = true;
        requirePermission = false;
	}
		
    private ItemStack[] getArmor() {
		return armor;
	}
    
    public void setArmor(ItemStack[] items) {
    	this.armor = items.clone();
    }
    
    public void setInventory(ItemStack[] inv) {
    	this.inventory = inv.clone();
    }

	private ItemStack[] getInventory() {
		return inventory;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public void setIcon(ItemStack item) {
		this.icon = item;
	}
	
	public ItemStack getLIcon() {
		return lIcon;
	}
	
	public void setLIcon(ItemStack item) {
		this.lIcon = item;
	}
	
	public String getColorName() {
		return ChatColor.translateAlternateColorCodes('&', name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String string) {
		this.name = string;
	}
    
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int i) {
		this.position = i;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int i) {
		this.page = i;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public boolean needPermission() {
		return requirePermission;
	}
	
	public void setNeedPermission(boolean state) {
		this.requirePermission = state;
	}
	
	public void setEnabled(boolean state) {
		enabled = state;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public List<String> getColorLores() {
		List<String> colorLores = new ArrayList<>();
		int spaces = 0;
		for (int x = 1; x < 17; x++ ) {
			if (lores.get(x).equals(" ")) {
				spaces++;
			} else {
				if (spaces > 0) {
					for (int y = 1; y <= spaces; y++) {
						colorLores.add(" ");
					}
					spaces = 0;
				}
				colorLores.add(ChatColor.translateAlternateColorCodes('&', lores.get(x)));
			}
		}
		return colorLores;
	}
	
	private Map<Integer, String> getLores() {
		return this.lores;
	}
	
	public String getColoredLockedLore() {
		return ChatColor.translateAlternateColorCodes('&', lockedLore);
	}
	
	private String getLockedLore() {
		return lockedLore;
	}
	
	public void setLoreLine(int line, String lore) {
		this.lores.put(line, lore);
	}
	
	public void setLockedLore(String lore) {
		this.lockedLore = lore;
	}
	
	//STATIC METHODS
	public static ArrayList<GameKit> getKits() {
        return GameKit.kits;
    }
	
	public static GameKit getKit(String filename) {
		for (GameKit kit: GameKit.getKits()) {
			if (kit.getFilename().equalsIgnoreCase(filename) || kit.getColorName().equals(filename) || kit.getColorName().equals(ChatColor.translateAlternateColorCodes('&', filename))) {
				return kit;
			}
		}
		return null;
	}
	
	public static void giveKit(Player player, GameKit kit) {
	       player.getInventory().clear();
	       if (kit != null) {
		        for (int i = 0; i < 36; i++) {
		        	if (kit.getInventory()[i] != null) {
		        		player.getInventory().setItem(i, kit.getInventory()[i]);
		        	}
		        }
		              
		        player.getInventory().setArmorContents(kit.getArmor());
		        
		    	if (SkyWars.getConfigManager().debugEnabled()) {
		    		Util.get().logToFile(ChatColor.RED + "[skywars] " + ChatColor.YELLOW + player.getName() + " has recieved kit " + kit.getColorName());
		    	}
	       }
		}
	
	public static void newKit(Player player, String kitName) {
		File dataDirectory = SkyWars.get().getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");
    	if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs())  {
                return;
            }
        }
        
    	File kitFile = new File(kitsDirectory, kitName + ".yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        ItemStack[] inventory = player.getInventory().getContents();
        storage.set("inventory", inventory);
        
        ItemStack[] armor = player.getInventory().getArmorContents();
        storage.set("armor",  armor);
        
        storage.set("requirePermission", false);
        
        storage.set("icon", new ItemStack(Material.SNOW_BLOCK, 1));
        
        storage.set("lockedIcon", new ItemStack(Material.BARRIER, 1));
        
        storage.set("position", 0);
        
        storage.set("page", 1);
        
        storage.set("name", kitName);
        
        storage.set("enabled", false);
        
        for (int x = 1; x < 17; x++) {
        	storage.set("lores.line" + x, " ");
        }
        storage.set("lores.locked", "&CPermission required to unlock this kit!");
        
        storage.set("gameSettings.noRegen", false);
        storage.set("gameSettings.noPvp", false);
        storage.set("gameSettings.soupPvp", false);
        storage.set("gameSettings.noFallDamage", false);
        
        storage.set("filename", kitFile.getName().substring(0, kitFile.getName().lastIndexOf('.')));
        
        try {
        	storage.save(kitFile);
		} catch (IOException e) {
            SkyWars.get().getLogger().info("Failed to save new kit file!");
		}
        GameKit.getKits().add(new GameKit(kitFile));
    }
	
	public static void saveKit(GameKit kit) {
		File dataDirectory = SkyWars.get().getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");
    	if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs())  {
                return;
            }
        }
        
    	File kitFile = new File(kitsDirectory, kit.getFilename() + ".yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        storage.set("inventory", kit.getInventory());
        
        storage.set("armor",  kit.getArmor());
        
        storage.set("requirePermission", kit.needPermission());
        
        storage.set("icon", kit.getIcon());
        storage.set("lockedIcon", kit.getLIcon());
        
        storage.set("position", kit.getPosition());
        
        storage.set("page", kit.getPage());
        
        storage.set("name", kit.getName());
        
        storage.set("enabled", kit.getEnabled());
        
        for (int x = 1; x < 17; x++) {
        	storage.set("lores.line" + x, kit.getLores().get(x));
        }

        storage.set("lores.locked", kit.getLockedLore());
        
        storage.set("filename", kit.getFilename());
        
        try {
        	storage.save(kitFile);
		} catch (IOException e) {
            SkyWars.get().getLogger().info("Failed to save kit file!");
		}
    }

	public static void loadkits() {
    	kits.clear();
    	if (SkyWars.getConfigManager().kitVotingEnabled()) {
        	kits.add(new GameKit("rand",
        			new Messaging.MessageFormatter().format("kit.vote-random"), 
        			SkyWars.getConfigManager().getRandPos(), 1,
        			new ItemStack(SkyWars.getConfigManager().getRandMat(), 1),
        			new Messaging.MessageFormatter().format("kit.rand-lore")));
        	kits.add(new GameKit("nokit",
        			new Messaging.MessageFormatter().format("kit.vote-nokit"), 
        			SkyWars.getConfigManager().getNoKitPos(), 1,
        			new ItemStack(SkyWars.getConfigManager().getNoKitMat(), 1),
        			new Messaging.MessageFormatter().format("kit.nokit-lore")));
    	}
        File dataDirectory = SkyWars.get().getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");

        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs())  {
                return;
            }
        }

        File[] kitsFiles = kitsDirectory.listFiles();
        if (kitsFiles == null) {
            return;
        }

        for (File kitFile : kitsFiles) {
            if (!kitFile.getName().endsWith(".yml")) {
                continue;
            }

            String name = kitFile.getName().replace(".yml", "");

            if (!name.isEmpty()) {
            	kits.add(new GameKit(kitFile));
            }
        }
    }

	public static ArrayList<GameKit> getAvailableKits() {
		ArrayList<GameKit> availableKits = new ArrayList<>();
		for (GameKit kit: GameKit.getKits()) {
			if (kit.enabled) {
				availableKits.add(kit);
			}
		}
		return availableKits;
	}

}

