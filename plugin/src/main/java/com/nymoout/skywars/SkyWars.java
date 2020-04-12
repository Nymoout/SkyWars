package com.nymoout.skywars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import com.nymoout.skywars.api.NMS;
import com.nymoout.skywars.commands.PartyCmdManager;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerData;
import com.nymoout.skywars.listeners.*;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.WorldManager;
import com.nymoout.skywars.menus.*;
import com.nymoout.skywars.menus.gameoptions.objects.GameKit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.nymoout.skywars.commands.CmdManager;
import com.nymoout.skywars.commands.KitCmdManager;
import com.nymoout.skywars.commands.MapCmdManager;
import com.nymoout.skywars.config.Config;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.database.Database;
import com.nymoout.skywars.managers.ChestManager;
import com.nymoout.skywars.managers.PlayerOptionsManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.managers.ItemsManager;
import com.nymoout.skywars.managers.Leaderboard;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.holograms.HoloDisUtil;
import com.nymoout.skywars.utilities.holograms.HologramsUtil;
import com.nymoout.skywars.utilities.placeholders.SWMVdWPlaceholder;
import com.nymoout.skywars.utilities.placeholders.SWPlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("UnstableApiUsage")
public class SkyWars extends JavaPlugin implements PluginMessageListener {

	private static SkyWars instance;
	private ArrayList<String> useable = new ArrayList<>();
	private Messaging messaging;
	private Leaderboard leaderboard;
	private IconMenuController ic;
	private ItemsManager im;
	private PlayerOptionsManager pom;
	private Config config;
	private static Database db;
	private ChestManager cm;
	private WorldManager wm;
	private String servername;
	private NMS nmsHandler;
	private HologramsUtil hu;
	private boolean loaded;
	private BukkitTask specObserver;

	
	public void onEnable() {
		loaded = false;
    	instance = this;
    	
    	String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            final Class<?> clazz = Class.forName("com.nymoout.skywars.nms." + version + ".NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (NMS.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.nmsHandler = (NMS) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Could not find support for this CraftBukkit version: " + version + ".");
           //this.getLogger().info("Check for updates at https://www.spigotmc.org/resources/skywarsreloaded.3796/");
            this.setEnabled(false);
            return;
        }
        this.getLogger().info("Loading support for " + version);
    	
    	servername = "none";
    	 
    	if (nmsHandler.getVersion() < 9) {
    		File config = new File(SkyWars.get().getDataFolder(), "config.yml");
            if (!config.exists()) {
            	SkyWars.get().saveResource("config18.yml", false);
            	config = new File(SkyWars.get().getDataFolder(), "config18.yml");
            	if (config.exists()) {
            		boolean result = config.renameTo(new File(SkyWars.get().getDataFolder(), "config.yml"));
            		if (result) {
            		    getLogger().info("Loading 1.8 Configuration Files");
                    }
            	}
            } 
    	} else if (nmsHandler.getVersion() < 13 && nmsHandler.getVersion() > 8) {
			File config = new File(SkyWars.get().getDataFolder(), "config.yml");
			if (!config.exists()) {
				SkyWars.get().saveResource("config112.yml", false);
				config = new File(SkyWars.get().getDataFolder(), "config112.yml");
				if (config.exists()) {
					boolean result = config.renameTo(new File(SkyWars.get().getDataFolder(), "config.yml"));
					if (result) {
						getLogger().info("Loading 1.9 - 1.12 Configuration Files");
					}
				}
			}
		}
    	
    	getConfig().options().copyDefaults(true);
    	saveDefaultConfig();
        saveConfig();
        reloadConfig();

        config = new Config();   
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	new SWPlaceholderAPI(this);
        }
        
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
        	new SWMVdWPlaceholder(this);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PerWorldInventory")) {
			this.getServer().getPluginManager().registerEvents(new PerWorldInventoryListener(), this);
		}

        ic = new IconMenuController();
        wm = new WorldManager();
        
        if (nmsHandler.getVersion() > 8) {
        	this.getServer().getPluginManager().registerEvents(new SwapHandListener(), this);
        }
        this.getServer().getPluginManager().registerEvents(ic, this);
        this.getServer().getPluginManager().registerEvents(new ArenaDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        this.getServer().getPluginManager().registerEvents(new LobbyListener(), this);
        this.getServer().getPluginManager().registerEvents(new SpectateListener(), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		this.getServer().getPluginManager().registerEvents(new ProjectileSpleefListener(), this);
		if (SkyWars.getConfigManager().hologramsEnabled()) {
			hu = null;
        	if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
        		hu = new HoloDisUtil();
        		hu.load();
        	}
        	if (hu == null) {
        		config.setHologramsEnabled(false);
        		config.save();
        	}
		}
        
        load();
        if (SkyWars.getConfigManager().tauntsEnabled()) {
            this.getServer().getPluginManager().registerEvents(new TauntListener(), this);
        }
        if (SkyWars.getConfigManager().particlesEnabled()) {
        	this.getServer().getPluginManager().registerEvents(new com.nymoout.skywars.listeners.ParticleEffectListener(), this);
        }
        if (config.disableCommands()) {
            this.getServer().getPluginManager().registerEvents(new PlayerCommandPrepocessListener(), this);
        }
        
    	if (getConfigManager().bungeeMode()) {
        	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        	this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    		Bukkit.getPluginManager().registerEvents(new PingListener(), this);
    	}
        if (getConfigManager().bungeeMode()) {
            new BukkitRunnable() {
                public void run() {
                	if (servername.equalsIgnoreCase("none")) {
                    	Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                    	if (player != null) {
                        	sendBungeeMsg(player, "GetServer", "none");
                    	}
                	} else {
                		this.cancel();
                	}
                }
            }.runTaskTimer(this, 0, 20);
        }
	}

	public void onDisable() {
		loaded = false;
        this.getServer().getScheduler().cancelTasks(this);
        for (final GameMap gameMap : GameMap.getMaps()) {
        	if (gameMap.isEditing()) {
        		gameMap.saveMap(null);
        	}
        	for (final UUID uuid: gameMap.getSpectators()) {
        		final Player player = getServer().getPlayer(uuid);
        		if (player != null) {
        			MatchManager.get().removeSpectator(player);
        		}
        	}
            for (final Player player : gameMap.getAlivePlayers()) {
            	if (player != null) {
                    MatchManager.get().playerLeave(player, DamageCause.CUSTOM, true, false, true);
            	}
            }
            getWorldManager().deleteWorld(gameMap.getName());
        }
        for (final PlayerData playerData : PlayerData.getPlayerData()) {
            playerData.restore(false);
        }
        PlayerData.getPlayerData().clear();
        for (final PlayerStat fData : PlayerStat.getPlayers()) {
        	DataStorage.get().saveStats(fData);
        }    
	}
	
	public void load() {
		messaging = null;
		messaging = new Messaging(this);
		reloadConfig();
		config.load();
        cm = new ChestManager();
        im = new ItemsManager();
        pom = new PlayerOptionsManager();
        GameKit.loadkits();
        GameMap.loadMaps();
        
        boolean sqlEnabled = getConfig().getBoolean("sqldatabase.enabled");
        if (sqlEnabled) {
        	getFWDatabase();
        }
        useable.clear();
        
        for (LeaderType type: LeaderType.values()) {
			if (SkyWars.getConfigManager().isTypeEnabled(type)) {
				useable.add(type.toString());
			}
        }
        
        new BukkitRunnable() {
            public void run() {
                for (final Player v : getServer().getOnlinePlayers()) {
                    if (PlayerStat.getPlayerStats(v.getUniqueId().toString()) == null) {
                        PlayerStat.getPlayers().add(new PlayerStat(v));
                    }
                }
                leaderboard = null;
                leaderboard = new Leaderboard();
            }
        }.runTaskAsynchronously(this);
        
        if (SkyWars.getConfigManager().economyEnabled()) {
    		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
    			SkyWars.getConfigManager().setEconomyEnabled(false);
            }
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                SkyWars.getConfigManager().setEconomyEnabled(false);
            }
        }

        if (SkyWars.getConfigManager().joinMenuEnabled() || SkyWars.getConfigManager().spectateMenuEnabled()) {
        	new JoinMenu();
	        new JoinSingleMenu();
			new JoinTeamMenu();
        }
        if (SkyWars.getConfigManager().spectateMenuEnabled()) {
        	new SpectateMenu();
	        new SpectateSingleMenu();
            new SpectateTeamMenu();
        }
        
        getCommand("skywars").setExecutor(new CmdManager());
        getCommand("swkit").setExecutor(new KitCmdManager());
        getCommand("swmap").setExecutor(new MapCmdManager());
        if (config.partyEnabled()) {
            getCommand("party").setExecutor(new PartyCmdManager());
        }
        if (getConfigManager().borderEnabled()) {
        	if (specObserver != null) {
				specObserver.cancel();
			}
			specObserver = new BukkitRunnable() {
				@Override
				public void run() {
					for (GameMap gMap : GameMap.getMaps()) {
						gMap.checkSpectators();
					}
				}
			}.runTaskTimer(SkyWars.get(), 0, 40);
		}
        loaded = true;
	}
	
	public static SkyWars get() {
        return instance;
    }
	
	public static Messaging getMessaging() {
	     return instance.messaging;
	}
	
	public static Leaderboard getLB() {
	     return instance.leaderboard;
	}
	
    public static Config getConfigManager() {
    	return instance.config;
    }
    
    public static IconMenuController getIconMenuController() {
    	return instance.ic;
    }
    
    public static WorldManager getWorldManager() {
    	return instance.wm;
    }
    
    public static Database getDatabase() {
    	return db;
    }
           
    private void getFWDatabase() {
    	try {
			db = new Database();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	try {
			db.createTables();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
    }

	public static ChestManager getChestManager() {
		return instance.cm;
	}
	
	public static ItemsManager getItemsManager() {
		return instance.im;
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
    
	    if (subchannel.equals("GetServer")) {
	    	servername = in.readUTF();
	    }
	    
	    if (subchannel.equals("SWRMessaging")) {
	    	short len = in.readShort();
	    	byte[] msgbytes = new byte[len];
	    	in.readFully(msgbytes);

	    	DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
	    	try {
				String header = msgin.readUTF();
				if (header.equalsIgnoreCase("RequestUpdate")) {
					String sendToServer = msgin.readUTF();
					String playerCount = "" + GameMap.getMaps().get(0).getAlivePlayers().size();
					String maxPlayers = "" + GameMap.getMaps().get(0).getMaxPlayers();
					String gameStarted = "" + GameMap.getMaps().get(0).getMatchState().toString();
					ArrayList<String> messages = new ArrayList<>();
					messages.add("ServerUpdate");
					messages.add(servername);
					messages.add(playerCount);
					messages.add(maxPlayers);
					messages.add(gameStarted);
					sendSWRMessage(player, sendToServer, messages);					
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    }
	}

	public void sendBungeeMsg(Player player, String subchannel, String message) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(subchannel);
		if (!message.equalsIgnoreCase("none")) {
			out.writeUTF(message);
		}
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
	
	public void sendSWRMessage(Player player, String server, ArrayList<String> messages) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward"); 
		out.writeUTF(server);
		out.writeUTF("SWRMessaging");

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			for (String msg: messages) {
				msgout.writeUTF(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
	
	public String getServerName() {
		return servername;
	}
	
    public static NMS getNMS() {
    	return instance.nmsHandler;
    }
    
    public ArrayList<String> getUseable() {
    	return useable;
    }

	public PlayerStat getPlayerStat(Player player) {
		return PlayerStat.getPlayerStats(player);
	}

	public boolean serverLoaded() {
		return loaded;
	}
	
	public static HologramsUtil getHoloManager() {
		return instance.hu;
	}
	
	public static PlayerOptionsManager getPlayerOptionsManager() {
		return instance.pom;
	}
    
}
