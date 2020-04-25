package com.nymoout.skywars.nms.v1_8_R1;

import com.nymoout.skywars.api.NMS;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Chunk;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftFallingSand;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BlockIterator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NMSHandler implements NMS {

	@Override
	public void sendTab(Player player, String header, String footer) {
		CraftPlayer craftplayer = (CraftPlayer)player;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent headerJSON = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent footerJSON = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, headerJSON);
			headerField.setAccessible(!headerField.isAccessible());
			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footerJSON);
			footerField.setAccessible(!footerField.isAccessible());
		} catch (Exception var11) {
			var11.printStackTrace();
		}

		connection.sendPacket(packet);
	}

	public void respawnPlayer(Player player) {
		((CraftServer)Bukkit.getServer()).getHandle().moveToWorld(((CraftPlayer)player).getHandle(), 0, false);
	}
	
	@SuppressWarnings("unchecked")
	public void updateChunks(World world, List<Chunk> chunks) {
		for (Chunk currentChunk: chunks) {
			net.minecraft.server.v1_8_R1.World mcWorld = ((CraftChunk) currentChunk).getHandle().world;
	        for (EntityPlayer ep : (List<EntityPlayer>) mcWorld.players) {
	                ep.chunkCoordIntPairQueue.add(new ChunkCoordIntPair(currentChunk.getX(), currentChunk.getZ()));
	        }
		}
	}
	
	public void sendParticles(World world, String type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float data, int amount) {
		EnumParticle particle = EnumParticle.valueOf(type);
		PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, false, x, y, z, offsetX, offsetY, offsetZ, data, amount, 1);
		for (Player player: world.getPlayers()) {
			CraftPlayer start = (CraftPlayer) player; //Replace player with your player.
			EntityPlayer target = start.getHandle();
			PlayerConnection connect = target.playerConnection;
			connect.sendPacket(particles);
		}
	}

	public FireworkEffect getFireworkEffect(Color one, Color two, Color three, Color four, Color five, Type type) {
		return FireworkEffect.builder().flicker(false).withColor(one, two, three, four).withFade(five).with(type).trail(true).build();
	}
	
	public void sendTitle(Player player, int fadein, int stay, int fadeout, String title, String subtitle) {
		PlayerConnection pConn = ((CraftPlayer) player).getHandle().playerConnection;
		PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(EnumTitleAction.TIMES, (IChatBaseComponent) null, (int) fadein, (int) stay, (int) fadeout);
		pConn.sendPacket(pTitleInfo);
		if (subtitle != null) {
			subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
			IChatBaseComponent iComp = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, iComp);
			pConn.sendPacket(pSubtitle);
		}
		if (title != null) {
			title = title.replaceAll("%player%", player.getDisplayName());
			title = ChatColor.translateAlternateColorCodes('&', title);
			IChatBaseComponent iComp = ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle pTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, iComp);
			pConn.sendPacket(pTitle);
		}
	}

	public void playGameSound(Location loc, String sound, float volume, float pitch, boolean customSound) {
		if (customSound) {
		} else {
			loc.getWorld().playSound(loc, Sound.valueOf(sound), volume, pitch);
		}
	}

	public void sendActionBar(Player p, String msg) {
        String s = ChatColor.translateAlternateColorCodes('&', msg);
        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
	}

	public String getItemName(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).getName();
	}
	
	public ItemStack getMainHandItem(Player player) {
        return player.getInventory().getItemInHand();
	}
	
	public ItemStack getOffHandItem(Player player) {
		return null;
	}
	
	public ItemStack getItemStack(Material material, List<String> lore, String message) {
    	ItemStack addItem = new ItemStack(material, 1);
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);
        addItem.setItemMeta(addItemMeta);
        return addItem;
	}
	
	@Override
	public ItemStack getItemStack(ItemStack item, List<String> lore, String message) {
		ItemStack addItem = item.clone();
        ItemMeta addItemMeta = addItem.getItemMeta();
        addItemMeta.setDisplayName(message);
        addItemMeta.setLore(lore);
        addItem.setItemMeta(addItemMeta);
        return addItem;
	}

	@Override
	public boolean isValueParticle(String string) {
		return true;
	}

	@Override
	public void updateSkull(Skull skull, UUID uuid) {
		skull.setSkullType(SkullType.PLAYER);
		skull.setOwner(Bukkit.getOfflinePlayer(uuid).getName());
	}

	public void setMaxHealth(Player player, int health) {
		player.setMaxHealth(health);
	}
	
	@Override
	public void spawnDragon(World world, Location loc) {
		WorldServer w = ((CraftWorld) world).getHandle();
		EntityEnderDragon dragon = new EntityEnderDragon(w);
		dragon.setLocation(loc.getX(), loc.getY(), loc.getZ(), w.random.nextFloat() * 360.0F, 0.0F);
		w.addEntity(dragon);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Entity spawnFallingBlock(Location loc, Material mat, boolean damage) {
		FallingBlock block = loc.getWorld().spawnFallingBlock(loc, mat, (byte) 0);
		block.setDropItem(false);
		EntityFallingBlock fb = ((CraftFallingSand) block).getHandle();
		fb.a(damage);
		return block;
	}
	
	@Override
	public void playEnderChestAction(Block block, boolean open) {
        Location location = block.getLocation();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityEnderChest ec = (TileEntityEnderChest) world.getTileEntity(position);
        world.playBlockAction(position, ec.w(), 1, open ? 1 : 0);
    }	
	
	@Override
	public void setEntityTarget(Entity ent, Player player) {
		EntityCreature entity = (EntityCreature) ((CraftEntity) ent).getHandle();
		entity.setGoalTarget(((EntityLiving) ((CraftPlayer) player).getHandle()), null, false);
	}
	
	@Override
	public void updateSkull(SkullMeta meta1, Player player) {
		meta1.setOwner(player.getName());
	}

	@Override
	public ChunkGenerator getChunkGenerator() {
		return new ChunkGenerator() {
			@Override
			public List<BlockPopulator> getDefaultPopulators(World world) {
				return Arrays.asList(new BlockPopulator[0]);
			}

			@Override
			public boolean canSpawn(World world, int x, int z) {
				return true;
			}

			@Override
			public byte[] generate(World world, Random random, int x, int z) {
				return new byte[32768];
			}

			@Override
			public Location getFixedSpawnLocation(World world, Random random) {
				return new Location(world, 0.0D, 64.0D, 0.0D);
			}
		};
	}

	@Override
	public boolean checkMaterial(FallingBlock fb, Material mat) {
		if (fb.getMaterial().equals(mat)) {
			return true;
		}
		return false;
	}

	@Override
	public Objective getNewObjective(Scoreboard scoreboard, String criteria, String DisplayName) {
		return scoreboard.registerNewObjective(DisplayName, criteria);
	}

	@Override
	public void setGameRule(World world, String rule, String bool) {
		world.setGameRuleValue(rule, bool);
	}

	@Override
	public boolean headCheck(Block h1) {
		return h1.getType() == Material.SKULL;
	}

	@Override
	public ItemStack getBlankPlayerHead() {
		return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
	}

	@Override
	public int getVersion() {
		return 8;
	}

	@Override
	public ItemStack getMaterial(String item) {
		if (item.equalsIgnoreCase("SKULL_ITEM")) {
			return new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		} else {
			return new ItemStack(Material.valueOf(item), 1);
		}
	}

	@Override
	public ItemStack getColorItem(String mat, byte color) {
		if (mat.equalsIgnoreCase("wool")) {
			return new ItemStack(Material.WOOL, 1, (short) color);
		} else if (mat.equalsIgnoreCase("glass")) {
			return new ItemStack(Material.STAINED_GLASS, 1, (short) color);
		} else if (mat.equalsIgnoreCase("banner")) {
			return new ItemStack(Material.BANNER, 1, (short) color);
		} else {
			return new ItemStack(Material.STAINED_GLASS, 1, (short) color);
		}
	}

	@Override
	public void setBlockWithColor(World world, int x, int y, int z, Material mat, byte cByte) {
		world.getBlockAt(x, y, z).setType(mat);
		world.getBlockAt(x, y, z).setData(cByte);
	}

	@Override
	public void deleteCache() {
		RegionFileCache.a();
	}

	@Override
	public Block getHitBlock(ProjectileHitEvent event) {
		BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);
		Block hitBlock = null;
		while (iterator.hasNext()) {
			hitBlock = iterator.next();
			if (hitBlock.getType() != Material.AIR) {
				break;
			}
		}
		return hitBlock;
	}

}
