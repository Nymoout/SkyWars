package com.nymoout.skywars.commands.party;

import com.nymoout.skywars.commands.BaseCmd;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;

public class CreateCmd extends BaseCmd {

    public CreateCmd(String t) {
        type = t;
        forcePlayer = true;
        cmdName = "create";
        alias = new String[]{"c"};
        argLength = 2; //counting cmdName
    }

    @Override
    public boolean run() {
        String partyName = args[1];
        Party party = Party.getParty(player);
        if (party != null) {
            player.sendMessage(new Messaging.MessageFormatter().format("party.alreadyinparty"));
            return false;
        }

        new Party(player, partyName);
        player.sendMessage(new Messaging.MessageFormatter().setVariable("partyname", partyName).format("party.create"));
        return true;
    }

}
