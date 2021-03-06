package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class LobbyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(final EntityDamageByEntityEvent e) {
        if (SkyWars.getConfigManager().protectLobby() && Util.get().isSpawnWorld(e.getEntity().getWorld())) {
            e.setCancelled(true);
            if (e.getEntity() instanceof Player || e.getDamager() instanceof Player) {
                if (e.getDamager().hasPermission("sw.alterlobby")) {
                    e.setCancelled(false);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Util.get().isSpawnWorld(e.getEntity().getWorld())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent e) {
        if (SkyWars.getConfigManager().protectLobby() && Util.get().isSpawnWorld(e.getWorld())) {
            e.setCancelled(e.toWeatherState());
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (SkyWars.getConfigManager().protectLobby() && Util.get().isSpawnWorld(player.getWorld())) {
            if (!player.hasPermission("sw.alterlobby") && !SkyWars.getIconMenuController().has(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent e) {
        if (SkyWars.getConfigManager().protectLobby() && Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            if (!e.getPlayer().hasPermission("sw.alterlobby")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void signPlaced(SignChangeEvent event) {
        if (Util.get().isSpawnWorld(event.getBlock().getWorld())) {
            String[] lines = event.getLines();
            if (lines[0].equalsIgnoreCase("[sw]") && lines.length >= 2) {
                if (event.getPlayer().hasPermission("sw.signs")) {
                    Location signLocation = event.getBlock().getLocation();
                    World w = signLocation.getWorld();
                    Block b = w.getBlockAt(signLocation);
                    if (b.getType() == Material.WALL_SIGN || b.getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()) {
                        event.setCancelled(true);
                        String arenaName = lines[1];
                        GameMap gMap = GameMap.getMap(arenaName);
                        if (gMap != null) {
                            gMap.addSign(signLocation);
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.added"));
                        } else {
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.no-map"));
                        }
                    }
                } else {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.signs-no-perm"));
                    event.setCancelled(true);
                }
            } else if (lines[0].equalsIgnoreCase("[swl]") && lines.length >= 3) {
                if (event.getPlayer().hasPermission("sw.signs")) {
                    Location signLocation = event.getBlock().getLocation();
                    World w = signLocation.getWorld();
                    Block b = w.getBlockAt(signLocation);
                    if (b.getType() == Material.WALL_SIGN || b.getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()) {
                        event.setCancelled(true);
                        if (SkyWars.get().getUseable().contains(lines[1].toUpperCase())) {
                            LeaderType type = LeaderType.valueOf(lines[1].toUpperCase());
                            if (Util.get().isInteger(lines[2])) {
                                if (Integer.valueOf(lines[2]) <= SkyWars.getConfigManager().getLeaderSize()) {
                                    SkyWars.getLB().addLeaderSign(Integer.valueOf(lines[2]), type, signLocation);
                                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.addedleader"));
                                } else {
                                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.invalid-range"));
                                }
                            } else {
                                event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.position"));
                            }
                        } else {
                            event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.invalid-type"));
                        }
                    }
                } else {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("error.signs-no-perm"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void signRemoved(BlockBreakEvent event) {
        if (Util.get().isSpawnWorld(event.getBlock().getWorld())) {
            Location blockLocation = event.getBlock().getLocation();
            World w = blockLocation.getWorld();
            Block b = w.getBlockAt(blockLocation);
            if (b.getType() == Material.WALL_SIGN || b.getType() == SkyWars.getNMS().getMaterial("SIGN_POST").getType()) {
                Sign sign = (Sign) b.getState();
                Location loc = sign.getLocation();
                boolean removed = false;
                for (GameMap gMap : GameMap.getMaps()) {
                    if (!removed) {
                        removed = gMap.removeSign(loc);
                    }
                }
                if (!removed) {
                    removed = SkyWars.getLB().removeLeaderSign(loc);
                }
                if (removed) {
                    event.getPlayer().sendMessage(new Messaging.MessageFormatter().format("signs.remove"));
                }
            }
        }
    }

    @EventHandler
    public void onPressurePlate(final PlayerInteractEvent e) {
        if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            Player player = e.getPlayer();
            GameMap gMap = MatchManager.get().getPlayerMap(player);
            if (gMap == null) {
                if (e.getAction() == Action.PHYSICAL && (e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("STONE_PLATE").getType()
                        || e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("IRON_PLATE").getType()
                        || e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("GOLD_PLATE").getType())) {
                    if (SkyWars.getConfigManager().pressurePlateJoin()) {
                        Location spawn = SkyWars.getConfigManager().getSpawn();
                        if (spawn != null) {
                            boolean joined = false;
                            int count = 0;
                            Party party = Party.getParty(player);
                            while (count < 4 && !joined) {
                                if (party != null) {
                                    if (party.getLeader().equals(player.getUniqueId())) {
                                        boolean tryJoin = true;
                                        for (UUID uuid : party.getMembers()) {
                                            if (Util.get().isBusy(uuid)) {
                                                tryJoin = false;
                                                party.sendPartyMessage(new Messaging.MessageFormatter().setVariable("player", Bukkit.getPlayer(uuid).getName()).format("party.memberbusy"));
                                            }
                                        }
                                        if (tryJoin) {
                                            if (e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("STONE_PLATE").getType()) {
                                                joined = MatchManager.get().joinGame(party, GameType.ALL);
                                            } else if (e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("IRON_PLATE").getType()) {
                                                joined = MatchManager.get().joinGame(party, GameType.SINGLE);
                                            } else {
                                                joined = MatchManager.get().joinGame(party, GameType.TEAM);
                                            }
                                        } else {
                                            break;
                                        }
                                    } else {
                                        player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                                        joined = true;
                                        break;
                                    }
                                } else {
                                    if (e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("STONE_PLATE").getType()) {
                                        joined = MatchManager.get().joinGame(player, GameType.ALL);
                                    } else if (e.getClickedBlock().getType() == SkyWars.getNMS().getMaterial("IRON_PLATE").getType()) {
                                        joined = MatchManager.get().joinGame(player, GameType.SINGLE);
                                    } else {
                                        joined = MatchManager.get().joinGame(player, GameType.TEAM);
                                    }
                                }
                                count++;
                            }
                            if (!joined) {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join"));
                            }
                        } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
                            SkyWars.get().getLogger().info("YOU MUST SET SPAWN IN THE LOBBY WORLD WITH /SWR SETSPAWN BEFORE STARTING A GAME");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJumpPlayer(final PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (Util.get().isSpawnWorld(player.getWorld())) {
            Location blockUnderPlayer = player.getLocation();
            blockUnderPlayer.setY(blockUnderPlayer.getY() - 1);
            if (blockUnderPlayer.getBlock().getType() == SkyWars.getNMS().getMaterial("SLIME_BLOCK").getType()) {
                Vector v = player.getLocation().getDirection().multiply(4.5D).setY(1.5D);
                player.setVelocity(v);
                SkyWars.getNMS().playGameSound(player.getLocation(), "ENDERDRAGON_WINGS", 4.0F, 3.0F, false);
            }
            if (player.getLocation().getY() < 0){
                player.teleport(SkyWars.getConfigManager().getSpawn());
            }
        }
    }


    @EventHandler
    public void onPlayerLosesFood(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Util.get().isSpawnWorld(e.getEntity().getWorld())) {
                e.setCancelled(true);
            }
        }
    }
}
