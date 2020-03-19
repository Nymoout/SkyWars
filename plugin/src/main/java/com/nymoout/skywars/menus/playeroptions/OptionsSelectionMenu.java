package com.nymoout.skywars.menus.playeroptions;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.PlayerOptions;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class OptionsSelectionMenu {

    private static final String menuName = new Messaging.MessageFormatter().format("menu.options-menu-title");

    public OptionsSelectionMenu(final Player player) {
        int menuSize = 27;
        Inventory inv = Bukkit.createInventory(null, menuSize + 9, menuName);

        if (SkyWars.getConfigManager().glassMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getGlassSlot(), SkyWars.getItemsManager().getItem("glassselect"));
        }

        if (SkyWars.getConfigManager().particleMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getParticleSlot(), SkyWars.getItemsManager().getItem("particleselect"));
        }
        if (SkyWars.getConfigManager().projectileMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getProjectileSlot(), SkyWars.getItemsManager().getItem("projectileselect"));
        }
        if (SkyWars.getConfigManager().killsoundMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getKillSoundSlot(), SkyWars.getItemsManager().getItem("killsoundselect"));
        }
        if (SkyWars.getConfigManager().winsoundMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getWinSoundSlot(), SkyWars.getItemsManager().getItem("winsoundselect"));
        }
        if (SkyWars.getConfigManager().tauntsMenuEnabled()) {
            inv.setItem(SkyWars.getConfigManager().getTauntSlot(), SkyWars.getItemsManager().getItem("tauntselect"));
        }

        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(inv);

        SkyWars.getIconMenuController().create(player, invs, event -> {

            String name = event.getName();

            if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.particle-effect-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.PARTICLEEFFECT, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenParticleMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.projectile-effect-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.PROJECTILEEFFECT, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenProjectileMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.killsound-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.KILLSOUND, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenKillSoundMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.winsound-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.WINSOUND, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenWinSoundMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.glass-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.GLASSCOLOR, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenGlassMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.taunt-sel"))) {
                new OptionSelectionMenu(player, PlayerOptions.TAUNT, false);
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getOpenTauntMenuSound(), 1, 1);
            } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.exit-menu-item"))) {
                player.closeInventory();
            }
        });

        if (player != null) {
            SkyWars.getIconMenuController().show(player, null);
        }
    }
}
