package com.nymoout.skywars.commands.party;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;

public class AcceptCmd extends BaseCmd {

    public AcceptCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "accept";
        alias = new String[]{"a"};
        argLength = 1; //counting cmdName
    }

    @Override
    public boolean run() {

        Party party = Party.getParty(player);

        if (party != null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.alreadyinparty"));
        }

        party = Party.getPartyOfInvite(player);

        if (party == null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.noinvite"));
            return false;
        }

        boolean result = party.acceptInvite(player);

        if (result) {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.youjoined"));
        } else {
            player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", party.getPartyName()).format("party.partyisfull-nojoin"));
        }
        return true;
    }
}