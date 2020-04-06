package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPrepocessListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPrepocess(final PlayerCommandPreprocessEvent e) {
        GameMap gMap = MatchManager.get().getSpectatorMap(e.getPlayer());
        String[] splited = e.getMessage().split("\\s+");
        if (gMap != null) {
            if (splited[0].equalsIgnoreCase("/spawn")) {
                e.setCancelled(true);
                gMap.getSpectators().remove(e.getPlayer().getUniqueId());
                MatchManager.get().removeSpectator(e.getPlayer());
                return;
            }
            if (SkyWars.getConfigManager().disableCommandsSpectate()) {
                if (e.getPlayer().hasPermission("sw.allowcommands")) {
                    return;
                }
                for (final String a1 : SkyWars.getConfigManager().getEnabledCommandsSpectate()) {
                    if (splited.length == 1) {
                        if (splited[0].equalsIgnoreCase("/" + a1)) {
                            return;
                        }
                    } else {
                        if (splited[0].equalsIgnoreCase("/" + a1) || (splited[0] + " " + splited[1]).equalsIgnoreCase("/" + a1)) {
                            return;
                        }
                    }
                }
                e.getPlayer().sendMessage(new Messaging.MessageFormatter().format("game.command-disabled-spec"));
                e.setCancelled(true);
                return;
            }
        }

        if (MatchManager.get().getPlayerMap(e.getPlayer()) != null) {
            if (e.getPlayer().hasPermission("sw.allowcommands")) {
                return;
            }
            for (final String a1 : SkyWars.getConfigManager().getEnabledCommands()) {
                if (splited.length == 1) {
                    if (splited[0].equalsIgnoreCase("/" + a1)) {
                        return;
                    }
                } else if (splited.length > 1) {
                    if (splited[0].equalsIgnoreCase("/" + a1) || (splited[0] + " " + splited[1]).equalsIgnoreCase("/" + a1)) {
                        return;
                    }
                }
            }
            e.getPlayer().sendMessage(new Messaging.MessageFormatter().format("game.command-disabled"));
            e.setCancelled(true);
        }
    }
}
