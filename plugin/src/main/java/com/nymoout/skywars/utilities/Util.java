package com.nymoout.skywars.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.menus.gameoptions.objects.CoordLoc;

public class Util {

	private static Util instance;
	private Random rand;
	
	public static Util get() {
        if (Util.instance == null) {
            Util.instance = new Util();
        }
        return Util.instance;
	}
	
	public Util() {
		rand = new Random();
	}
	
	public int getRandomNum(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public boolean hasPermissions(String t, CommandSender sender, String s) {
		if (t.equalsIgnoreCase("sw")) {
			return sender.hasPermission("sw." + s);
		} else if (t.equalsIgnoreCase("kit")) {
			return sender.hasPermission("sw.kit." + s);
		} else if (t.equalsIgnoreCase("map")) {
			return sender.hasPermission("sw.map." + s);
		} else if (t.equalsIgnoreCase("party")) {
			return sender.hasPermission("sw.party." + s);
		}
		return false;
	}
	
	public String getMessageKey(String type) {
		if (type.equalsIgnoreCase("sw")) {
				return "sw";
		} else if (type.equalsIgnoreCase("kit")) {
				return "swkit";
		} else if (type.equalsIgnoreCase("map")) {
				return "swmap";
		} else if (type.equalsIgnoreCase("party")) {
				return "swparty";
		}
		return "";
	}
	
	public void playSound(Player player, Location location, String sound, float volume, float pitch) {
		if (SkyWars.getConfigManager().soundsEnabled()) {
			try {
				if (player != null) {
					player.playSound(location, Sound.valueOf(sound), volume, pitch);
				}
			} catch (IllegalArgumentException | NullPointerException e) {
				SkyWars.get().getLogger().info("ERROR: " + sound + " is not a valid bukkit sound. Please check your configs");
			}
		}
	}
	
	public int getMultiplier(Player player) {
		if (player.hasPermission("sw.vip5")) {
			return SkyWars.getConfigManager().getVip5();
		} else if (player.hasPermission("sw.vip4")) {
			return SkyWars.getConfigManager().getVip4();
		} else if (player.hasPermission("sw.vip3")) {
			return SkyWars.getConfigManager().getVip3();
		} else if (player.hasPermission("sw.vip2")) {
			return SkyWars.getConfigManager().getVip2();
		} else if (player.hasPermission("sw.vip1")) {
			return SkyWars.getConfigManager().getVip1();
		} else {
			return 1;
		}
	}
	
	public void doCommands(List<String> commandList, Player player) {
		for (String com: commandList) {
			String command = com;
			if (player != null) {
				command = com.replace("<player>", player.getName());
			}
			SkyWars.get().getServer().dispatchCommand(SkyWars.get().getServer().getConsoleSender(), command);
		}
	}
	
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
	
	public boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
	
    public Location stringToLocation(final String location) {
    	if (location != null) {
        	final String[] locationParts = location.split(":");
            if (locationParts.length != 6) {
            	return null;
            } else {
            	 return new Location(SkyWars.get().getServer().getWorld(locationParts[0]), Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]), Float.parseFloat(locationParts[4]), Float.parseFloat(locationParts[5]));
            }
    	}
    	return null;
    }
    
    public String locationToString(final Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
    	SkyWars.getNMS().sendTitle(player, fadein, stay, fadeout, title, subtitle);
	}
    
    public void clear(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        for (final PotionEffect a1 : player.getActivePotionEffects()) {
        	player.removePotionEffect(a1.getType());
        }
    }
    
    public boolean isBusy(UUID uuid) {
        Player player = SkyWars.get().getServer().getPlayer(uuid);

        if (player == null) {
        	return true;
        }
        
        if (player.isDead()) {
        	return true;
        }
        
    	if (MatchManager.get().isSpectating(player)) {
    		return true;
    	}
    	
    	boolean allowed = false;
    	for (String world: SkyWars.getConfigManager().getLobbyWorlds()) {
    		if (world.equalsIgnoreCase(player.getWorld().getName())) {
    			allowed = true;
    		}
    	}

    	if (!allowed) {
    		return true;
    	}
        
		PlayerStat ps = PlayerStat.getPlayerStats(player);
		if (ps == null) {
			PlayerStat.getPlayers().add(new PlayerStat(player));
			return true;
		} else return !ps.isInitialized();
    }
    
    public void fireworks(final Player player, final int length, final int fireworksPer5Tick) {
        final List<FireworkEffect.Type> type = new ArrayList<>(Arrays.asList(FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BURST, FireworkEffect.Type.STAR, FireworkEffect.Type.CREEPER));
        final List<Color> colors = new ArrayList<>(Arrays.asList(Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW));
        final long currentTime = System.currentTimeMillis();
        Random rand = new Random();
        if (SkyWars.get().isEnabled()) {
            new BukkitRunnable() {
                public void run() {
                    if (System.currentTimeMillis() >= currentTime + length * 1000 || SkyWars.get().getServer().getPlayer(player.getUniqueId()) == null) {
                        this.cancel();
                    }
                    else {
                        for (int i = 0; i < fireworksPer5Tick; ++i) {
                            final Location loc = player.getLocation();
                            @SuppressWarnings({ "unchecked", "rawtypes" })
							final Firework firework = (Firework)player.getLocation().getWorld().spawn(loc, (Class)Firework.class);
                            final FireworkMeta fMeta = firework.getFireworkMeta();
                            FireworkEffect fe = FireworkEffect.builder().withColor(colors.get(rand.nextInt(17))).withColor(colors.get(rand.nextInt(17)))
                                    .withColor(colors.get(rand.nextInt(17))).with(type.get(rand.nextInt(5))).trail(rand.nextBoolean())
                                    .flicker(rand.nextBoolean()).build();
                            fMeta.addEffects(fe);
                            fMeta.setPower(new Random().nextInt(2) + 2);
                            firework.setFireworkMeta(fMeta);
                        }
                    }
                }
            }.runTaskTimer(SkyWars.get(), 0L, 5L);
        }
    }
    
	public void sendParticles(final org.bukkit.World world, final String type, final float x, final float y, final float z, final float offsetX, final float offsetY, final float offsetZ, final float data, final int amount) {
		if (SkyWars.get().isEnabled()) {
			new BukkitRunnable() {
				public void run() {
					SkyWars.getNMS().sendParticles(world, type, x, y, z, offsetX, offsetY, offsetZ, data, amount);
				}
			}.runTaskAsynchronously(SkyWars.get());
    	}
	}
    
    private List<Block> getBlocks(Location center, int radius) {
        List<Location> locs = circle(center, radius);
        List<Block> blocks = new ArrayList<>();
 
        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }
 
        return blocks;
    }
 
    private List<Location> circle(Location loc, int radius) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
 
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (cy - radius); y < (cy + radius); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + ((cy - y) * (cy - y));
                    if (dist < radius * radius
                            && !(dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
 
        return circleblocks;
    }
 
    public void surroundParticles(Player player, int r, final List<String> types, final int density, final double speed){
    	if (SkyWars.get().isEnabled()) {
        	if (player != null) {
    	    	Location l = player.getLocation().add(0, 1, 0);
    			final Random random = new Random();
    	    	for (final Block b : getBlocks(l, r)) {
    	    		new BukkitRunnable() {
    					@Override
    					public void run() {
    						sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density/2) + density/2) + 1);
    						sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density/2) + density/2) + 1);
    						sendParticles(b.getWorld(), types.get(random.nextInt(types.size())), (float) b.getLocation().getX(), (float) b.getLocation().getY(), (float) b.getLocation().getZ(), random.nextFloat(), random.nextFloat(), random.nextFloat(), (float) speed, random.nextInt((density - density/2) + density/2) + 1);}
    	    		}.runTaskAsynchronously(SkyWars.get());
    	    	}
    		}
    	}
    }
    
	public void respawnPlayer(Player player) {
		SkyWars.getNMS().respawnPlayer(player);
	}
	
	public String getDeathMessage(DamageCause dCause, boolean withHelp, Player target, Player killer) {
		String first;
		String second = new Messaging.MessageFormatter()
						.setVariable("killer", killer.getName())
						.format("game.death.killer-section");
		
		if (dCause.equals(DamageCause.BLOCK_EXPLOSION) || dCause.equals(DamageCause.ENTITY_EXPLOSION)) {
			first = new Messaging.MessageFormatter()
					.withPrefix()
					.setVariable("player", target.getName())
					.format("game.death.explosion");
		} else if (dCause.equals(DamageCause.DROWNING)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.drowning");
		} else if (dCause.equals(DamageCause.FIRE) || dCause.equals(DamageCause.FIRE_TICK)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.fire");
		} else if (dCause.equals(DamageCause.ENTITY_ATTACK)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.setVariable("killer", killer.getName())
			.format("game.death.pvp");
			second = "";
		} else if (dCause.equals(DamageCause.FALLING_BLOCK)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.falling-block");
		} else if (dCause.equals(DamageCause.LAVA)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.lava");
		} else if (dCause.equals(DamageCause.PROJECTILE)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.setVariable("killer", killer.getName())
			.format("game.death.projectile");
			second = "";
		} else if (dCause.equals(DamageCause.SUFFOCATION)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.suffocation");
		} else if (dCause.equals(DamageCause.VOID)) {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.void");
		} else {
			first = new Messaging.MessageFormatter()
			.withPrefix()
			.setVariable("player", target.getName())
			.format("game.death.general");
		}
		
		if (withHelp) {
			return first + second;
		} else {
			return first + "!";
		}
	}
	
    public List<Chunk> getChunks(World mapWorld) {
		int size = 400;
		int maxX = size/2;
		int minX = -size/2;
		int maxZ = size/2;
		int minZ = -size/2;
		int minY = 0;
		int maxY = 0;
		Block min = mapWorld.getBlockAt(minX, minY, minZ);
		Block max = mapWorld.getBlockAt(maxX, maxY, maxZ);
		Chunk cMin = min.getChunk();
		Chunk cMax = max.getChunk();
		List<Chunk> chunks = new ArrayList<>();
		
		for(int cx = cMin.getX(); cx < cMax.getX(); cx++) {
			for(int cz = cMin.getZ(); cz < cMax.getZ(); cz++) {
		           Chunk currentChunk = mapWorld.getChunkAt(cx, cz);
		           chunks.add(currentChunk);
			}
		}
		return chunks;
    }
	
	public ItemStack name(ItemStack itemStack, String name, String... lores) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!name.isEmpty()) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        if (lores.length > 0) {
            List<String> loreList = new ArrayList<>(lores.length);

            for (String lore : lores) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
            }

            itemMeta.setLore(loreList);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public void logToFile(String message) {
    	ConsoleCommandSender console = SkyWars.get().getServer().getConsoleSender();
    	console.sendMessage(message);

        try {
            File dataFolder = SkyWars.get().getDataFolder();
            if (dataFolder.exists() || dataFolder.mkdir()) {
                File saveTo = new File(dataFolder, "DebugLog.txt");
                if (saveTo.exists() || saveTo.createNewFile()) {
                    FileWriter fw = new FileWriter(saveTo, true);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(ChatColor.stripColor(message));
                    pw.flush();
                    pw.close();
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void sendActionBar(Player p, String msg) {
    	SkyWars.getNMS().sendActionBar(p, msg);
    }
    
	public byte getByteFromColor(String color) {
		switch (color) {
			case "white": return (byte) 0;
			case "orange": return (byte) 1;
			case "magenta": return (byte) 2;
			case "lightblue": return (byte) 3;
			case "yellow": return (byte) 4;
			case "lime": return (byte) 5;
			case "pink": return (byte) 6;
			case "gray": return (byte) 7;
			case "lightgray": return (byte) 8;
			case "cyan": return (byte) 9;
			case "purple": return (byte) 10;
			case "blue": return (byte) 11;
			case "brown": return (byte) 12;
			case "green": return (byte) 13;
			case "red": return (byte) 14;
			case "black": return (byte) 15;
			case "none": return (byte) -2;
			case "lapis": return (byte) -3;
			case "redstone": return (byte) -4;
			case "emerald": return (byte) -5;
			case "diamond": return (byte) -6;
			default: return (byte) -1;
		}
	}

    public String formatScore(int score) {
        char color = '7';

        if (score > 0) {
            color = 'a';
        } else if (score < 0) {
            color = 'c';
        }

        return "\247" + color + "(" + (score > 0 ? "+" : "") + score + " Elo" + ")";
    }
	
	public void setPlayerExperience(Player player, int amount) {
		if (amount <= 352) {
			int level = (int) Math.floor(quadraticEquationRoot(1, 6, 0-amount));
			double nextLevel = 2 * level + 7;
			double levelExp = (level * level) + 6 * level;
			double leftOver = amount - levelExp;
			player.setLevel(level);
			player.setExp((float) (leftOver/nextLevel));
		} else if (amount <= 1507) {
			int level = (int) Math.floor(quadraticEquationRoot(2.5, -40.5, 360-amount));
			double nextLevel = 5 * level - 38;
			double levelExp = (int) (2.5 * (level * level) - 40.5 * level + 360);
			double leftOver = amount - levelExp;
			player.setLevel(level);
			player.setExp((float) (leftOver/nextLevel));
		} else {
			int level = (int) Math.floor(quadraticEquationRoot(4.5, -162.5, 2220-amount));
			double nextLevel = 9 * level - 158;
			double levelExp = (int) (4.5 * (level * level) - 162.5 * level + 2220);
			double leftOver = amount - levelExp;
			player.setLevel(level);
			player.setExp((float) (leftOver/nextLevel));
		}
	}
	
	public int getPlayerLevel(Player player) {
	    if (SkyWars.getConfigManager().displayPlayerExeperience()) {
	    	return player.getLevel();
	    } else {
	    	PlayerStat ps = PlayerStat.getPlayerStats(player);
	    	if (ps != null) {
	    		int amount = ps.getXp();
	    		if (amount <= 352) {
	    			return (int) Math.floor(quadraticEquationRoot(1, 6, 0-amount));
	    		} else if (amount <= 1507) {
	    			return (int) Math.floor(quadraticEquationRoot(2.5, -40.5, 360-amount));
	    		} else {
	    			return (int) Math.floor(quadraticEquationRoot(4.5, -162.5, 2220-amount));
	    		}
	    	} else {
	        	return 0;
	    	}
	    }
	}
		
	private static double quadraticEquationRoot(double a, double b, double c) {
	    double root1, root2;
	    root1 = (-b + Math.sqrt(Math.pow(b, 2) - 4*a*c)) / (2*a);
	    root2 = (-b - Math.sqrt(Math.pow(b, 2) - 4*a*c)) / (2*a);
	    return Math.max(root1, root2);  
	}
	
	public String getFormattedTime(int x) {
		String hms;
		if (x >= 3600) {
			hms = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(x),
				    TimeUnit.SECONDS.toMinutes(x) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.SECONDS.toSeconds(x) % TimeUnit.MINUTES.toSeconds(1));
		} else {
			hms = String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(x) % TimeUnit.HOURS.toMinutes(1),
				    TimeUnit.SECONDS.toSeconds(x) % TimeUnit.MINUTES.toSeconds(1));
		}
		return hms;
	}

	public boolean isSpawnWorld(World world) {
        return SkyWars.getConfigManager().getSpawn() != null && world.equals(SkyWars.getConfigManager().getSpawn().getWorld());
    }

	public CoordLoc getCoordLocFromString(String location) {
		if (location != null) {
        	final String[] locationParts = location.split(":");
            if (locationParts.length != 3) {
            	return null;
            } else {
            	 return new CoordLoc(Integer.valueOf(locationParts[0]), Integer.valueOf(locationParts[1]),Integer.valueOf(locationParts[2]));   
            }
    	}
    	return null;
	}


}
