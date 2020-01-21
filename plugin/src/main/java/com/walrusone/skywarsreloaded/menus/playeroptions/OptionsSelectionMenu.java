package com.walrusone.skywarsreloaded.menus.playeroptions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.PlayerOptions;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Util;

public class OptionsSelectionMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.options-menu-title");
    
    public OptionsSelectionMenu(final Player player) {
        int menuSize = 27;
        Inventory inv = Bukkit.createInventory(null, menuSize + 9, menuName);

        if (SkyWarsReloaded.getConfigManager().glassMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getGlassSlot(), SkyWarsReloaded.getItemsManager().getItem("glassselect"));
        }
        	
        if (SkyWarsReloaded.getConfigManager().particleMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getParticleSlot(), SkyWarsReloaded.getItemsManager().getItem("particleselect"));
        }
        if (SkyWarsReloaded.getConfigManager().projectileMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getProjectileSlot(), SkyWarsReloaded.getItemsManager().getItem("projectileselect"));
        }
        if (SkyWarsReloaded.getConfigManager().killsoundMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getKillSoundSlot(), SkyWarsReloaded.getItemsManager().getItem("killsoundselect"));
        }
        if (SkyWarsReloaded.getConfigManager().winsoundMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getWinSoundSlot(), SkyWarsReloaded.getItemsManager().getItem("winsoundselect"));
        }
        if (SkyWarsReloaded.getConfigManager().tauntsMenuEnabled()) {
        	inv.setItem(SkyWarsReloaded.getConfigManager().getTauntSlot(), SkyWarsReloaded.getItemsManager().getItem("tauntselect"));
        }
               
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(inv);

        SkyWarsReloaded.getIconMenuController().create(player, invs, event -> {

            String name = event.getName();

            if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.particle-effect-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.PARTICLEEFFECT, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenParticleMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.projectile-effect-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.PROJECTILEEFFECT, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenProjectileMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.killsound-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.KILLSOUND, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenKillSoundMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.winsound-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.WINSOUND, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenWinSoundMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.glass-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.GLASSCOLOR, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenGlassMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.taunt-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.TAUNT, false);
                Util.get().playSound(player, player.getLocation(), SkyWarsReloaded.getConfigManager().getOpenTauntMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.exit-menu-item"))) {
                player.closeInventory();
            }
        });
                
        if (player != null) {
            SkyWarsReloaded.getIconMenuController().show(player, null);
        }
    }
}
