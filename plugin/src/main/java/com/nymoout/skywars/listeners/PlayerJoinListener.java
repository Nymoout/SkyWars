package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.PlayerStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent a1) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWars.getConfigManager().getSpawn() != null && SkyWars.getConfigManager().teleportOnJoin()) {
                    a1.getPlayer().teleport(SkyWars.getConfigManager().getSpawn());
                }
            }
        }.runTaskLater(SkyWars.get(), 1);

        if (SkyWars.getConfigManager().promptForResource()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    a1.getPlayer().setResourcePack(SkyWars.getConfigManager().getResourceLink());
                }
            }.runTaskLater(SkyWars.get(), 20);
        }

        if (PlayerStat.getPlayerStats(a1.getPlayer()) != null) {
            PlayerStat.removePlayer(a1.getPlayer().getUniqueId().toString());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (GameMap gMap : GameMap.getMaps()) {
                    if (gMap.getCurrentWorld() != null && gMap.getCurrentWorld().equals(a1.getPlayer().getWorld())) {
                        if (SkyWars.getConfigManager().getSpawn() != null) {
                            a1.getPlayer().teleport(SkyWars.getConfigManager().getSpawn());
                        }
                    }
                }
            }
        }.runTaskLater(SkyWars.get(), 1);

        PlayerStat.getPlayers().add(new PlayerStat(a1.getPlayer()));
    }
}
