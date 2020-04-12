package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.utilities.Party;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent a1) {
        final String id = a1.getPlayer().getUniqueId().toString();
        Party party = Party.getParty(a1.getPlayer());
        if (party != null) {
            party.removeMember(a1.getPlayer());
        }
        final GameMap gameMap = MatchManager.get().getPlayerMap(a1.getPlayer());
        if (gameMap == null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerStat.removePlayer(id);
                }
            }.runTaskLater(SkyWars.get(), 5);
            return;
        }

        MatchManager.get().playerLeave(a1.getPlayer(), DamageCause.CUSTOM, true, true, true);

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
