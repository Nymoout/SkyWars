package com.nymoout.skywars.commands.party;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;

public class DeclineCmd extends BaseCmd {

    public DeclineCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "decline";
        alias = new String[]{"d"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {
        Party party = Party.getPartyOfInvite(player);
        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.noinvite"));
            return false;
        }

        boolean result = party.declineInvite(player);
        if (result) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.youdeclined"));
        }
        return true;
    }

}
