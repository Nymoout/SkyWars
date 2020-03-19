package com.nymoout.skywars.game;

import org.bukkit.ChatColor;

import java.util.Comparator;

public class GameMapComparator implements Comparator<GameMap> {

    @Override
    public int compare(final GameMap f1, final GameMap f2) {
        if (f1 != null && f2 != null) {
            return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', f1.getDisplayName()))
                    .compareTo(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', f2.getDisplayName())));
        }
        return 0;
    }
}
