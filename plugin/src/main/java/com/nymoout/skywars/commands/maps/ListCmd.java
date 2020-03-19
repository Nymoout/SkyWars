package com.nymoout.skywars.commands.maps;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.ChatColor;

public class ListCmd extends BaseCmd {

    public ListCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "list";
        alias = new String[]{"l"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        sender.sendMessage(new Messaging.MessageFormatter().format("maps.listHeader"));
        for (GameMap map : GameMap.getMaps()) {
            if (map.isRegistered()) {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("filename", map.getName()).setVariable("displayname", map.getDisplayName()).setVariable("status", ChatColor.GREEN + "REGISTERED").format("maps.listResult"));
            } else {
                sender.sendMessage(new Messaging.MessageFormatter().setVariable("filename", map.getName()).setVariable("displayname", map.getDisplayName()).setVariable("status", ChatColor.RED + "UNREGISTERED").format("maps.listResult"));
            }
        }
        return true;
    }

}
