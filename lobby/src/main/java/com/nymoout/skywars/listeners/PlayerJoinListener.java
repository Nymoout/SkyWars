package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.objects.PlayerStat;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (PlayerStat.getPlayerStats(e.getPlayer()) != null) {
            PlayerStat.removePlayer(e.getPlayer().getUniqueId().toString());
        }
        PlayerStat.getPlayers().add(new PlayerStat(e.getPlayer()));
        SkyWars.getNMS().sendTab(e.getPlayer(), (new Messaging.MessageFormatter()).format("tab.header"), (new Messaging.MessageFormatter()).format("tab.footer"));
    }
}
