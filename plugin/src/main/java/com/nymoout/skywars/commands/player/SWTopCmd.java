package com.nymoout.skywars.commands.player;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.managers.Leaderboard.LeaderData;
import com.nymoout.skywars.utilities.Messaging;

import java.util.List;

public class SWTopCmd extends BaseCmd {


    public SWTopCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "top";
        alias = new String[]{"leaderboard"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        if (SkyWars.get().getUseable().contains(args[1].toUpperCase())) {
            if (!SkyWars.getLB().loaded(LeaderType.valueOf(args[1].toUpperCase()))) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.updating"));
                return true;
            }
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header"));
            final List<LeaderData> top = SkyWars.getLB().getTopList(LeaderType.valueOf(args[1].toUpperCase()));
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.header2"));
            if (top.size() == 0) {
                player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.no-data"));
            }
            for (int i = 0; i < top.size(); ++i) {
                final LeaderData playerData = top.get(i);
                player.sendMessage(new Messaging.MessageFormatter().setVariable("rank", "" + (i + 1)).
                        setVariable("player", playerData.getName()).
                        setVariable("elo", "" + playerData.getElo()).
                        setVariable("wins", "" + playerData.getWins()).
                        setVariable("losses", "" + playerData.getLoses()).
                        setVariable("kills", "" + playerData.getKills()).
                        setVariable("deaths", "" + playerData.getDeaths()).
                        setVariable("xp", "" + playerData.getXp()).
                        format("leaderboard.player-data"));
            }
            player.sendMessage(new Messaging.MessageFormatter().format("leaderboard.footer"));
            return true;
        } else {
            StringBuilder types = new StringBuilder();
            for (String add : SkyWars.get().getUseable()) {
                types.append(add);
                types.append(", ");
            }
            types.substring(0, types.length() - 2);
            player.sendMessage(new Messaging.MessageFormatter().setVariable("validtypes", types.toString()).format("leaderboard.invalidtype"));
            return false;
        }
    }
}
