package com.nymoout.skywars.utilities;

import com.nymoout.skywars.managers.MatchManager;
import org.bukkit.entity.Player;

public class Serializers {

    public static void sendMessageAutoJoinToSpectators(Player p) {
        ChatSerializerUtil dMessage = new ChatSerializerUtil(
                "§7§m-----------------------------------\n" +
                        "         §e§lGracias por jugar SkyWars\n" +
                        "           ¿§a§lQuieres Jugar de Nuevo?§f\n" +
                        "                                        \n");
        ChatSerializerUtil exit = new ChatSerializerUtil("       §c§lSalir  §8§l●");
        exit.addClickEvent(ChatSerializerUtil.SerializerAction.RUN_COMMAND, "/sw leave");
        exit.addHoverEvent(ChatSerializerUtil.SerializerAction.SHOW_TEXT, "§cSalir de la partida...");
        ChatSerializerUtil play = new ChatSerializerUtil("                    §a§lJugar de nuevo §8§l●\n");
        play.addHoverEvent(ChatSerializerUtil.SerializerAction.SHOW_TEXT, "§aIr a Otra Partida...");
        play.addClickEvent(ChatSerializerUtil.SerializerAction.RUN_COMMAND, "/sw rejoin ");
        dMessage.addExtra(exit);
        dMessage.addExtra(play);
        dMessage.addText("§7§m-----------------------------------");
        dMessage.send(p);
    }

    public static void sendMessageAutoJoinToWinners(Player p) {
        ChatSerializerUtil dMessage = new ChatSerializerUtil(
                "§7§m-----------------------------------\n" +
                        "         §e§lGracias por jugar SkyWars\n" +
                        "           ¿§a§lQuieres Jugar de Nuevo?§f\n" +
                        "                                        \n");
        ChatSerializerUtil exit = new ChatSerializerUtil("       §c§lSalir  §8§l●");
        exit.addClickEvent(ChatSerializerUtil.SerializerAction.RUN_COMMAND, "/sw quit");
        exit.addHoverEvent(ChatSerializerUtil.SerializerAction.SHOW_TEXT, "§cSalir de la partida...");
        ChatSerializerUtil play = new ChatSerializerUtil("                    §a§lJugar de nuevo §8§l●\n");
        play.addHoverEvent(ChatSerializerUtil.SerializerAction.SHOW_TEXT, "§aIr a Otra Partida...");
        play.addClickEvent(ChatSerializerUtil.SerializerAction.RUN_COMMAND, "/sw rejoin ");
        dMessage.addExtra(exit);
        dMessage.addExtra(play);
        dMessage.addText("§7§m-----------------------------------");
        dMessage.send(p);
    }

    public static void sendPartyAccept(Player p) {
        ChatSerializerUtil dMessage = new ChatSerializerUtil(new Messaging.MessageFormatter().format("party.clicktoaccept"));
        dMessage.addClickEvent(ChatSerializerUtil.SerializerAction.RUN_COMMAND, "/swp a");
        dMessage.addHoverEvent(ChatSerializerUtil.SerializerAction.SHOW_TEXT, new Messaging.MessageFormatter().format("party.clicktoaccept"));
        dMessage.send(p);
    }

}
