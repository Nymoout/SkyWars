package com.nymoout.skywars.commands;


import com.nymoout.skywars.commands.party.DeclineCmd;
import com.nymoout.skywars.commands.party.LeaveCmd;
import com.nymoout.skywars.commands.party.*;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PartyCmdManager implements CommandExecutor {

    private List<BaseCmd> partycmds = new ArrayList<>();

    //Add New Commands Here
    public PartyCmdManager() {
        partycmds.add(new AcceptCmd("party"));
        partycmds.add(new CreateCmd("party"));
        partycmds.add(new DisbandCmd("party"));
        partycmds.add(new InviteCmd("party"));
        partycmds.add(new LeaveCmd("party"));
        partycmds.add(new NameCmd("party"));
        partycmds.add(new InfoCmd("party"));
        partycmds.add(new DeclineCmd("party"));
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommands(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(partycmds, s);
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommands(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hasPermissions(cmd.getType(), s, cmd.cmdName)) {
                count++;
                if (count == 1) {
                    s.sendMessage(" ");
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.swparty.header" + 1));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.swparty." + cmd.cmdName));
            }
        }
    }

    private BaseCmd getCommands(String s) {
        return getCmd(partycmds, s);
    }

    private BaseCmd getCmd(List<BaseCmd> cmds, String s) {
        for (BaseCmd cmd : cmds) {
            if (cmd.cmdName.equalsIgnoreCase(s)) {
                return cmd;
            }
            for (String alias : cmd.alias) {
                if (alias.equalsIgnoreCase(s))
                    return cmd;
            }
        }
        return null;
    }
}

