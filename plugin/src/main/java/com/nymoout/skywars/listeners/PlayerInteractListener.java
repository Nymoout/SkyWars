package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.ChestPlacementType;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.Crate;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.menus.gameoptions.KitSelectionMenu;
import com.nymoout.skywars.menus.gameoptions.VotingMenu;
import com.nymoout.skywars.menus.gameoptions.objects.CoordLoc;
import com.nymoout.skywars.menus.playeroptions.OptionsSelectionMenu;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerInteractListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(final PlayerInteractEvent e) {
        final GameMap gameMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gameMap == null) {
            if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
                if (SkyWars.getConfigManager().protectLobby()) {
                    e.setCancelled(true);
                    if (e.getPlayer().hasPermission("sw.alterlobby")) {
                        e.setCancelled(false);
                    }
                }
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.hasItem()) {
                        if (e.getItem().equals(SkyWars.getItemsManager().getItem("optionselect"))) {
                            e.setCancelled(true);
                            Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getConfigManager().getOpenOptionsMenuSound(), 0.5F, 1);
                            new OptionsSelectionMenu(e.getPlayer());
                        } else if (e.getItem().equals(SkyWars.getItemsManager().getItem("joinselect"))) {
                            e.setCancelled(true);
                            if (SkyWars.getIconMenuController().has("joinmenu")) {
                                Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getConfigManager().getOpenJoinMenuSound(), 1, 1);
                                if (GameMap.getPlayableArenas(GameType.TEAM).size() == 0) {
                                    if (!SkyWars.getIconMenuController().hasViewers("joinsinglemenu")) {
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                SkyWars.getIconMenuController().getMenu("joinsinglemenu").update();
                                            }
                                        }.runTaskLater(SkyWars.get(), 5);
                                    }
                                    SkyWars.getIconMenuController().show(e.getPlayer(), "joinsinglemenu");
                                    return;
                                } else if (GameMap.getPlayableArenas(GameType.SINGLE).size() == 0) {
                                    if (!SkyWars.getIconMenuController().hasViewers("jointeammenu")) {
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                SkyWars.getIconMenuController().getMenu("jointeammenu").update();
                                            }
                                        }.runTaskLater(SkyWars.get(), 5);
                                    }
                                    SkyWars.getIconMenuController().show(e.getPlayer(), "jointeammenu");
                                    return;
                                } else {
                                    SkyWars.getIconMenuController().show(e.getPlayer(), "joinmenu");
                                    return;
                                }
                            }
                        } else if (e.getItem().equals(SkyWars.getItemsManager().getItem("spectateselect"))) {
                            e.setCancelled(true);
                            Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getConfigManager().getOpenSpectateMenuSound(), 1, 1);
                            if (GameMap.getPlayableArenas(GameType.TEAM).size() == 0) {
                                if (!SkyWars.getIconMenuController().hasViewers("spectatesinglemenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWars.getIconMenuController().getMenu("joinsinglemenu").update();
                                        }
                                    }.runTaskLater(SkyWars.get(), 5);
                                }
                                SkyWars.getIconMenuController().show(e.getPlayer(), "spectatesinglemenu");
                                return;
                            } else if (GameMap.getPlayableArenas(GameType.SINGLE).size() == 0) {
                                if (!SkyWars.getIconMenuController().hasViewers("spectateteammenu")) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            SkyWars.getIconMenuController().getMenu("jointeammenu").update();
                                        }
                                    }.runTaskLater(SkyWars.get(), 5);
                                }
                                SkyWars.getIconMenuController().show(e.getPlayer(), "spectateteammenu");
                                return;
                            } else {
                                SkyWars.getIconMenuController().show(e.getPlayer(), "spectatemenu");
                                return;
                            }
                        } else if (e.getItem().equals(SkyWars.getItemsManager().getItem("autojoinselect"))) {
                            e.setCancelled(true);
                            Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getConfigManager().getOpenJoinMenuSound(), 1, 1);
                            e.getPlayer().performCommand("sw join");
                        }
                    }
                }
                Player player = e.getPlayer();
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (SkyWars.getNMS().getMainHandItem(player) == null || SkyWars.getNMS().getMainHandItem(player).getType() == Material.AIR)) {
                    Material signPost = SkyWars.getNMS().getMaterial("SIGN_POST").getType();
                    if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == signPost) {
                        Sign s = (Sign) e.getClickedBlock().getState();
                        Location loc = s.getLocation();
                        boolean joined;
                        for (GameMap gMap : GameMap.getMaps()) {
                            if (gMap.hasSign(loc) && gMap.getMatchState().equals(MatchState.WAITINGSTART)) {
                                Party party = Party.getParty(player);
                                if (party != null) {
                                    if (party.getLeader().equals(player.getUniqueId())) {
                                        joined = gMap.addPlayers(null, party);
                                        if (!joined) {
                                            player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                                        }
                                    } else {
                                        player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                                    }
                                } else {
                                    joined = gMap.addPlayers(null, player);
                                    if (!joined) {
                                        player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return;
        } else {
            if (gameMap.getMatchState() == MatchState.WAITINGSTART) {
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.hasItem()) {
                        Player player = e.getPlayer();
                        if (e.getItem().equals(SkyWars.getItemsManager().getItem("kitvote"))) {
                            if (SkyWars.getConfigManager().kitVotingEnabled()) {
                                SkyWars.getIconMenuController().show(player, gameMap.getKitVoteOption().getKey());
                            } else {
                                new KitSelectionMenu(e.getPlayer());
                            }
                            Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenKitMenuSound(), 1, 1);
                            return;
                        } else if (e.getItem().equals(SkyWars.getItemsManager().getItem("votingItem"))) {
                            if (e.getPlayer().hasPermission("sw.votemenu")) {
                                new VotingMenu(e.getPlayer());
                                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenChestMenuSound(), 1, 1);
                            } else {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.nopermission"));
                            }
                            return;
                        } else if (e.getItem().equals(SkyWars.getItemsManager().getItem("exitGameItem"))) {
                            MatchManager.get().playerLeave(player, DamageCause.CUSTOM, true, true, true);
                        }
                    }
                }
                return;
            }
            if (gameMap.getMatchState() == MatchState.PLAYING) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = e.getClickedBlock();
                    if (block.getType().equals(Material.ENDER_CHEST)) {
                        for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                            for (Crate crate : gMap.getCrates()) {
                                if (crate.getLocation().equals(block.getLocation())) {
                                    e.setCancelled(true);
                                    if (SkyWars.getNMS().getVersion() < 9) {
                                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.valueOf("CHEST_OPEN"), 1, 1);
                                    } else {
                                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
                                    }
                                    e.getPlayer().openInventory(crate.getInventory());
                                    SkyWars.getNMS().playEnderChestAction(block, true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (gameMap.getMatchState() == MatchState.ENDING) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        InventoryView inView = e.getPlayer().getOpenInventory();
        if (inView.getTitle().equals(new Messaging.MessageFormatter().format("event.crateInv"))) {
            for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                for (Crate crate : gMap.getCrates()) {
                    if (crate.getInventory().equals(inv) && inv.getViewers().size() <= 1) {
                        if (SkyWars.getNMS().getVersion() < 9) {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.valueOf("CHEST_CLOSE"), 1, 1);
                        } else {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                        }
                        SkyWars.getNMS().playEnderChestAction(e.getPlayer().getWorld().getBlockAt(crate.getLocation()), false);
                        return;
                    }
                }
            }
        } else if (inView.getTitle().contains("chest.yml")) {
            SkyWars.getChestManager().save(inView.getTitle());
        }

    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            GameMap gMap = MatchManager.get().getPlayerMap((Player) event.getWhoClicked());
            if (gMap == null) {
                ItemStack item;
                ItemStack item2;
                if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                    item = event.getWhoClicked().getInventory().getItem(event.getHotbarButton());
                    item2 = event.getCurrentItem();
                } else {
                    item = event.getCurrentItem();
                    item2 = event.getCurrentItem();
                }

                if (item != null && (item.equals(SkyWars.getItemsManager().getItem("optionselect"))
                        || item.equals(SkyWars.getItemsManager().getItem("joinselect"))
                        || item.equals(SkyWars.getItemsManager().getItem("spectateselect"))
                        || item.equals(SkyWars.getItemsManager().getItem("autojoinselect")))
                        || item2 != null && (item2.equals(SkyWars.getItemsManager().getItem("optionselect"))
                        || item2.equals(SkyWars.getItemsManager().getItem("joinselect"))
                        || item2.equals(SkyWars.getItemsManager().getItem("spectateselect"))
                        || item2.equals(SkyWars.getItemsManager().getItem("autojoinselect")))) {
                    event.setCancelled(true);
                }
            } else {
                if (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.ENDING)) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        final GameMap gameMap = MatchManager.get().getPlayerMap(event.getPlayer());
        if (gameMap == null) {
            return;
        }
        if (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.ENDING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        GameMap gMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gMap == null) {
            if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST) || e.getBlock().getType().equals(Material.DIAMOND_BLOCK) || e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                GameMap map = GameMap.getMap(e.getPlayer().getWorld().getName());
                if (map == null) {
                    return;
                }
                if (map.isEditing()) {
                    if (e.getBlock().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
                        Chest chest = (Chest) e.getBlock().getState();
                        map.removeChest(chest);
                        InventoryHolder ih = chest.getInventory().getHolder();
                        if (ih instanceof DoubleChest) {
                            DoubleChest dc = (DoubleChest) ih;
                            Chest left = (Chest) dc.getLeftSide();
                            Chest right = (Chest) dc.getRightSide();
                            Location locLeft = left.getLocation();
                            Location locRight = right.getLocation();
                            World world = e.getBlock().getWorld();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    world.getBlockAt(locLeft).setType(Material.AIR);
                                    world.getBlockAt(locRight).setType(Material.AIR);
                                }
                            }.runTaskLater(SkyWars.get(), 2L);
                        }
                        e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.removeChest"));
                    } else if (e.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
                        boolean result = map.removeTeamCard(e.getBlock().getLocation());
                        if (result) {
                            e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + (map.getMaxPlayers() + 1)).setVariable("mapname", map.getDisplayName()).format("maps.spawnRemoved"));
                        }
                    } else if (e.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                        boolean result = map.removeDeathMatchSpawn(e.getBlock().getLocation());
                        if (result) {
                            e.getPlayer().sendMessage(new Messaging.MessageFormatter().setVariable("num", "" + (map.getDeathMatchSpawns().size() + 1)).setVariable("mapname", map.getDisplayName()).format("maps.deathSpawnRemoved"));
                        }
                    }
                }
            }
            return;
        }
        if (gMap.getMatchState().equals(MatchState.WAITINGSTART)) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    CoordLoc spawn = gMap.getPlayerCard(e.getPlayer()).getTeamCard().getSpawn();
                    e.getPlayer().teleport(new Location(gMap.getCurrentWorld(), spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5));
                }
            }.runTaskLater(SkyWars.get(), 2);
        }
        if (gMap.getMatchState().equals(MatchState.PLAYING)) {
            Block block = e.getBlock();
            if (block.getType().equals(Material.ENDER_CHEST)) {
                for (Crate crate : gMap.getCrates()) {
                    if (crate.getLocation().equals(block.getLocation())) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        GameMap gMap = MatchManager.get().getPlayerMap(e.getPlayer());
        if (gMap == null) {
            if (e.getBlockPlaced().getType().equals(Material.CHEST) || e.getBlock().getType().equals(Material.TRAPPED_CHEST)) {
                GameMap map = GameMap.getMap(e.getPlayer().getWorld().getName());
                if (map == null) {
                    return;
                }
                if (map.isEditing()) {
                    Location loc = e.getBlock().getLocation();
                    Player player = e.getPlayer();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            map.addChest((Chest) loc.getBlock().getState(), map.getChestPlacementType());
                            if (map.getChestPlacementType() == ChestPlacementType.NORMAL) {
                                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.addChest"));
                            } else if (map.getChestPlacementType() == ChestPlacementType.CENTER) {
                                player.sendMessage(new Messaging.MessageFormatter().setVariable("mapname", map.getDisplayName()).format("maps.addCenterChest"));
                            }
                        }
                    }.runTaskLater(SkyWars.get(), 2L);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
            if (gMap.getDeathMatchWaiters().contains(player.getUniqueId().toString())) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
