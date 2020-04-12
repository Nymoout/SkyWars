package com.nymoout.skywars.managers;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.*;

public class Leaderboard {

    private static HashMap<LeaderType, List<LeaderData>> topLeaders = new HashMap<>();
    private static HashMap<LeaderType, ArrayList<LeaderData>> leaders = new HashMap<>();
    private static HashMap<LeaderType, Boolean> loaded = new HashMap<>();
    private static HashMap<LeaderType, HashMap<Integer, ArrayList<Location>>> signs = new HashMap<>();

    public Leaderboard() {
        loaded.put(LeaderType.DEATHS, false);
        loaded.put(LeaderType.ELO, false);
        loaded.put(LeaderType.WINS, false);
        loaded.put(LeaderType.KILLS, false);
        loaded.put(LeaderType.LOSSES, false);
        loaded.put(LeaderType.XP, false);

        for (LeaderType type : LeaderType.values()) {
            if (SkyWars.getConfigManager().isTypeEnabled(type)) {
                leaders.put(type, new ArrayList<>());
                if (SkyWars.getConfigManager().leaderSignsEnabled()) {
                    signs.put(type, new HashMap<>());
                    getSigns(type);
                }
            }
        }

        SkyWars.get().getServer().getScheduler().scheduleSyncRepeatingTask(SkyWars.get(), () -> {
            for (LeaderType type : LeaderType.values()) {
                if (SkyWars.getConfigManager().isTypeEnabled(type)) {
                    DataStorage.get().updateTop(type, SkyWars.getConfigManager().getLeaderSize());
                }
            }
        }, 0, SkyWars.getConfigManager().getUpdateTime() * 20);
    }

    public void addLeader(LeaderType type, String uuid, String name, int wins, int loses, int kills, int deaths, int elo, int xp) {
        leaders.get(type).add(new LeaderData(uuid, name, wins, loses, kills, deaths, elo, xp));
    }

    public void resetLeader(LeaderType type) {
        leaders.get(type).clear();
    }

    public void finishedLoading(LeaderType type) {
        loaded.put(type, false);
        topLeaders.remove(type);
        topLeaders.put(type, getTop(SkyWars.getConfigManager().getLeaderSize(), type));
        loaded.put(type, true);
        if (SkyWars.getConfigManager().leaderSignsEnabled() && SkyWars.get().isEnabled()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateSigns(type);
                    if (SkyWars.get().serverLoaded() && SkyWars.getConfigManager().hologramsEnabled()) {
                        SkyWars.getHoloManager().updateLeaderHolograms(type);
                    }
                }
            }.runTaskLater(SkyWars.get(), 1);
        }
    }

    public boolean loaded(LeaderType type) {
        return loaded.get(type);
    }

    private List<LeaderData> getTop(final int top, LeaderType type) {
        final ArrayList<LeaderData> pData = new ArrayList<>(leaders.get(type));
        pData.sort(new RankComparator(type));
        return pData.subList(0, (top > pData.size()) ? pData.size() : top);
    }

    public List<LeaderData> getTopList(LeaderType type) {
        return topLeaders.get(type);
    }

    private void getSigns(LeaderType type) {
        File leaderboardsFile = new File(SkyWars.get().getDataFolder(), "leaderboards.yml");

        if (!leaderboardsFile.exists()) {
            SkyWars.get().saveResource("leaderboards.yml", false);
        }

        if (leaderboardsFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(leaderboardsFile);
            for (int i = 1; i < 11; i++) {
                List<String> locations = storage.getStringList(type.toString().toLowerCase() + ".signs." + i);
                if (locations != null) {
                    ArrayList<Location> locs = new ArrayList<>();
                    for (String location : locations) {
                        Location loc = Util.get().stringToLocation(location);
                        locs.add(loc);
                    }
                    signs.get(type).put(i, locs);
                }
            }
        }

    }

    private void saveSigns(LeaderType type) {
        File leaderboardsFile = new File(SkyWars.get().getDataFolder(), "leaderboards.yml");

        if (!leaderboardsFile.exists()) {
            SkyWars.get().saveResource("leaderboards.yml", false);
        }

        if (leaderboardsFile.exists()) {
            FileConfiguration storage = YamlConfiguration.loadConfiguration(leaderboardsFile);
            for (int pos : signs.get(type).keySet()) {
                if (signs.get(type).get(pos) != null) {
                    List<String> locs = new ArrayList<>();
                    for (Location loc : signs.get(type).get(pos)) {
                        locs.add(Util.get().locationToString(loc));
                    }
                    storage.set(type.toString().toLowerCase() + ".signs." + pos, locs);
                }
            }
            try {
                storage.save(leaderboardsFile);
            } catch (IOException e) {
                SkyWars.get().getLogger().info("[ERROR] Failed to save leaderboards file");
            }
        }

        if (loaded(type)) {
            updateSigns(type);
        }
    }

    public void addLeaderSign(int pos, LeaderType type, Location loc) {
        signs.get(type).get(pos).add(loc);
        saveSigns(type);
    }

    public boolean removeLeaderSign(Location loc) {
        for (LeaderType type : signs.keySet()) {
            for (int pos : signs.get(type).keySet()) {
                if (signs.get(type).get(pos).contains(loc)) {
                    signs.get(type).get(pos).remove(loc);
                    saveSigns(type);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateSigns(LeaderType type) {
        List<LeaderData> top = getTopList(type);
        if (top != null) {
            for (int pos : signs.get(type).keySet()) {
                if (pos - 1 < top.size()) {
                    for (Location loc : signs.get(type).get(pos)) {
                        BlockState bs = loc.getBlock().getState();
                        Sign sign = null;
                        if (bs instanceof Sign) {
                            sign = (Sign) bs;
                        }
                        if (sign != null) {
                            sign.getBlock().getChunk().load();
                            for (int i = 0; i < 4; i++) {
                                sign.setLine(i, new Messaging.MessageFormatter().setVariable("name", top.get(pos - 1).getName())
                                        .setVariable("elo", "" + top.get(pos - 1).getElo())
                                        .setVariable("kills", "" + top.get(pos - 1).getKills())
                                        .setVariable("wins", "" + top.get(pos - 1).getWins())
                                        .setVariable("xp", "" + top.get(pos - 1).getXp())
                                        .setVariable("deaths", "" + top.get(pos - 1).getDeaths())
                                        .setVariable("losses", "" + top.get(pos - 1).getLoses())
                                        .setVariable("position", "" + pos)
                                        .setVariable("type", type.toString())
                                        .format("leaderboard.signformats." + type.toString().toLowerCase() + ".line" + (i + 1)));
                            }
                            sign.update();
                            if (SkyWars.getConfigManager().leaderHeadsEnabled()) {
                                updateHead(sign, top.get(pos - 1).getUUID());
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateHead(Sign sign, UUID uuid) {
        Block b = sign.getBlock();
        org.bukkit.material.Sign meteSign;
        meteSign = (org.bukkit.material.Sign) b.getState().getData();
        BlockFace facing = meteSign.getFacing();
        Block h1 = b.getRelative(BlockFace.UP, 1);
        Block h2 = b.getRelative(BlockFace.UP, 1);
        if (facing.equals(BlockFace.EAST)) {
            h2 = b.getRelative(-1, 1, 0);
        }
        if (facing.equals(BlockFace.WEST)) {
            h2 = b.getRelative(1, 1, 0);
        }
        if (facing.equals(BlockFace.SOUTH)) {
            h2 = b.getRelative(0, 1, -1);
        }
        if (facing.equals(BlockFace.NORTH)) {
            h2 = b.getRelative(0, 1, 1);
        }
        if (SkyWars.getNMS().headCheck(h1)) {
            Skull skull = (Skull) h1.getState();
            SkyWars.getNMS().updateSkull(skull, uuid);
            skull.update();
        }
        if (SkyWars.getNMS().headCheck(h2)) {
            Skull skull = (Skull) h2.getState();
            SkyWars.getNMS().updateSkull(skull, uuid);
            skull.update();
        }
    }

    public class RankComparator implements Comparator<LeaderData> {
        private LeaderType type;

        RankComparator(LeaderType deaths) {
            type = deaths;
        }

        @Override
        public int compare(final LeaderData f1, final LeaderData f2) {
            if (type.equals(LeaderType.DEATHS)) {
                return f2.getDeaths() - f1.getDeaths();
            } else if (type.equals(LeaderType.KILLS)) {
                if (f2.getKills() == f1.getKills()) {
                    return f2.getElo() - f1.getElo();
                }
                return f2.getKills() - f1.getKills();
            } else if (type.equals(LeaderType.LOSSES)) {
                return f2.getLoses() - f1.getLoses();
            } else if (type.equals(LeaderType.WINS)) {
                if (f2.getWins() == f1.getWins()) {
                    return f2.getElo() - f1.getElo();
                }
                return f2.getWins() - f1.getWins();
            } else if (type.equals(LeaderType.XP)) {
                return f2.getXp() - f1.getXp();
            } else {
                if (f2.getElo() == f1.getElo()) {
                    return f2.getWins() - f1.getWins();
                }
                return f2.getElo() - f1.getElo();
            }
        }
    }

    public class LeaderData {
        private String uuid;
        private String name;
        private int wins;
        private int loses;
        private int kills;
        private int deaths;
        private int elo;
        private int xp;

        LeaderData(String uuid, String name, int wins, int loses, int kills, int deaths, int elo, int xp) {
            this.uuid = uuid;
            this.name = name;
            this.wins = wins;
            this.loses = loses;
            this.kills = kills;
            this.deaths = deaths;
            this.elo = elo;
            this.xp = xp;
        }

        public String getName() {
            return name;
        }

        public int getWins() {
            return wins;
        }

        public int getLoses() {
            return loses;
        }

        public int getKills() {
            return kills;
        }

        public int getDeaths() {
            return deaths;
        }

        public int getElo() {
            return elo;
        }

        public int getXp() {
            return xp;
        }

        public UUID getUUID() {
            return UUID.fromString(uuid);
        }
    }
}