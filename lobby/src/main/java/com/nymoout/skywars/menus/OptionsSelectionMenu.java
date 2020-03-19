package com.nymoout.skywars.menus;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class OptionsSelectionMenu {

    private static int menuSize = 27;
    private static final String menuName = new Messaging.MessageFormatter().format("menu.options-menu-title");
    
    public OptionsSelectionMenu(final Player player) {

        SkyWars.getIC().create(player, menuName, menuSize, new IconMenu.OptionClickEventHandler() {
			@Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                
                String name = event.getName();
                event.setWillClose(false);
                event.setWillDestroy(false); 
                if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.particle-effect-sel"))) {
                	new com.nymoout.skywars.menus.EffectSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenParticleMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.projectile-effect-sel"))) {
                	new ProjSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenProjectileMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.killsound-sel"))) {
                	new KillSoundSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenKillSoundMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.winsound-sel"))) {
                	new WinSoundSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenWinSoundMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.glass-sel"))) {
                	new ColorSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenGlassMenuSound(), 1, 1);
                } else if (name.equalsIgnoreCase(new Messaging.MessageFormatter().format("items.taunt-sel"))) {
                	new TauntSelectionMenu(player);
                	Util.get().playSound(player, player.getLocation(), SkyWars.getCfg().getOpenTauntMenuSound(), 1, 1);
                } else {
                	return;
                }
            }
        });

        List<String> loreList = Lists.newLinkedList();

        if (SkyWars.getCfg().glassMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("glassselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        9,
                        SkyWars.getIM().getItem("glassselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("glassselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
        if (SkyWars.getCfg().particleMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("particleselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        11,
                        SkyWars.getIM().getItem("particleselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("particleselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
        if (SkyWars.getCfg().projectileMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("projectileselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        12,
                        SkyWars.getIM().getItem("projectileselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("projectileselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
        if (SkyWars.getCfg().killsoundMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("killsoundselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        14,
                        SkyWars.getIM().getItem("killsoundselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("killsoundselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
        if (SkyWars.getCfg().winsoundMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("winsoundselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        15,
                        SkyWars.getIM().getItem("winsoundselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("winsoundselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
        if (SkyWars.getCfg().tauntsMenuEnabled()) {
        	if (player != null) {
                loreList = SkyWars.getIM().getItem("tauntselect").getItemMeta().getLore();
                SkyWars.getIC().setOption(
                        player,
                        17,
                        SkyWars.getIM().getItem("tauntselect"),
                        SkyWars.getNMS().getItemName(SkyWars.getIM().getItem("tauntselect")),
                        loreList.toArray(new String[loreList.size()]));
            }
        }
                
        if (player != null) {
            SkyWars.getIC().show(player);
        }
    }
}
