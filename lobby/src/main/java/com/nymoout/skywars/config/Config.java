package com.nymoout.skywars.config;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private boolean debug;
    private boolean economyEnabled;

    private int leaderSize;
    private boolean leaderSignsEnabled;
    private boolean leaderHeadsEnabled;
    private int leaderboardUpdateInterval;
    private boolean eloEnabled;
    private boolean winsEnabled;
    private boolean lossesEnabled;
    private boolean killsEnabled;
    private boolean deathsEnabled;
    private boolean xpEnabled;
    private boolean lobbyBoardEnabled;
    private boolean protectlobby;
    private boolean displayPlayerExeperience;

    private Location spawn;

    private boolean joinEnabled;
    private int joinSlot;
    private boolean optionsEnabled;
    private int optionsSlot;
    private boolean glassEnabled;
    private boolean particleEnabled;
    private boolean projectEnabled;
    private boolean killsoundEnabled;
    private boolean winsoundEnabled;
    private boolean tauntsEnabled;
    private boolean playSounds;
    private int autoJoinSlot;
    private boolean autoJoinEnable;

    private String openJoinMenu;
    private String openOptionsMenu;
    private String openGlassMenu;
    private String openWinSoundMenu;
    private String openKillSoundMenu;
    private String openParticleMenu;
    private String openProjectileMenu;
    private String openTauntMenu;
    private String confirmSelection;
    private String errorSound;

    private List<String> gameServers;

    private Map<String, String> materials = new HashMap<String, String>();
    private final List<String> itemNames = Arrays.asList("joinselect",
            "nopermission",
            "optionselect",
            "particleselect",
            "projectileselect",
            "killsoundselect", "killsounditem",
            "winsoundselect",
            "glassselect", "tauntselect",
            "autojoinselect");
    private final List<String> defItems = Arrays.asList("DIAMOND_HELMET",
            "BARRIER",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "SHIELD",
            "BOW");
    private final List<String> defItems18 = Arrays.asList("DIAMOND_HELMET",
            "BARRIER",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "DRAGON_EGG",
            "BOW");

    private final List<String> signItems = Arrays.asList("blockoffline", "blockwaiting", "blockplaying", "blockending", "almostfull", "threefull", "halffull", "almostempty");
    private final List<String> signDef = Arrays.asList("COAL_BLOCK", "EMERALD_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "DIAMOND_HELMET", "GOLD_HELMET", "IRON_HELMET", "LEATHER_HELMET");

    public Config() {
        load();
    }

    private void load() {
        debug = SkyWars.get().getConfig().getBoolean("debugMode");
        economyEnabled = SkyWars.get().getConfig().getBoolean("economyEnabled");

        lobbyBoardEnabled = SkyWars.get().getConfig().getBoolean("lobbyBoardEnabled");
        protectlobby = SkyWars.get().getConfig().getBoolean("enabledLobbyGuard");
        displayPlayerExeperience = SkyWars.get().getConfig().getBoolean("displayPlayerLevelOnXpBar");
        leaderSize = SkyWars.get().getConfig().getInt("leaderboards.length");
        leaderSignsEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.signsEnabled");
        leaderHeadsEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.headsEnabled");
        eloEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.eloLeaderboardEnabled");
        winsEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.winsLeaderboardEnabled");
        lossesEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.lossesLeaderboardEnabled");
        killsEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.killsLeaderboardEnabled");
        deathsEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.deathsLeaderboardEnabled");
        xpEnabled = SkyWars.get().getConfig().getBoolean("leaderboards.xpLeaderboardEnabled");
        leaderboardUpdateInterval = SkyWars.get().getConfig().getInt("leaderboards.leaderboardUpdateInterval");

        spawn = Util.get().stringToLocation(SkyWars.get().getConfig().getString("spawn"));

        joinEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.join");
        joinSlot = SkyWars.get().getConfig().getInt("enabledMenus.joinSlot");
        optionsEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.options");
        optionsSlot = SkyWars.get().getConfig().getInt("enabledMenus.optionsSlot");
        glassEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.glass");
        particleEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.particle");
        projectEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.projectile");
        killsoundEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.killsound");
        winsoundEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.winsound");
        tauntsEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.taunts");
        autoJoinEnable = SkyWars.get().getConfig().getBoolean("enabledMenus.autojoin");
        autoJoinSlot = SkyWars.get().getConfig().getInt("enabledMenus.autojoinSlot");

        playSounds = SkyWars.get().getConfig().getBoolean("sounds.enabled");
        openJoinMenu = SkyWars.get().getConfig().getString("sounds.openJoinMenu");
        openOptionsMenu = SkyWars.get().getConfig().getString("sounds.openOptionsMenu");
        openGlassMenu = SkyWars.get().getConfig().getString("sounds.openGlassMenu");
        openWinSoundMenu = SkyWars.get().getConfig().getString("sounds.openWinSoundMenu");
        openKillSoundMenu = SkyWars.get().getConfig().getString("sounds.openKillSoundMenu");
        openParticleMenu = SkyWars.get().getConfig().getString("sounds.openParticleMenu");
        openProjectileMenu = SkyWars.get().getConfig().getString("sounds.openProjectileMenu");
        openTauntMenu = SkyWars.get().getConfig().getString("sounds.openTauntMenu");
        confirmSelection = SkyWars.get().getConfig().getString("sounds.confirmSelectionSound");
        errorSound = SkyWars.get().getConfig().getString("sounds.errorSound");
        gameServers = SkyWars.get().getConfig().getStringList("gameServers");

        for (int i = 0; i < itemNames.size(); i++) {
            String name = itemNames.get(i);
            String def;
            if (SkyWars.getNMS().getVersion() < 9) {
                def = defItems18.get(i);
            } else {
                def = defItems.get(i);
            }
            addMaterial(name, SkyWars.get().getConfig().getString("items." + name), def);
        }

        for (int i = 0; i < signItems.size(); i++) {
            String name = signItems.get(i);
            String def = signDef.get(i);
            addMaterial(name, SkyWars.get().getConfig().getString("signs." + name), def);
        }
    }

    private void addMaterial(String key, String mat, String def) {
        Material material = Material.matchMaterial(mat);
        if (material == null) {
            materials.put(key, def);
        } else {
            materials.put(key, mat);
        }
    }

    public void save() {
        SkyWars.get().getConfig().set("debugMode", debug);

        SkyWars.get().getConfig().set("spawn", Util.get().locationToString(spawn));

        SkyWars.get().getConfig().set("economyEnabled", economyEnabled);

        SkyWars.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWars.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWars.get().getConfig().set("enabledLobbyGuard", protectlobby);
        SkyWars.get().getConfig().set("leaderboards.length", leaderSize);
        SkyWars.get().getConfig().set("leaderboards.signsEnabled", leaderSignsEnabled);
        SkyWars.get().getConfig().set("leaderboards.headsEnabled", leaderHeadsEnabled);
        SkyWars.get().getConfig().set("leaderboards.eloLeaderboardEnabled", eloEnabled);
        SkyWars.get().getConfig().set("leaderboards.winsLeaderboardEnabled", winsEnabled);
        SkyWars.get().getConfig().set("leaderboards.lossesLeaderboardEnabled", lossesEnabled);
        SkyWars.get().getConfig().set("leaderboards.killsLeaderboardEnabled", killsEnabled);
        SkyWars.get().getConfig().set("leaderboards.deathsLeaderboardEnabled", deathsEnabled);
        SkyWars.get().getConfig().set("leaderboards.xpLeaderboardEnabled", xpEnabled);
        SkyWars.get().getConfig().set("leaderboards.leaderboardUpdateInterval", leaderboardUpdateInterval);

        SkyWars.get().getConfig().set("enabledMenus.join", joinEnabled);
        SkyWars.get().getConfig().set("enabledMenus.joinSlot", joinSlot);
        SkyWars.get().getConfig().set("enabledMenus.options", optionsEnabled);
        SkyWars.get().getConfig().set("enabledMenus.optionsSlot", optionsSlot);
        SkyWars.get().getConfig().set("enabledMenus.particle", particleEnabled);
        SkyWars.get().getConfig().set("enabledMenus.glass", glassEnabled);
        SkyWars.get().getConfig().set("enabledMenus.projectile", projectEnabled);
        SkyWars.get().getConfig().set("enabledMenus.killsound", killsoundEnabled);
        SkyWars.get().getConfig().set("enabledMenus.winsound", winsoundEnabled);
        SkyWars.get().getConfig().set("enabledMenus.taunts", tauntsEnabled);
        SkyWars.get().getConfig().set("enabledMenus.autojoin", autoJoinEnable);
        SkyWars.get().getConfig().set("enabledMenus.autojoinSlot", autoJoinSlot);

        SkyWars.get().getConfig().set("sounds.enabled", playSounds);
        SkyWars.get().getConfig().set("sounds.openJoinMenu", openJoinMenu);
        SkyWars.get().getConfig().set("sounds.openOptionsMenu", openOptionsMenu);
        SkyWars.get().getConfig().set("sounds.openGlassMenu", openGlassMenu);
        SkyWars.get().getConfig().set("sounds.openWinSoundMenu", openWinSoundMenu);
        SkyWars.get().getConfig().set("sounds.openKillSoundMenu", openKillSoundMenu);
        SkyWars.get().getConfig().set("sounds.openParticleMenu", openParticleMenu);
        SkyWars.get().getConfig().set("sounds.openProjectileMenu", openProjectileMenu);
        SkyWars.get().getConfig().set("sounds.openTauntMenu", openTauntMenu);
        SkyWars.get().getConfig().set("sounds.confirmSelectionSound", confirmSelection);
        SkyWars.get().getConfig().set("sounds.errorSound", errorSound);

        SkyWars.get().getConfig().set("gameServers", gameServers);

        for (int i = 0; i < itemNames.size(); i++) {
            String name = itemNames.get(i);
            SkyWars.get().getConfig().set("items." + name, materials.get(name));
        }

        SkyWars.get().saveConfig();
    }

    public int getUpdateTime() {
        return leaderboardUpdateInterval;
    }

    public boolean soundsEnabled() {
        return playSounds;
    }

    public boolean debugEnabled() {
        return debug;
    }

    public void setSpawn(Location location) {
        this.spawn = location;
    }

    public Location getSpawn() {
        return spawn;
    }

    public String getMaterial(String string) {
        return materials.get(string);
    }

    public boolean glassMenuEnabled() {
        return glassEnabled;
    }

    public boolean particleMenuEnabled() {
        return particleEnabled;
    }

    public boolean projectileMenuEnabled() {
        return projectEnabled;
    }

    public boolean killsoundMenuEnabled() {
        return killsoundEnabled;
    }

    public boolean winsoundMenuEnabled() {
        return winsoundEnabled;
    }

    public boolean tauntsMenuEnabled() {
        return tauntsEnabled;
    }

    public boolean optionsMenuEnabled() {
        return optionsEnabled;
    }

    public int getOptionsSlot() {
        return optionsSlot;
    }

    public int getLeaderSize() {
        return leaderSize;
    }

    public boolean eloEnabled() {
        return eloEnabled;
    }

    public boolean winsEnabled() {
        return winsEnabled;
    }

    public boolean lossesEnabled() {
        return lossesEnabled;
    }

    public boolean killsEnabled() {
        return killsEnabled;
    }

    public boolean deathsEnabled() {
        return deathsEnabled;
    }

    public boolean xpEnabled() {
        return xpEnabled;
    }

    public boolean leaderSignsEnabled() {
        return leaderSignsEnabled;
    }

    public boolean leaderHeadsEnabled() {
        return leaderHeadsEnabled;
    }

    public boolean lobbyBoardEnabled() {
        return lobbyBoardEnabled;
    }

    public String getOpenOptionsMenuSound() {
        return openOptionsMenu;
    }

    public String getOpenParticleMenuSound() {
        return openParticleMenu;
    }

    public String getOpenProjectileMenuSound() {
        return openProjectileMenu;
    }

    public String getOpenKillSoundMenuSound() {
        return openKillSoundMenu;
    }

    public String getOpenWinSoundMenuSound() {
        return openWinSoundMenu;
    }

    public String getOpenTauntMenuSound() {
        return openTauntMenu;
    }

    public String getOpenGlassMenuSound() {
        return openGlassMenu;
    }

    public String getConfirmeSelctionSound() {
        return confirmSelection;
    }

    public String getErrorSound() {
        return errorSound;
    }

    public boolean economyEnabled() {
        return economyEnabled;
    }

    public boolean protectLobby() {
        return protectlobby;
    }

    public boolean displayPlayerExeperience() {
        return displayPlayerExeperience;
    }

    public List<String> getGameServers() {
        return gameServers;
    }

    public boolean joinMenuEnabled() {
        return joinEnabled;
    }

    public int getJoinSlot() {
        return joinSlot;
    }

    public String getOpenJoinMenuSound() {
        return openJoinMenu;
    }

    public int getAutojoinSlot() {
        return autoJoinSlot;
    }

    public boolean autoJoinEnable() {
        return autoJoinEnable;
    }
}


