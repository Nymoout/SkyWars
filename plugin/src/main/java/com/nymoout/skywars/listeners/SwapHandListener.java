package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class SwapHandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerSwapHand(PlayerSwapHandItemsEvent event) {
        GameMap gMap = MatchManager.get().getPlayerMap(event.getPlayer());
        if (gMap == null) {
            ItemStack item = event.getOffHandItem();
            if (item.equals(SkyWars.getItemsManager().getItem("optionselect"))
                    || item.equals(SkyWars.getItemsManager().getItem("joinselect"))
                    || item.equals(SkyWars.getItemsManager().getItem("spectateselect"))) {
                event.setCancelled(true);
            }
        } else {
            if (gMap.getMatchState().equals(MatchState.WAITINGSTART) || gMap.getMatchState().equals(MatchState.ENDING)) {
                event.setCancelled(true);
            }
        }
    }

}
