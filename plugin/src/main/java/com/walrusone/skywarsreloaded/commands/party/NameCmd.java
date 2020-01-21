package com.walrusone.skywarsreloaded.commands.party;

import com.walrusone.skywarsreloaded.commands.BaseCmd;
import com.walrusone.skywarsreloaded.utilities.Messaging;
import com.walrusone.skywarsreloaded.utilities.Party;

public class NameCmd extends BaseCmd {

    public NameCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "name";
        alias = new String[]{"n"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        Party party = Party.getParty(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.notinaparty"));
            return false;
        }

        if (!party.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.mustbepartyleader"));
            return false;
        }

        String partyName = args[1];
        party.setPartyName(partyName);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", partyName).format("party.rename"));
        return true;
    }

}
