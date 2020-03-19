package com.nymoout.skywars.managers;

import com.google.common.collect.Maps;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.objects.GlassColor;
import com.nymoout.skywars.objects.ParticleEffect;
import com.nymoout.skywars.objects.ParticleItem;
import com.nymoout.skywars.objects.SoundItem;
import com.nymoout.skywars.objects.Taunt;
import com.nymoout.skywars.utilities.Util;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LevelManager {

    private final ArrayList<GlassColor> colorList = new ArrayList<GlassColor>();
    private final ArrayList<Taunt> tauntList = new ArrayList<Taunt>();
	private final Map<Projectile, List<ParticleEffect>> projectileMap = Maps.newConcurrentMap();
	private final Map<UUID, List<ParticleEffect>> playerMap = Maps.newConcurrentMap();
    private final ArrayList<ParticleItem> particleList = new ArrayList<ParticleItem>();
	private final ArrayList<ParticleItem> projEffectList = new ArrayList<ParticleItem>();
	private final ArrayList<SoundItem> killSoundList = new ArrayList<SoundItem>();
	private final ArrayList<SoundItem> winSoundList = new ArrayList<SoundItem>();

    public LevelManager() {
        loadGlassColors();
        loadParticleEffects();
        loadProjEffects();
        loadKillSounds();
        loadWinSounds();
        loadTaunts();
    }
    
	/*Glass color methods*/
    
    public void loadGlassColors() {
        colorList.clear();
        File glassFile = new File(SkyWars.get().getDataFolder(), "glasscolors.yml");

        if (!glassFile.exists()) {
        	SkyWars.get().saveResource("glasscolors.yml", false);
        }

        if (glassFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(glassFile);

            if (storage.getConfigurationSection("colors") != null) {
            	for (String key: storage.getConfigurationSection("colors").getKeys(false)) {
            		String color = key;
                	String name = storage.getString("colors." + key + ".displayname");
                	String material = storage.getString("colors." + key + ".material");
                	int level = storage.getInt("colors." + key + ".level");
                	int cost = storage.getInt("colors." + key + ".cost");
                	int data = storage.getInt("colors." + key + ".datavalue");
                	                	
                	Material mat = Material.matchMaterial(material);
                	if (mat != null) {
                		ItemStack itemStack = null;
                		if (data == -1) {
                			itemStack = new ItemStack(mat, 1);
                		} else {
                			itemStack = new ItemStack(mat, 1, (short) data);
                		}
                		
                        if (itemStack != null) {
                            colorList.add(new GlassColor(color, name, itemStack, level, cost));
                        }
                	}
            	}
            }
        }
        
        Collections.<GlassColor>sort(colorList);
    }
       
    public GlassColor getGlassByName(String color) {
    	for (GlassColor glassColor: colorList) {
    		if (glassColor.getName().equalsIgnoreCase(color)) {
    			return glassColor;
    		}
    	}
        return null;
    }
    
    public GlassColor getGlassByColor(String color) {
    	for (GlassColor glassColor: colorList) {
    		if (glassColor.getKey().equalsIgnoreCase(color)) {
    			return glassColor;
    		}
    	}
        return null;
    }
    
    public ArrayList<GlassColor> getColorItems() {
    	return colorList;
    }
    
    /*Particle Effect Methods*/
    
    public void loadParticleEffects() {
        particleList.clear();
        File particleFile = new File(SkyWars.get().getDataFolder(), "particleeffects.yml");

        if (!particleFile.exists()) {
        	SkyWars.get().saveResource("particleeffects.yml", false);
        }

        if (particleFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(particleFile);

            if (storage.getConfigurationSection("effects") != null) {
            	for (String key: storage.getConfigurationSection("effects").getKeys(false)) {
                	String name = storage.getString("effects." + key + ".displayname");
                	String material = storage.getString("effects." + key + ".icon");
                	int level = storage.getInt("effects." + key + ".level");
                	int cost = storage.getInt("effects." + key + ".cost");
                	List<String> particles = storage.getStringList("effects." + key + ".particles");
                	
                	List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
                	if (particles != null) {
                    	for (String part: particles) {
                    		final String[] parts = part.split(":");
                            if (parts.length == 6 
                            		&& SkyWars.getNMS().isValueParticle(parts[0].toUpperCase())
                            		&& Util.get().isFloat(parts[1])
                    				&& Util.get().isFloat(parts[2])
                    				&& Util.get().isFloat(parts[3])
                            		&& Util.get().isInteger(parts[4]) 
                            		&& Util.get().isInteger(parts[5])) {
                            	effects.add(new ParticleEffect(parts[0].toUpperCase(), Float.valueOf(parts[1]), Float.valueOf(parts[2]), Float.valueOf(parts[3]), Integer.valueOf(parts[4]), Integer.valueOf(parts[5])));
                            } else {
                            	SkyWars.get().getLogger().info("The particle effect " + key + " has an invalid particle effect");
                            }
                    	}
                	}
                	Material mat = Material.matchMaterial(material);
                	if (mat != null) {
                		particleList.add(new ParticleItem(key, effects, name, mat, level, cost));
                    }
            	}
            }
        }
        
        Collections.<ParticleItem>sort(particleList);
    }
    
 
    public ParticleItem getParticleByName(String name) {
    	for (ParticleItem pi: particleList) {
    		if (pi.getName().equalsIgnoreCase(name)) {
    			return pi;
    		}
    	}
    	return null;
    }
    
    public ArrayList<ParticleItem> getParticleItems() {
    	return particleList;
    }
    
    public ParticleItem getParticleByKey(String effect) {
    	for (ParticleItem pItem: particleList) {
    		if (pItem.getKey().equalsIgnoreCase(effect)) {
    			return pItem;
    		}
    	}
        return null;
    }
    
    
    /*Particle Effect Methods*/
    
    public void loadProjEffects() {
        projEffectList.clear();
        File particleFile = new File(SkyWars.get().getDataFolder(), "projectileeffects.yml");

        if (!particleFile.exists()) {
        	SkyWars.get().saveResource("projectileeffects.yml", false);
        }

        if (particleFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(particleFile);

            if (storage.getConfigurationSection("effects") != null) {
            	for (String key: storage.getConfigurationSection("effects").getKeys(false)) {
            		String name = storage.getString("effects." + key + ".displayname");
                	String material = storage.getString("effects." + key + ".icon");
                	int level = storage.getInt("effects." + key + ".level");
                	int cost = storage.getInt("effects." + key + ".cost");
                	List<String> particles = storage.getStringList("effects." + key + ".particles");
                	
                	List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
                	if (particles != null) {
                		for (String part: particles) {
                    		final String[] parts = part.split(":");
                            if (parts.length == 6 
                            		&& SkyWars.getNMS().isValueParticle(parts[0].toUpperCase())
                            		&& Util.get().isFloat(parts[1])
                    				&& Util.get().isFloat(parts[2])
                    				&& Util.get().isFloat(parts[3])
                            		&& Util.get().isInteger(parts[4]) 
                            		&& Util.get().isInteger(parts[5])) {
                            	effects.add(new ParticleEffect(parts[0].toUpperCase(), Float.valueOf(parts[1]), Float.valueOf(parts[2]), Float.valueOf(parts[3]), Integer.valueOf(parts[4]), Integer.valueOf(parts[5])));
                            } else {
                            	SkyWars.get().getLogger().info("The particle effect " + key + " has an invalid particle effect");
                            }
                    	}
                	}                	
        
                	Material mat = Material.matchMaterial(material);
                	if (mat != null) {
                		projEffectList.add(new ParticleItem(key, effects, name, mat, level, cost));
                    }
            	}
            }
        }
        
        Collections.<ParticleItem>sort(projEffectList);
    }
    
    public ParticleItem getProjByName(String name) {
    	for (ParticleItem pi: projEffectList) {
    		if (pi.getName().equalsIgnoreCase(name)) {
    			return pi;
    		}
    	}
        return null;
    }
    
    public ArrayList<ParticleItem> getProjParticleItems() {
    	return projEffectList;
    }
    
    public ParticleItem getProjByKey(String effect) {
    	for (ParticleItem pItem: projEffectList) {
    		if (pItem.getKey().equalsIgnoreCase(effect)) {
    			return pItem;
    		}
    	}
        return null;
    }  
    
       
    public void loadKillSounds() {
        killSoundList.clear();
        File soundFile = new File(SkyWars.get().getDataFolder(), "killsounds.yml");

        if (!soundFile.exists()) {
        	if (SkyWars.getNMS().getVersion() < 8) {
                	SkyWars.get().saveResource("killsounds18.yml", false);
                	File sf = new File(SkyWars.get().getDataFolder(), "killsounds18.yml");
                	if (sf.exists()) {
                		sf.renameTo(new File(SkyWars.get().getDataFolder(), "killsounds.yml"));
                	}
        	} else {
            	SkyWars.get().saveResource("killsounds.yml", false);
        	}
        }

        if (soundFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(soundFile);

            if (storage.getConfigurationSection("sounds") != null) {
            	for (String key: storage.getConfigurationSection("sounds").getKeys(false)) {
            		String sound = storage.getString("sounds." + key + ".sound");
                	String name = storage.getString("sounds." + key + ".displayName");
                	int volume = storage.getInt("sounds." + key + ".volume");
                	int pitch = storage.getInt("sounds." + key + ".pitch");
                	String material = storage.getString("sounds." + key + ".icon");
                	int level = storage.getInt("sounds." + key + ".level");
                	int cost = storage.getInt("sounds." + key + ".cost");
                	boolean isCustom = storage.getBoolean("sounds." + key + ".isCustomSound");
                	
                	Material mat = Material.matchMaterial(material);
                	if (mat != null) {
                		if (!isCustom) {
                			try {
                				Sound s = Sound.valueOf(sound);
                				if (s != null) {
                					killSoundList.add(new SoundItem(key, sound, name, level, cost, volume, pitch, mat, isCustom));
                				}
                			} catch (IllegalArgumentException e) {
                				SkyWars.get().getServer().getLogger().info(sound + " is not a valid sound in killsounds.yml");
                			}
                		} else {
                			killSoundList.add(new SoundItem(key, sound, name, level, cost, volume, pitch, mat, isCustom));
                		}
                			
                    } else {
                    	SkyWars.get().getServer().getLogger().info(mat + " is not a valid Material in killsounds.yml");
                    }
            	}
            }
        }
        
        Collections.<SoundItem>sort(killSoundList);
    }
    
    public SoundItem getKillSoundByName(String name) {
    	for (SoundItem pi: killSoundList) {
    		if (pi.getName().equalsIgnoreCase(name)) {
    			return pi;
    		}
    	}
        return null;
    }
    
    public ArrayList<SoundItem> getKillSoundItems() {
    	return killSoundList;
    }
    
    public SoundItem getKillSoundByKey(String key) {
    	for (SoundItem pItem: killSoundList) {
    		if (pItem.getKey().equalsIgnoreCase(key)) {
    			return pItem;
    		}
    	}
        return null;
    }
    
    public void loadWinSounds() {
        winSoundList.clear();
        File soundFile = new File(SkyWars.get().getDataFolder(), "winsounds.yml");

        if (!soundFile.exists()) {
        	if (SkyWars.getNMS().getVersion() < 9) {
                	SkyWars.get().saveResource("winsounds18.yml", false);
                	File sf = new File(SkyWars.get().getDataFolder(), "winsounds18.yml");
                	if (sf.exists()) {
                		sf.renameTo(new File(SkyWars.get().getDataFolder(), "winsounds.yml"));
                	}
        	} else {
            	SkyWars.get().saveResource("winsounds.yml", false);
        	}
        }

        if (soundFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(soundFile);

            if (storage.getConfigurationSection("sounds") != null) {
            	for (String key: storage.getConfigurationSection("sounds").getKeys(false)) {
            		String sound = storage.getString("sounds." + key + ".sound");
                	String name = storage.getString("sounds." + key + ".displayName");
                	int volume = storage.getInt("sounds." + key + ".volume");
                	int pitch = storage.getInt("sounds." + key + ".pitch");
                	String material = storage.getString("sounds." + key + ".icon");
                	int level = storage.getInt("sounds." + key + ".level");
                	int cost = storage.getInt("sounds." + key + ".cost");
                	boolean isCustom = storage.getBoolean("sounds." + key + ".isCustomSound");
                	
                	Material mat = Material.matchMaterial(material);
                	if (mat != null) {
                		if (!isCustom) {
                			try {
                				Sound s = Sound.valueOf(sound);
                				if (s != null) {
                    				winSoundList.add(new SoundItem(key, sound, name, level, cost, volume, pitch, mat, isCustom));
                				}
                			} catch (IllegalArgumentException e) {
                				SkyWars.get().getServer().getLogger().info(sound + " is not a valid sound in winsounds.yml");
                			}
                		} else {
                			winSoundList.add(new SoundItem(key, sound, name, level, cost, volume, pitch, mat, isCustom));
                		}
                			
                    } else {
                    	SkyWars.get().getServer().getLogger().info(mat + " is not a valid Material in winsounds.yml");
                    }
            	}
            }
        }
        Collections.<SoundItem>sort(winSoundList);
    }
    
    public SoundItem getWinSoundByName(String name) {
    	for (SoundItem pi: winSoundList) {
    		if (pi.getName().equalsIgnoreCase(name)) {
    			return pi;
    		}
    	}
        return null;
    }
    
    public ArrayList<SoundItem> getWinSoundItems() {
    	return winSoundList;
    }
    
    public SoundItem getWinSoundBySound(String effect) {
    	for (SoundItem pItem: winSoundList) {
    		if (pItem.getSound().equalsIgnoreCase(effect)) {
    			return pItem;
    		}
    	}
        return null;
    }
    
	/*Loads tuants from taunts.yml configuation.
	 */
	private void loadTaunts() {
		tauntList.clear();
        File tauntFile = new File(SkyWars.get().getDataFolder(), "taunts.yml");

        if (!tauntFile.exists()) {
        	if (SkyWars.getNMS().getVersion() < 9) {
                	SkyWars.get().saveResource("taunts18.yml", false);
                	File sf = new File(SkyWars.get().getDataFolder(), "taunts18.yml");
                	if (sf.exists()) {
                		sf.renameTo(new File(SkyWars.get().getDataFolder(), "taunts.yml"));
                	}
        	} else {
            	SkyWars.get().saveResource("taunts.yml", false);
        	}
        }
        
        if (tauntFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(tauntFile);

            if (storage.getConfigurationSection("taunts") != null) {
            	for (String key: storage.getConfigurationSection("taunts").getKeys(false)) {
                	String name = storage.getString("taunts." + key + ".name");
                	List<String> lore = storage.getStringList("taunts." + key + ".lore");
                	int level = storage.getInt("taunts." + key + ".level");
                	int cost = storage.getInt("taunts." + key + ".cost");
                	String message = storage.getString("taunts." + key + ".message");
                	String sound = storage.getString("taunts." + key + ".sound");
                	boolean useCustomSound = storage.getBoolean("taunts." + key + ".useCustomSound", false);
                	double volume = storage.getDouble("taunts." + key + ".volume");
                	double pitch = storage.getDouble("taunts." + key + ".pitch");
                	double speed = storage.getDouble("taunts." + key + ".particleSpeed");
                	int density = storage.getInt("taunts." + key + ".particleDensity");
                	List<String> particles = storage.getStringList("taunts." + key + ".particles");
                	Material icon = Material.valueOf(storage.getString("taunts." + key + ".icon", "DIAMOND"));
                	tauntList.add(new Taunt(key, name, lore, message, sound, useCustomSound, volume, pitch, speed, density, particles, icon, level, cost));
                }
            } 
        }
        Collections.<Taunt>sort(tauntList);
	}
	    
    /**Returns a list of tuants that are currently loaded.
	 */
    public ArrayList<Taunt> getTaunts() {
    	return tauntList;
    }
    
	public Taunt getTauntFromName(String name) {
		for (Taunt taunt: tauntList) {
			if (taunt.getName().equalsIgnoreCase(name)) {
				return taunt;
			}
		}
		return null;
	}
	
	public Taunt getTauntFromKey(String key) {
		for (Taunt taunt: tauntList) {
			if (taunt.getKey().equalsIgnoreCase(key)) {
				return taunt;
			}
		}
		return null;
	}
    
    /*Handles projectile effects*/
  
    public void addProjectile(Projectile p, List<ParticleEffect> e) {
		projectileMap.put(p,  e);
	}
    
    public void addPlayer(UUID p, List<ParticleEffect> e) {
		playerMap.put(p,  e);
	}
    
    public void removePlayer(UUID p) {
		playerMap.remove(p);
	}    
}
