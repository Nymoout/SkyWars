package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerData;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath2(final PlayerDeathEvent v2) {
        final GameMap gameMap = MatchManager.get().getPlayerMap(v2.getEntity());

        if (gameMap == null) {
            return;
        }

        final Player player = v2.getEntity();
        v2.getEntity().getInventory().clear();
        v2.getEntity().getInventory().setArmorContents(null);

        DamageCause damageCause = DamageCause.CUSTOM;
        if (v2.getEntity().getLastDamageCause() != null) {
            damageCause = v2.getEntity().getLastDamageCause().getCause();
        }
        final DamageCause dCause = damageCause;
        v2.setDeathMessage("");

        MatchManager.get().playerLeave(player, dCause, false, true, true);
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent a1) {
        final PlayerData pData = PlayerData.getPlayerData(a1.getPlayer().getUniqueId());
        if (pData != null) {
            if (SkyWars.getConfigManager().spectateEnable()) {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0, 95, 0);
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            MatchManager.get().addSpectator(gMap, a1.getPlayer());
                        }
                    }.runTaskLater(SkyWars.get(), 15L);
                }
            } else {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0, 200, 0);
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            pData.restore(false);
                        }
                    }.runTaskLater(SkyWars.get(), 15L);
                }
            }
        }
        if (Util.get().isSpawnWorld(a1.getPlayer().getWorld())) {
            a1.setRespawnLocation(SkyWars.getConfigManager().getSpawn());
            PlayerStat.updatePlayer(a1.getPlayer().getUniqueId().toString());
        }
    }
}
