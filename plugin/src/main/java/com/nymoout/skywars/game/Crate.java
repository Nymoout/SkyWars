package com.nymoout.skywars.game;

import com.nymoout.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.nymoout.skywars.utilities.Messaging;

public class Crate {
	private Location loc;
	private Inventory inv = Bukkit.createInventory(null, 27, new Messaging.MessageFormatter().format("event.crateInv"));
	private Entity ent;
	private boolean success = false;
	private double prevY;
	
	Crate(Location loc, int max) {
		this.loc = loc.clone();
		SkyWars.getChestManager().fillCrate(inv, max);
		ent = SkyWars.getNMS().spawnFallingBlock(loc.add(0, 40, 0), Material.SAND, false);
		prevY = ent.getLocation().getY();
		checkSuccess();
	}
	
	private void checkSuccess() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (ent.getLocation().getBlockY() == prevY && !success) {
					ent.getWorld().getBlockAt(ent.getLocation()).setType(Material.ENDER_CHEST);
					setLocation(ent.getWorld().getBlockAt(ent.getLocation()));
					ent.remove();
				} else {
					prevY = ent.getLocation().getY();
					checkSuccess();
				}
			}
		}.runTaskLater(SkyWars.get(), 10L);
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public Entity getEntity() {
		return ent;
	}

	public void setLocation(Block block) {
		loc = block.getLocation().clone();
		success = true;
	}
}
