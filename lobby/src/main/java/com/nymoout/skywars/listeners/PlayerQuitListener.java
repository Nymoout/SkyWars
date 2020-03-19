package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.objects.PlayerStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent a1) {
        final String id = a1.getPlayer().getUniqueId().toString();
        if (PlayerStat.getPlayerStats(id) != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerStat.removePlayer(id);
                }
            }.runTaskLater(SkyWars.get(), 20);
        }
    }
}
