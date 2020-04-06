package com.nymoout.skywars.commands;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.admin.ChestEditCmd;
import com.nymoout.skywars.commands.admin.SetSpawnCmd;
import com.nymoout.skywars.commands.player.*;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CmdManager implements CommandExecutor {
    private List<BaseCmd> admincmds = new ArrayList<>();
    private List<BaseCmd> pcmds = new ArrayList<>();

    public CmdManager() {
        admincmds.add(new com.nymoout.skywars.commands.admin.ReloadCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.ChestAddCmd("sw"));
        admincmds.add(new ChestEditCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.SetStatsCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.ClearStatsCmd("sw"));
        admincmds.add(new SetSpawnCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.StartCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.UpdateTopCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.HoloAddCmd("sw"));
        admincmds.add(new com.nymoout.skywars.commands.admin.HoloRemoveCmd("sw"));

        pcmds.add(new SWJoinCmd("sw"));
        pcmds.add(new com.nymoout.skywars.commands.player.SWQuitCmd("sw"));
        pcmds.add(new SWStatsCmd("sw"));
        pcmds.add(new SWTopCmd("sw"));
        pcmds.add(new SWOptionsCmd("sw"));
        pcmds.add(new com.nymoout.skywars.commands.player.SWLeaveSpecCmd("sw"));
        pcmds.add(new com.nymoout.skywars.commands.player.SWReJoinCmd("sw"));
        pcmds.add(new com.nymoout.skywars.commands.player.SWSpectateCmd("sw"));

        if (SkyWars.getConfigManager().winsoundMenuEnabled()) {
            pcmds.add(new com.nymoout.skywars.commands.player.SWWinsoundCmd("sw"));
        }
        if (SkyWars.getConfigManager().killsoundMenuEnabled()) {
            pcmds.add(new com.nymoout.skywars.commands.player.SWKillsoundCmd("sw"));
        }
        if (SkyWars.getConfigManager().tauntsMenuEnabled()) {
            pcmds.add(new SWTauntCmd("sw"));
        }
        if (SkyWars.getConfigManager().projectileMenuEnabled()) {
            pcmds.add(new com.nymoout.skywars.commands.player.SWProjectileCmd("sw"));
        }
        if (SkyWars.getConfigManager().particleMenuEnabled()) {
            pcmds.add(new com.nymoout.skywars.commands.player.SWParticleCmd("sw"));
        }
        if (SkyWars.getConfigManager().glassMenuEnabled()) {
            pcmds.add(new com.nymoout.skywars.commands.player.SWGlassCmd("sw"));
        }

    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0 || getCommands(args[0]) == null) {
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.header"));
            sendHelp(admincmds, s, "1");
            sendHelp(pcmds, s, "2");
            s.sendMessage(new Messaging.MessageFormatter().format("helpList.footer"));
        } else getCommands(args[0]).processCmd(s, args);
        return true;
    }

    private void sendHelp(List<BaseCmd> cmds, CommandSender s, String num) {
        int count = 0;
        for (BaseCmd cmd : cmds) {
            if (Util.get().hasPermissions(cmd.getType(), s, cmd.cmdName)) {
                count++;
                if (count == 1) {
                    s.sendMessage(" ");
                    s.sendMessage(new Messaging.MessageFormatter().format("helpList.sw.header" + num));
                }
                s.sendMessage(new Messaging.MessageFormatter().format("helpList.sw." + cmd.cmdName));
            }
        }
    }

    private BaseCmd getCommands(String s) {
        BaseCmd cmd;
        cmd = getCmd(admincmds, s);
        if (cmd == null) cmd = getCmd(pcmds, s);
        return cmd;
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

