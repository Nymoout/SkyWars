package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWars.getConfigManager().getSpawn() != null && SkyWars.getConfigManager().teleportOnJoin()) {
                    e.getPlayer().teleport(SkyWars.getConfigManager().getSpawn());
                }
            }
        }.runTaskLater(SkyWars.get(), 1);

        if (SkyWars.getConfigManager().promptForResource()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setResourcePack(SkyWars.getConfigManager().getResourceLink());
                }
            }.runTaskLater(SkyWars.get(), 20);
        }

        if (PlayerStat.getPlayerStats(e.getPlayer()) != null) {
            PlayerStat.removePlayer(e.getPlayer().getUniqueId().toString());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (GameMap gMap : GameMap.getMaps()) {
                    if (gMap.getCurrentWorld() != null && gMap.getCurrentWorld().equals(e.getPlayer().getWorld())) {
                        if (SkyWars.getConfigManager().getSpawn() != null) {
                            e.getPlayer().teleport(SkyWars.getConfigManager().getSpawn());
                        }
                    }
                }
            }
        }.runTaskLater(SkyWars.get(), 1);
        PlayerStat.getPlayers().add(new PlayerStat(e.getPlayer()));
    }
}
