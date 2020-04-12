package com.nymoout.skywars.listeners;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.menus.OptionsSelectionMenu;
import com.nymoout.skywars.objects.SWRServer;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LobbyListener implements Listener {

    private static Inventory joinMenu;

    public LobbyListener() {
        ArrayList<SWRServer> servers = SWRServer.getServers();

        int menuSize = 81;
        int rowCount = 9;
        while (rowCount < servers.size() && rowCount < menuSize) {
            rowCount += 9;
        }
        joinMenu = Bukkit.createInventory(null, rowCount, new Messaging.MessageFormatter().format("menu.joingame-menu-title"));
        updateJoinMenu();
    }

    public static void updateJoinMenu() {
        if (SkyWars.getCfg().joinMenuEnabled() && joinMenu != null) {
            ArrayList<SWRServer> servers = SWRServer.getServers();

            int menuSize = 81;
            int rowCount = 9;
            while (rowCount < servers.size() && rowCount < menuSize) {
                rowCount += 9;
            }

            for (int iii = 0; iii < servers.size(); iii++) {
                if (iii >= joinMenu.getSize()) {
                    break;
                }

                SWRServer server = servers.get(iii);

                List<String> loreList = Lists.newLinkedList();
                if (server.getMatchState() == MatchState.OFFLINE) {
                    loreList.add((new Messaging.MessageFormatter().format("signs.offline").toUpperCase()));
                } else if (server.getMatchState() == MatchState.WAITINGSTART) {
                    loreList.add((new Messaging.MessageFormatter().format("signs.joinable").toUpperCase()));
                } else if (server.getMatchState().equals(MatchState.PLAYING) || server.getMatchState().equals(MatchState.SUDDENDEATH)) {
                    loreList.add((new Messaging.MessageFormatter().format("signs.playing").toUpperCase()));
                } else if (server.getMatchState().equals(MatchState.ENDING)) {
                    loreList.add((new Messaging.MessageFormatter().format("signs.ending").toUpperCase()));
                }
                loreList.add((new Messaging.MessageFormatter().setVariable("playercount", "" + server.getPlayerCount()).setVariable("maxplayers", "" + server.getMaxPlayers()).format("signs.line4")));

                double xy = 0;
                if (server.getMaxPlayers() != 0) {
                    xy = ((double) (server.getPlayerCount() / server.getMaxPlayers()));
                }

                ItemStack gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("blockwaiting")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                if (server.getMatchState().equals(MatchState.OFFLINE)) {
                    gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("blockoffline")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                } else if (server.getMatchState().equals(MatchState.PLAYING) || server.getMatchState().equals(MatchState.SUDDENDEATH)) {
                    gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("blockplaying")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                } else if (server.getMatchState().equals(MatchState.ENDING)) {
                    gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("blockending")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                } else if (server.getMatchState() == MatchState.WAITINGSTART) {
                    gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("almostfull")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                    if (xy < 0.75) {
                        gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("threefull")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                    }
                    if (xy < 0.50) {
                        gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("halffull")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                    }
                    if (xy < 0.25) {
                        gameIcon = SkyWars.getNMS().getItemStack(Material.valueOf(SkyWars.getCfg().getMaterial("almostempty")), loreList, ChatColor.translateAlternateColorCodes('&', server.getDisplayName()));
                    }
                }
                joinMenu.setItem(iii, gameIcon);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onEntityDamage(final EntityDamageByEntityEvent e) {
        if (SkyWars.getCfg().protectLobby() && Util.get().isSpawnWorld(e.getEntity().getWorld())) {
            e.setCancelled(true);
            if (e.getEntity() instanceof Player || e.getDamager() instanceof Player) {
                if (((Player) e.getDamager()).hasPermission("sw.alterlobby")) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onClick(final PlayerInteractEvent e) {
        if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            if (SkyWars.getCfg().protectLobby()) {
                e.setCancelled(true);
                if (e.getPlayer().hasPermission("sw.alterlobby")) {
                    e.setCancelled(false);
                }
            }

            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.hasItem()) {
                    if (e.getItem().equals(SkyWars.getIM().getItem("optionselect"))) {
                        e.setCancelled(true);
                        Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getCfg().getOpenOptionsMenuSound(), 0.5F, 1);
                        new OptionsSelectionMenu(e.getPlayer());
                    } else if (e.getItem().equals(SkyWars.getIM().getItem("joinselect"))) {
                        e.setCancelled(true);
                        Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getCfg().getOpenJoinMenuSound(), 1, 1);
                        e.getPlayer().openInventory(joinMenu);
                    } else if (e.getItem().equals(SkyWars.getIM().getItem("autojoinselect"))) {
                        e.setCancelled(true);
                        Util.get().playSound(e.getPlayer(), e.getPlayer().getLocation(), SkyWars.getCfg().getConfirmeSelctionSound(), 1, 1);
                        e.getPlayer().performCommand("sw join");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (Util.get().isSpawnWorld(player.getWorld())) {
            if (SkyWars.getCfg().protectLobby()) {
                if (!player.hasPermission("sw.alterlobby") && !SkyWars.getIC().has(player)) {
                    e.setCancelled(true);
                }
            }
            int rawSlot = e.getRawSlot();
            if (e.getInventory().equals(joinMenu) && rawSlot < joinMenu.getSize() && rawSlot >= 0) {
                e.setCancelled(true);
                SWRServer server = SWRServer.getServerByDisplayName(ChatColor.stripColor(SkyWars.getNMS().getItemName(e.getCurrentItem())));
                if (server == null) {
                    return;
                }

                if (server.getMatchState() != MatchState.WAITINGSTART) {
                    Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getErrorSound(), 1, 1);
                    return;
                }

                if (player.hasPermission("sw.join")) {
                    if (player != null) {
                        if (server.getMatchState() == MatchState.WAITINGSTART && server.getPlayerCount() < server.getMaxPlayers()) {
                            player.closeInventory();
                            server.setPlayerCount(server.getPlayerCount() + 1);
                            server.updateSigns();
                            SkyWars.get().sendBungeeMsg(player, "Connect", server.getServerName());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent e) {
        if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            if (!e.getPlayer().hasPermission("sw.alterlobby") && !SkyWars.getIC().has(e.getPlayer())) {
                e.setCancelled(true);
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
                Vector v = player.getLocation().getDirection().multiply(3.5D).setY(1.5D);
                player.setVelocity(v);
                SkyWars.getNMS().playGameSound(player.getLocation(), "ENDERDRAGON_WINGS", 4.0F, 3.0F, false);
            }
            if (player.getLocation().getY() < 0) {
                player.teleport(SkyWars.getCfg().getSpawn());
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