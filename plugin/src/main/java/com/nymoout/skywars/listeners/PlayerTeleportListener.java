package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        final GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap == null) {
            if (SkyWars.getConfigManager().getSpawn() != null) {
                if (!e.getFrom().getWorld().equals(SkyWars.getConfigManager().getSpawn().getWorld()) && e.getTo().getWorld().equals(SkyWars.getConfigManager().getSpawn().getWorld())) {
                    PlayerStat.updatePlayer(e.getPlayer().getUniqueId().toString());
                    return;
                }
                if (e.getFrom().getWorld().equals(SkyWars.getConfigManager().getSpawn().getWorld()) && !e.getTo().getWorld().equals(SkyWars.getConfigManager().getSpawn().getWorld())) {
                    if (SkyWars.getConfigManager().lobbyBoardEnabled()) {
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    }
                    if (SkyWars.getConfigManager().optionsMenuEnabled()) {
                        if (player.getInventory().getItem(SkyWars.getConfigManager().getOptionsSlot()) != null) {
                            if (player.getInventory().getItem(SkyWars.getConfigManager().getOptionsSlot()).equals(SkyWars.getItemsManager().getItem("optionselect"))) {
                                player.getInventory().setItem(SkyWars.getConfigManager().getOptionsSlot(), new ItemStack(Material.AIR, 1));
                            }
                        }
                    }
                    if (SkyWars.getConfigManager().joinMenuEnabled() && player.hasPermission("sw.join")) {
                        if (player.getInventory().getItem(SkyWars.getConfigManager().getJoinSlot()) != null) {
                            if (player.getInventory().getItem(SkyWars.getConfigManager().getJoinSlot()).equals(SkyWars.getItemsManager().getItem("joinselect"))) {
                                player.getInventory().setItem(SkyWars.getConfigManager().getJoinSlot(), new ItemStack(Material.AIR, 1));
                            }
                        }
                    }
                    if (SkyWars.getConfigManager().spectateMenuEnabled() && player.hasPermission("sw.spectate")) {
                        if (player.getInventory().getItem(SkyWars.getConfigManager().getSpectateSlot()) != null) {
                            if (player.getInventory().getItem(SkyWars.getConfigManager().getSpectateSlot()).equals(SkyWars.getItemsManager().getItem("spectateselect"))) {
                                player.getInventory().setItem(SkyWars.getConfigManager().getSpectateSlot(), new ItemStack(Material.AIR, 1));
                            }
                        }
                    }
                }
            }
        } else {
            if (e.getCause().equals(TeleportCause.END_PORTAL) || player.hasPermission("sw.opteleport") || e.getTo().getWorld().equals(e.getFrom().getWorld())) {
                e.setCancelled(false);
            } else {
                if (e.getCause().equals(TeleportCause.ENDER_PEARL) && gameMap.getMatchState() != MatchState.ENDING && gameMap.getMatchState() != MatchState.WAITINGSTART) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }


}
