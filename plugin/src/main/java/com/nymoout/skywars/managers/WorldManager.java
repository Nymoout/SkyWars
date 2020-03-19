package com.nymoout.skywars.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.*;

import com.nymoout.skywars.SkyWars;

public class WorldManager {

	public World createEmptyWorld(String name, World.Environment environment) {
		if (Bukkit.getWorld(name) == null) {
			loadWorld(name, environment);
			return Bukkit.getWorld(name);
		}
		return null;
    }

	public boolean loadWorld(String worldName, World.Environment environment){
		WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(environment);
        worldCreator.generateStructures(false);
        worldCreator.generator(SkyWars.getNMS().getChunkGenerator());
		World world = worldCreator.createWorld();
        world.setDifficulty(Difficulty.NORMAL);
        world.setSpawnFlags(true, true);
        world.setPVP(true);
        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setKeepSpawnInMemory(false);
	    world.setTicksPerAnimalSpawns(1);
	    world.setTicksPerMonsterSpawns(1);
	    world.setAutoSave(false);

		SkyWars.getNMS().setGameRule(world,"doMobSpawning", "false");
		SkyWars.getNMS().setGameRule(world,"mobGriefing", "false");
		SkyWars.getNMS().setGameRule(world,"doFireTick", "false");
		SkyWars.getNMS().setGameRule(world,"showDeathMessages", "false");
		SkyWars.getNMS().setGameRule(world,"announceAdvancements", "false");

        boolean loaded = false;
        for(World w: SkyWars.get().getServer().getWorlds()) {
          if(w.getName().equals(world.getName())) {
            loaded = true;
            break;
          }
        }
        return loaded;
	}
	
	public void unloadWorld(String w) {
	    World world = SkyWars.get().getServer().getWorld(w);
	    if(world != null) {
	        SkyWars.get().getServer().unloadWorld(world, false);
	    }
	}

	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat", "session.lock"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                if (target.mkdirs()) {
                        String[] files = source.list();
                        if (files != null) {
                            for (String file : files) {
                                File srcFile = new File(source, file);
                                File destFile = new File(target, file);
                                copyWorld(srcFile, destFile);
                            }
                        }
                    }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	        SkyWars.get().getLogger().info("Failed to copy world as required!");
	    }
	}
	
	public void deleteWorld(String name) {
		unloadWorld(name);
		File target = new File (SkyWars.get().getServer().getWorldContainer().getAbsolutePath(), name);
		deleteWorld(target);
	}

    @SuppressWarnings("ResultOfMethodCallIgnored")
	public void deleteWorld(File path) {
	      if(path.exists()) {
	          File[] files = path.listFiles();
	          if (files != null) {
                  for (File file: files) {
                      if(file.isDirectory()) {
                          deleteWorld(file);
                      } else {
                          file.delete();
                      }
                  }
              }
	      }
	      path.delete();
	}
	
}


