package com.nymoout.skywars.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatSerializerUtil {


    private static final String TEXT = "text";
    private static final String EXTRA = "extra";
    private static final String ACTION = "action";
    private static final String VALUE = "value";
    private static final String CLICK_EVENT = "clickEvent";
    private static final String HOVER_EVENT = "hoverEvent";
    private static final String TELLRAW = "tellraw";
    private JsonObject json = new JsonObject();
    private JsonArray extras = new JsonArray();

    public ChatSerializerUtil(String text) {
        this.json.addProperty("text", text);
    }

    public void addText(String text) {
        ChatSerializerUtil extra = new ChatSerializerUtil(text);
        this.addExtra(extra);
    }

    public void addExtra(ChatSerializerUtil extra) {
        if (this.extras.size() == 0) {
            this.json.add("extra", this.extras);
        }

        this.extras.add(extra.getJson());
    }

    private JsonObject getJson() {
        return this.json;
    }

    public void addClickEvent(ChatSerializerUtil.SerializerAction serializerAction, String value) {
        JsonObject clickEvent = new JsonObject();
        clickEvent.addProperty("action", serializerAction.getValue());
        clickEvent.addProperty("value", value);
        this.json.add("clickEvent", clickEvent);
    }

    public void addHoverEvent(ChatSerializerUtil.SerializerAction serializerAction, String value) {
        JsonObject hoverEvent = new JsonObject();
        hoverEvent.addProperty("action", serializerAction.getValue());
        hoverEvent.addProperty("value", value);
        this.json.add("hoverEvent", hoverEvent);
    }

    public String toString() {
        return this.json.toString();
    }

    public String toRawString() {
        return this.toString();
    }

    public void send(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + this.toRawString());
    }

    public static enum SerializerAction {
        SHOW_TEXT("show_text"),
        SUGGEST_COMMAND("suggest_command"),
        RUN_COMMAND("run_command"),
        OPEN_URL("open_url");

        private String value;

        private SerializerAction(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
