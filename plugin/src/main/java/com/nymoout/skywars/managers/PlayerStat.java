package com.nymoout.skywars.managers;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.database.DataStorage;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import com.nymoout.skywars.utilities.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStat {

    private static ArrayList<PlayerStat> players;
    private static HashMap<Player, Scoreboard> scoreboards = new HashMap<>();
    private final String uuid;
    private String playername;
    private int wins;
    private int losts;
    private int kills;
    private int deaths;
    private int elo;
    private int xp;
    private String particleEffect;
    private String projectileEffect;
    private String glassColor;
    private String deathEffect;
    private String killSound;
    private String winSound;
    private String taunt;
    private boolean initialized;
    private PermissionAttachment perms;

    public PlayerStat(final Player player) {
        this.initialized = false;
        this.uuid = player.getUniqueId().toString();
        this.playername = player.getName();
        this.perms = player.addAttachment(SkyWars.get());
        DataStorage.get().loadStats(this);
        if (SkyWars.getConfigManager().economyEnabled()) {
            DataStorage.get().loadperms(this);
        }
        if (SkyWars.getConfigManager().getSpawn() != null) {
            if (player.getWorld().equals(SkyWars.getConfigManager().getSpawn().getWorld())) {
                updatePlayer(uuid);
            }
        }
        saveStats(uuid);
    }

    private void saveStats(final String uuid) {
        new BukkitRunnable() {
            public void run() {
                PlayerStat ps = PlayerStat.getPlayerStats(uuid);
                if (ps == null) {
                    this.cancel();
                } else if (ps.isInitialized()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (SkyWars.getConfigManager().bungeeMode()) {
                                Player player = SkyWars.get().getServer().getPlayer(UUID.fromString(uuid));
                                if (player != null) {
                                    boolean joined = MatchManager.get().joinGame(player, GameType.ALL);
                                    if (!joined) {
                                        SkyWars.get().sendBungeeMsg(player, "Connect", SkyWars.getConfigManager().getBungeeLobby());
                                    }
                                }
                            }
                        }
                    }.runTask(SkyWars.get());

                    DataStorage.get().saveStats(PlayerStat.getPlayerStats(uuid));
                } else {
                    saveStats(uuid);
                }
            }
        }.runTaskLaterAsynchronously(SkyWars.get(), 10L);
    }

    public static void updatePlayer(final String uuid) {
        new BukkitRunnable() {
            public void run() {
                PlayerStat ps = PlayerStat.getPlayerStats(uuid);
                if (ps == null) {
                    this.cancel();
                } else if (ps.isInitialized()) {
                    final Player player = SkyWars.get().getServer().getPlayer(UUID.fromString(uuid));
                    if (player != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (Util.get().isSpawnWorld(player.getWorld())) {
                                    if (SkyWars.getConfigManager().protectLobby()) {
                                        player.setGameMode(GameMode.ADVENTURE);
                                        player.setHealth(20);
                                        player.setFoodLevel(20);
                                        player.setSaturation(20);
                                        player.setFireTicks(0);
                                        player.resetPlayerTime();
                                        player.resetPlayerWeather();
                                    }
                                    PlayerStat pStats = PlayerStat.getPlayerStats(player);
                                    if (pStats != null && SkyWars.getConfigManager().displayPlayerExeperience()) {
                                        Util.get().setPlayerExperience(player, pStats.getXp());
                                    }
                                    if (SkyWars.get().isEnabled() && SkyWars.getConfigManager().lobbyBoardEnabled()) {
                                        getScoreboard(player);
                                        player.setScoreboard(getPlayerScoreboard(player));
                                    }
                                    if (SkyWars.getConfigManager().optionsMenuEnabled()) {
                                        player.getInventory().setItem(SkyWars.getConfigManager().getOptionsSlot(), SkyWars.getItemsManager().getItem("optionselect"));
                                    }
                                    if (SkyWars.getConfigManager().joinMenuEnabled() && player.hasPermission("sw.join")) {
                                        player.getInventory().setItem(SkyWars.getConfigManager().getJoinSlot(), SkyWars.getItemsManager().getItem("joinselect"));
                                    }
                                    if (SkyWars.getConfigManager().spectateMenuEnabled() && player.hasPermission("sw.spectate")) {
                                        player.getInventory().setItem(SkyWars.getConfigManager().getSpectateSlot(), SkyWars.getItemsManager().getItem("spectateselect"));
                                    }
                                    if (SkyWars.getConfigManager().autoJoinEnable()) {
                                        player.getInventory().setItem(SkyWars.getConfigManager().getAutojoinSlot(), SkyWars.getItemsManager().getItem("autojoinselect"));
                                    }
                                    player.updateInventory();
                                }
                            }
                        }.runTask(SkyWars.get());
                    } else {
                        this.cancel();
                    }
                } else {
                    updatePlayer(uuid);
                }
            }
        }.runTaskLaterAsynchronously(SkyWars.get(), 10L);
    }

    public static ArrayList<PlayerStat> getPlayers() {
        return PlayerStat.players;
    }

    public static void setPlayers(final ArrayList<PlayerStat> playerData) {
        PlayerStat.players = playerData;
    }

    public static PlayerStat getPlayerStats(final String playerData) {
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(playerData)) {
                return pData;
            }
        }
        return null;
    }

    public static PlayerStat getPlayerStats(final Player player) {
        String uuid = player.getUniqueId().toString();
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(uuid)) {
                return pData;
            }
        }
        return null;
    }

    public static PlayerStat getPlayerStats(final UUID uuid) {
        for (final PlayerStat pData : getPlayers()) {
            if (pData.getId().equals(uuid.toString())) {
                return pData;
            }
        }
        return null;
    }

    public String getId() {
        return this.uuid;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(final int a1) {
        this.wins = a1;
    }

    public int getKills() {
        return this.kills;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int x) {
        this.xp = x;
    }

    public void setKills(final int a1) {
        this.kills = a1;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(final int a1) {
        this.deaths = a1;
    }

    public int getElo() {
        return this.elo;
    }

    public void setElo(final int a1) {
        this.elo = a1;
    }

    public int getLosses() {
        return this.losts;
    }

    public void setLosts(final int a1) {
        this.losts = a1;
    }

    static {
        PlayerStat.players = new ArrayList<>();
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(final boolean a1) {
        this.initialized = a1;
    }

    public void clear() {
        this.losts = 0;
        this.wins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.elo = 1500;
    }

    public void setParticleEffect(String effect) {
        this.particleEffect = effect;
    }

    public String getParticleEffect() {
        return particleEffect;
    }

    public void setProjectileEffect(String effect) {
        this.projectileEffect = effect;
    }

    public String getProjectileEffect() {
        return projectileEffect;
    }

    public void setGlassColor(String glassC) {
        this.glassColor = glassC;
    }

    public String getGlassColor() {
        return this.glassColor;
    }

    public void setDeathEffect(String dEffect) {
        this.deathEffect = dEffect;
    }

    public String getDeathEffect() {
        return deathEffect;
    }

    public void setKillSound(String glassC) {
        this.killSound = glassC;
    }

    public String getKillSound() {
        return this.killSound;
    }

    public String getWinSound() {
        return this.winSound;
    }

    public String getPlayerName() {
        return playername;
    }

    public void setWinSound(String string) {
        this.winSound = string;
    }

    public void setTaunt(String string) {
        taunt = string;
    }

    public String getTaunt() {
        return taunt;
    }

    //Scoreboard Methods

    private static void getScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (scoreboard != null) {
            resetScoreboard(player);
        }
        ScoreboardManager manager = SkyWars.get().getServer().getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        Objective objective = SkyWars.getNMS().getNewObjective(scoreboard, "dummy", "info");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboards.put(player, scoreboard);
        updateScoreboard(player);
    }

    public static void updateScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        if (scoreboard == null) {
            getScoreboard(player);
            scoreboard = scoreboards.get(player);
        }
        for (Objective objective : scoreboard.getObjectives()) {
            if (objective != null) {
                objective.unregister();
            }
        }

        Objective objective = SkyWars.getNMS().getNewObjective(scoreboard, "dummy", "info");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        String sb = "scoreboards.lobbyboard.line";
        ArrayList<String> scores = new ArrayList<>();
        for (int i = 1; i < 17; i++) {
            if (i == 1) {
                String leaderboard = getScoreboardLine(sb + i, player);
                objective.setDisplayName(leaderboard);
            } else {
                String s = getScoreboardLine(sb + i, player);
                while (scores.contains(s) && !s.equalsIgnoreCase("remove")) {
                    s = s + " ";
                }
                scores.add(s);
                if (!s.equalsIgnoreCase("remove")) {
                    Score score = objective.getScore(s);
                    score.setScore(17 - i);
                }
            }
        }
    }

    private static String getScoreboardLine(String lineNum, Player player) {
        PlayerStat ps = PlayerStat.getPlayerStats(player);
        String killdeath;
        String winloss;
        if (ps != null) {
            if (ps.getWins() == 0) {
                winloss = "0.00";
            } else {
                winloss = String.format("%1$,.2f", ((double) ((double) ps.getWins() / (double) ps.getLosses())));
            }
            if (ps.getKills() == 0) {
                killdeath = "0.00";
            } else {
                killdeath = String.format("%1$,.2f", ((double) ((double) ps.getKills() / (double) ps.getDeaths())));
            }

            return new Messaging.MessageFormatter()
                    .setVariable("elo", Integer.toString(ps.getElo()))
                    .setVariable("wins", Integer.toString(ps.getWins()))
                    .setVariable("losses", Integer.toString(ps.getLosses()))
                    .setVariable("kills", Integer.toString(ps.getKills()))
                    .setVariable("deaths", Integer.toString(ps.getDeaths()))
                    .setVariable("xp", Integer.toString(ps.getXp()))
                    .setVariable("killdeath", killdeath)
                    .setVariable("winloss", winloss)
                    .setVariable("balance", "" + getBalance(player))
                    .setVariable("player_name", ps.getPlayerName())
                    .setVariable("online_players", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()))
                    .format(lineNum);
        }
        return "";
    }

    private static double getBalance(Player player) {
        if (SkyWars.getConfigManager().economyEnabled()) {
            return VaultUtils.get().getBalance(player);
        }
        return 0;
    }

    private static void resetScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player);
        for (Objective objective : scoreboard.getObjectives()) {
            if (objective != null) {
                objective.unregister();
            }
        }
    }

    private static Scoreboard getPlayerScoreboard(Player player) {
        return scoreboards.get(player);
    }

    public PermissionAttachment getPerms() {
        return perms;
    }

    public void addPerm(String perm, boolean save) {
        perms.setPermission(perm, true);
        if (save) {
            DataStorage.get().savePerms(this);
        }
    }

    public static void removePlayer(String id) {
        PlayerStat ps = getPlayerStats(id);
        if (ps != null) {
            players.remove(ps);
        }
    }
}
