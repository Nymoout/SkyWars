package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerData;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.ChatSerializerUtil;
import com.nymoout.skywars.utilities.Serializers;
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
    public void onDeath2(final PlayerDeathEvent e) {
        final GameMap gameMap = MatchManager.get().getPlayerMap(e.getEntity());

        if (gameMap == null) {
            return;
        }

        final Player player = e.getEntity();
        e.getEntity().getInventory().clear();
        e.getEntity().getInventory().setArmorContents(null);

        DamageCause damageCause = DamageCause.CUSTOM;
        if (e.getEntity().getLastDamageCause() != null) {
            damageCause = e.getEntity().getLastDamageCause().getCause();
        }
        final DamageCause dCause = damageCause;
        player.getWorld().strikeLightningEffect(player.getLocation());
        e.setDeathMessage("");
        MatchManager.get().playerLeave(player, dCause, false, true, true);
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        final PlayerData pData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        if (pData != null) {
            if (SkyWars.getConfigManager().spectateEnable()) {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(e.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0, 95, 0);
                    e.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            MatchManager.get().addSpectator(gMap, e.getPlayer());
                        }
                    }.runTaskLater(SkyWars.get(), 15L);
                }
            } else {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(e.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0, 200, 0);
                    e.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            pData.restore(false);
                        }
                    }.runTaskLater(SkyWars.get(), 15L);
                }
            }
        }
        if (Util.get().isSpawnWorld(e.getPlayer().getWorld())) {
            e.setRespawnLocation(SkyWars.getConfigManager().getSpawn());
            PlayerStat.updatePlayer(e.getPlayer().getUniqueId().toString());
        }
    }
}
