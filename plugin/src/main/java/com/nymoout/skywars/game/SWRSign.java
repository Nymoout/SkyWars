package com.nymoout.skywars.game;

import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Messaging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class SWRSign {
    private String gameName;
    private Location location;

    SWRSign(String name, Location loc) {
        this.gameName = name;
        this.location = loc;
    }

    public void update() {
        GameMap gMap = GameMap.getMap(this.gameName);
        Location loc = this.location;
        BlockState bs = loc.getBlock().getState();
        Sign sign;
        if (bs instanceof Sign) {
            sign = (Sign) bs;
            Block b = sign.getBlock();
            org.bukkit.material.Sign meteSign;
            meteSign = (org.bukkit.material.Sign) b.getState().getData();
            Block attachedBlock = b.getRelative(meteSign.getAttachedFace());
            setMaterial(gMap, attachedBlock);
            String state = "";
            if (gMap == null || gMap.getMatchState().equals(MatchState.OFFLINE)) {
                state = new Messaging.MessageFormatter().format("signs.offline");
            } else if (gMap.getMatchState().equals(MatchState.WAITINGSTART)) {
                state = new Messaging.MessageFormatter().format("signs.joinable");
            } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                state = new Messaging.MessageFormatter().format("signs.playing");
            } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                state = new Messaging.MessageFormatter().format("signs.ending");
            }
            sign.getBlock().getChunk().load();
            if (gMap != null) {
                String team = "";
                if (gMap.getTeamSize() > 1) {
                    team = "team";
                }
                sign.setLine(0, new Messaging.MessageFormatter().setVariable("matchstate", state).
                        setVariable("mapname", gMap.getDisplayName().toUpperCase()).
                        setVariable("playercount", "" + gMap.getPlayerCount()).
                        setVariable("maxplayers", "" + gMap.getMaxPlayers()).
                        setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line1" + team));
                sign.setLine(1, new Messaging.MessageFormatter().setVariable("matchstate", state).
                        setVariable("mapname", gMap.getDisplayName().toUpperCase()).
                        setVariable("playercount", "" + gMap.getPlayerCount()).
                        setVariable("maxplayers", "" + gMap.getMaxPlayers()).
                        setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line2" + team));
                sign.setLine(2, new Messaging.MessageFormatter().setVariable("matchstate", state).
                        setVariable("mapname", gMap.getDisplayName().toUpperCase()).
                        setVariable("playercount", "" + gMap.getPlayerCount()).
                        setVariable("maxplayers", "" + gMap.getMaxPlayers()).
                        setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line3" + team));
                sign.setLine(3, new Messaging.MessageFormatter().setVariable("matchstate", state).
                        setVariable("mapname", gMap.getDisplayName().toUpperCase()).
                        setVariable("playercount", "" + gMap.getPlayerCount()).
                        setVariable("maxplayers", "" + gMap.getMaxPlayers()).
                        setVariable("teamsize", "" + gMap.getTeamSize()).format("signs.line4" + team));
            }
            sign.update();
        }
    }

    private static void setMaterial(GameMap gMap, Block attachedBlock) {
        attachedBlock.getWorld().loadChunk(attachedBlock.getChunk());
        if (gMap == null) {
            updateBlock(attachedBlock, "blockoffline");
        } else if (gMap.getMatchState().equals(MatchState.WAITINGSTART)) {
            updateBlock(attachedBlock, "blockwaiting");
        } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
            updateBlock(attachedBlock, "blockplaying");
        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
            updateBlock(attachedBlock, "blockending");
        } else if (gMap.getMatchState().equals(MatchState.OFFLINE)) {
            updateBlock(attachedBlock, "blockoffline");
        }
    }

    @SuppressWarnings("deprecation")
    private static void updateBlock(Block block, String item) {
        block.setType(SkyWars.getItemsManager().getItem(item).getType());
        if (SkyWars.getNMS().getVersion() < 13) {
            if (SkyWars.getItemsManager().getItem(item).getType().equals(Material.valueOf("WOOL")) || SkyWars.getItemsManager().getItem(item).getType().equals(Material.valueOf("STAINED_GLASS")) || SkyWars.getItemsManager().getItem(item).getType().equals(Material.valueOf("STAINED_CLAY"))) {
                SkyWars.getNMS().setBlockWithColor(block.getWorld(), block.getX(), block.getY(), block.getZ(), SkyWars.getItemsManager().getItem(item).getType(), SkyWars.getItemsManager().getItem(item).getData().getData());
            }
        }

    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.gameName;
    }
}
