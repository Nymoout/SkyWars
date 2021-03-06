package com.nymoout.skywars.commands.admin;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.ChatColor;

public class HoloAddCmd extends BaseCmd {

    public HoloAddCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "hologram";
        alias = new String[]{"h"};
        argLength = 3; //counting cmdName
    }

    @Override
    public boolean run() {
        if (SkyWars.getConfigManager().hologramsEnabled()) {
            LeaderType type = LeaderType.matchType(args[1].toUpperCase());
            if (type == null || !SkyWars.get().getUseable().contains(type.toString())) {
                StringBuilder types = new StringBuilder();
                for (String add : SkyWars.get().getUseable()) {
                    types.append(add);
                    types.append(", ");
                }
                types.substring(0, types.length() - 2);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
                return false;
            }
            String format = args[2];
            if (SkyWars.getHoloManager().getFormats(type).contains(format)) {
                SkyWars.getHoloManager().createLeaderHologram(player.getEyeLocation(), type, format);
                return true;
            }

            StringBuilder formats = new StringBuilder();
            for (String add : SkyWars.getHoloManager().getFormats(type)) {
                formats.append(add);
                formats.append(", ");
            }
            formats.substring(0, formats.length() - 2);
            player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", formats.toString()).format("leaderboard.invalidformat"));
            return false;
        }
        player.sendMessage(ChatColor.RED + "Holograms are not enabled!");
        return false;
    }

}
