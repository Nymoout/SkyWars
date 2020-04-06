package com.nymoout.skywars.config;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class Config {

    private boolean debug;

    private boolean bungeeMode;
    private boolean economyEnabled;
    private String bungeeLobby;
    private List<String> gameEndCommands;


    private String resourcePack;
    private boolean promptResource;

    private int kitVotePos;
    private boolean kitsEnabled;
    private int votePos;
    private boolean voteEnabled;
    private int exitPos;
    private int chestVotePos;
    private boolean chestVoteEnabled;
    private int healthVotePos;
    private boolean healthVoteEnabled;
    private int timeVotePos;
    private boolean timeVoteEnabled;
    private int weatherVotePos;
    private boolean weatherVoteEnabled;
    private int modifierVotePos;
    private boolean modifierVoteEnabled;
    private int particleSelectSlot;
    private int projectileSelectSlot;
    private int killSoundSelectSlot;
    private int winSoundSelectSlot;
    private int glassSelectSlot;
    private int tauntSelectSlot;

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
    private boolean protectLobby;
    private boolean displayPlayerExperience;

    private boolean borderEnabled;
    private int borderSize;
    private boolean showHealth;
    private int winnerEco;
    private int killerEco;
    private int snowballDamage;
    private int eggDamage;
    private int winnerXP;
    private List<String> winCommands;
    private int killerXP;
    private List<String> killCommands;
    private int vip1;
    private int vip2;
    private int vip3;
    private int vip4;
    private int vip5;
    private boolean tauntsEnabled;
    private boolean titlesEnabled;
    private boolean allowFallDamage;
    private boolean kitVotingEnabled;
    private int waitTimer;
    private int strength;
    private int speed;
    private int jump;

    private boolean usePlayerNames;
    private boolean usePlayerGlassColors;
    private String teamMaterial;

    private int timeAfterMatch;
    private boolean fireworksEnabled;
    private int fireworksPer5Tick;
    private int maxMapSize;
    private Location spawn;

    private boolean pressurePlate;
    private boolean teleportOnJoin;
    private boolean teleportOnWorldEnter;

    private int maxPartySize;
    private boolean partyEnabled;
    private List<String> lobbyWorlds;

    private int maxChest;
    private int maxDoubleChest;

    private boolean useHolograms;

    private int cooldown;

    private int kitMenuSize;
    private int randPos;
    private int noKitPos;
    private String randMat;
    private String noKitMat;

    private boolean particlesEnabled;
    private int ticksPerUpdate;

    private boolean joinEnabled;
    private int joinSlot;
    private int singleSlot;
    private int teamSlot;
    private boolean spectateMenuEnabled;
    private int spectateSlot;
    private boolean optionsEnabled;
    private int optionsSlot;
    private boolean glassEnabled;
    private boolean particleEnabled;
    private boolean projectEnabled;
    private boolean killSoundEnabled;
    private boolean winSoundEnabled;
    private boolean tauntSMenuEnabled;
    private boolean playSounds;
    private int autojoinSlot;
    private boolean autojoinEnable;

    private String countdown;
    private String joinSound;
    private String leaveSound;
    private String openJoinMenu;
    private String openSpectateMenu;
    private String openOptionsMenu;
    private String openGlassMenu;
    private String openWinSoundMenu;
    private String openKillSoundMenu;
    private String openParticleMenu;
    private String openProjectileMenu;
    private String openTauntMenu;
    private String openKitMenu;
    private String openChestMenu;
    private String openTimeMenu;
    private String openWeatherMenu;
    private String openHealthMenu;
    private String openModifierMenu;
    private String confirmSelection;
    private String errorSound;

    private boolean spectateEnabled;

    private boolean disableCommands;
    private List<String> enabledCommands;

    private boolean disableCommandsSpectate;
    private List<String> enabledCommandsSpectate;

    private boolean useExternalChat;
    private boolean addPrefix;
    private boolean enableFormatter;
    private boolean limitGameChat;
    private boolean limitSpecChat;
    private boolean limitLobbyChat;


    private Map<String, String> materials = new HashMap<>();
    private final List<String> itemNames = Arrays.asList("kitvote", "votingItem",
            "exitMenuItem", "nextPageItem", "prevPageItem",
            "exitGameItem",
            "chestvote", "chestrandom", "chestbasic", "chestnormal", "chestop", "chestscavenger",
            "healthvote", "healthrandom", "healthfive", "healthten", "healthfifteen", "healthtwenty",
            "nopermission",
            "timevote", "timerandom", "timedawn", "timenoon", "timedusk", "timemidnight",
            "weathervote", "weatherrandom", "weathersunny", "weatherrain", "weatherstorm", "weathersnow",
            "modifiervote", "modifierrandom", "modifierspeed", "modifierjump", "modifierstrength", "modifiernone",
            "joinselect",
            "singlemenu",
            "teammenu",
            "spectateselect",
            "optionselect",
            "particleselect",
            "projectileselect",
            "killsoundselect", "killsounditem",
            "winsoundselect",
            "glassselect", "tauntselect",
            "autojoinselect");
    private final List<String> defItems13 = Arrays.asList("ENDER_EYE", "COMPASS",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "SHIELD", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOODEN_HOE",
            "EXPERIENCE_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "CLOCK", "NETHER_STAR", "CLOCK", "CLOCK", "CLOCK", "CLOCK",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGON_BREATH", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH",
            "COMPARATOR",
            "LEATHER_HELMET",
            "ENDER_EYE",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "GLASS", "SHIELD");
    private final List<String> defItems12 = Arrays.asList("EYE_OF_ENDER", "COMPASS",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "SHIELD", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOOD_HOE",
            "EXP_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "WATCH", "NETHER_STAR", "WATCH", "WATCH", "WATCH", "WATCH",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGONS_BREATH", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH_OFF",
            "REDSTONE_COMPARATOR",
            "LEATHER_HELMET",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "SHIELD",
            "BOW");
    private final List<String> defItems8 = Arrays.asList("EYE_OF_ENDER", "COMPASS",
            "BARRIER", "FEATHER", "FEATHER",
            "IRON_DOOR",
            "DIAMOND", "NETHER_STAR", "STONE_SWORD", "IRON_SWORD", "DIAMOND_SWORD", "WOOD_HOE",
            "EXP_BOTTLE", "NETHER_STAR", "REDSTONE", "REDSTONE", "REDSTONE", "REDSTONE",
            "BARRIER",
            "WATCH", "NETHER_STAR", "WATCH", "WATCH", "WATCH", "WATCH",
            "BLAZE_POWDER", "NETHER_STAR", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD", "PRISMARINE_SHARD",
            "DRAGON_EGG", "NETHER_STAR", "BOOK", "BOOK", "BOOK", "BOOK",
            "DIAMOND_HELMET",
            "REDSTONE_TORCH_OFF",
            "REDSTONE_COMPARATOR",
            "LEATHER_HELMET",
            "EYE_OF_ENDER",
            "BLAZE_POWDER",
            "ARROW",
            "DIAMOND_SWORD", "NOTE_BLOCK",
            "DRAGON_EGG",
            "STAINED_GLASS", "DRAGON_EGG",
            "BOW");

    private final List<String> signItems = Arrays.asList("blockoffline", "blockwaiting", "blockplaying", "blockending", "almostfull", "threefull", "halffull", "almostempty");
    private final List<String> signDef8 = Arrays.asList("COAL_BLOCK", "EMERALD_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "DIAMOND_HELMET", "GOLD_HELMET", "IRON_HELMET", "LEATHER_HELMET");
    private final List<String> signDef13 = Arrays.asList("COAL_BLOCK", "EMERALD_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK", "DIAMOND_HELMET", "GOLDEN_HELMET", "IRON_HELMET", "LEATHER_HELMET");

    private boolean loading = false;

    public Config() {
        load();
    }

    public void load() {
        if (!loading) {
            loading = true;
            debug = SkyWars.get().getConfig().getBoolean("debugMode");

            bungeeMode = SkyWars.get().getConfig().getBoolean("bungeeMode");
            economyEnabled = SkyWars.get().getConfig().getBoolean("economyEnabled");
            bungeeLobby = SkyWars.get().getConfig().getString("bungeeLobby");
            gameEndCommands = SkyWars.get().getConfig().getStringList("gameEndCommands");
            resourcePack = SkyWars.get().getConfig().getString("resourcepack");
            promptResource = SkyWars.get().getConfig().getBoolean("promptForResourcePackOnJoin");

            lobbyBoardEnabled = SkyWars.get().getConfig().getBoolean("lobbyBoardEnabled");
            protectLobby = SkyWars.get().getConfig().getBoolean("enabledLobbyGuard");
            displayPlayerExperience = SkyWars.get().getConfig().getBoolean("displayPlayerLevelOnXpBar");
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

            borderEnabled = SkyWars.get().getConfig().getBoolean("game.worldBorder.enabled");
            borderSize = SkyWars.get().getConfig().getInt("game.worldBorder.borderSize");
            showHealth = SkyWars.get().getConfig().getBoolean("game.showHealth");
            winnerEco = SkyWars.get().getConfig().getInt("game.ecoForWin");
            killerEco = SkyWars.get().getConfig().getInt("game.ecoForKill");
            winnerXP = SkyWars.get().getConfig().getInt("game.xpForWin");
            snowballDamage = SkyWars.get().getConfig().getInt("game.snowballDamage");
            eggDamage = SkyWars.get().getConfig().getInt("game.eggDamage");
            winCommands = SkyWars.get().getConfig().getStringList("game.winCommands");
            killerXP = SkyWars.get().getConfig().getInt("game.xpForKill");
            killCommands = SkyWars.get().getConfig().getStringList("game.killCommands");
            vip1 = SkyWars.get().getConfig().getInt("game.vip1Multiplier");
            vip2 = SkyWars.get().getConfig().getInt("game.vip2Multiplier");
            vip3 = SkyWars.get().getConfig().getInt("game.vip3Multiplier");
            vip4 = SkyWars.get().getConfig().getInt("game.vip4Multiplier");
            vip5 = SkyWars.get().getConfig().getInt("game.vip5Multiplier");
            spawn = Util.get().stringToLocation(SkyWars.get().getConfig().getString("spawn"));
            timeAfterMatch = SkyWars.get().getConfig().getInt("game.timeAfterMatch");
            fireworksPer5Tick = SkyWars.get().getConfig().getInt("fireworks.per5Ticks");
            fireworksEnabled = SkyWars.get().getConfig().getBoolean("fireworks.enabled");
            waitTimer = SkyWars.get().getConfig().getInt("game.waitTimer");
            tauntsEnabled = SkyWars.get().getConfig().getBoolean("game.tauntsEnabled");
            titlesEnabled = SkyWars.get().getConfig().getBoolean("titles.enabled");
            allowFallDamage = SkyWars.get().getConfig().getBoolean("game.allowFallDamage");
            kitVotingEnabled = SkyWars.get().getConfig().getBoolean("game.kitVotingEnabled");
            spectateEnabled = SkyWars.get().getConfig().getBoolean("game.spectateEnabled");
            maxMapSize = SkyWars.get().getConfig().getInt("game.maxMapSize");
            pressurePlate = SkyWars.get().getConfig().getBoolean("enablePressurePlateJoin");
            teleportOnJoin = SkyWars.get().getConfig().getBoolean("teleportToSpawnOnJoin");
            teleportOnWorldEnter = SkyWars.get().getConfig().getBoolean("teleportToSpawnOnWorldEnter");
            strength = SkyWars.get().getConfig().getInt("game.modifierLevel.strength");
            speed = SkyWars.get().getConfig().getInt("game.modifierLevel.speed");
            jump = SkyWars.get().getConfig().getInt("game.modifierLevel.jump");

            usePlayerNames = SkyWars.get().getConfig().getBoolean("teams.usePlayerNames");
            usePlayerGlassColors = SkyWars.get().getConfig().getBoolean("teams.usePlayerGlassColors");
            teamMaterial = SkyWars.get().getConfig().getString("teams.teamCageMaterial");
            if (teamMaterial == null || (!teamMaterial.equalsIgnoreCase("wool") && !teamMaterial.equalsIgnoreCase("stained_glass") && !teamMaterial.equalsIgnoreCase("banner"))) {
                teamMaterial = "STAINED_GLASS";
            }

            maxPartySize = SkyWars.get().getConfig().getInt("parties.maxPartySize");
            partyEnabled = SkyWars.get().getConfig().getBoolean("parties.enabled");
            lobbyWorlds = SkyWars.get().getConfig().getStringList("parties.lobbyWorlds");

            maxChest = SkyWars.get().getConfig().getInt("chests.maxItemsChest");
            maxDoubleChest = SkyWars.get().getConfig().getInt("chests.maxItemsDoubleChest");

            useHolograms = SkyWars.get().getConfig().getBoolean("holograms.enabled");

            boolean requireSave = false;

            if (spawn != null) {
                if (lobbyWorlds == null) {
                    lobbyWorlds = new ArrayList<>();
                }
                if (!lobbyWorlds.contains(spawn.getWorld().getName())) {
                    lobbyWorlds.add(spawn.getWorld().getName());
                    requireSave = true;
                }
            }

            kitVotePos = SkyWars.get().getConfig().getInt("items.kitVotePosition");
            kitsEnabled = SkyWars.get().getConfig().getBoolean("items.kitsEnabled");
            votePos = SkyWars.get().getConfig().getInt("items.votingPosition");
            voteEnabled = SkyWars.get().getConfig().getBoolean("items.voteEnabled");
            exitPos = SkyWars.get().getConfig().getInt("items.exitPosition");
            chestVotePos = SkyWars.get().getConfig().getInt("items.chestVotePosition");
            chestVoteEnabled = SkyWars.get().getConfig().getBoolean("items.chestVoteEnabled");
            healthVotePos = SkyWars.get().getConfig().getInt("items.healthVotePosition");
            healthVoteEnabled = SkyWars.get().getConfig().getBoolean("items.healthVoteEnabled");
            timeVotePos = SkyWars.get().getConfig().getInt("items.timeVotePosition");
            timeVoteEnabled = SkyWars.get().getConfig().getBoolean("items.timeVoteEnabled");
            weatherVotePos = SkyWars.get().getConfig().getInt("items.weatherVotePosition");
            weatherVoteEnabled = SkyWars.get().getConfig().getBoolean("items.weatherVoteEnabled");
            modifierVotePos = SkyWars.get().getConfig().getInt("items.modifierVotePosition");
            modifierVoteEnabled = SkyWars.get().getConfig().getBoolean("items.modifierVoteEnabled");
            particleSelectSlot = SkyWars.get().getConfig().getInt("items.particleselectslot");
            projectileSelectSlot = SkyWars.get().getConfig().getInt("items.projectileselectslot");
            killSoundSelectSlot = SkyWars.get().getConfig().getInt("items.killsoundselectslot");
            winSoundSelectSlot = SkyWars.get().getConfig().getInt("items.winsoundselectslot");
            glassSelectSlot = SkyWars.get().getConfig().getInt("items.glassselectslot");
            tauntSelectSlot = SkyWars.get().getConfig().getInt("items.tauntselectslot");

            cooldown = SkyWars.get().getConfig().getInt("tauntCooldown");

            randPos = SkyWars.get().getConfig().getInt("kit.randPos");
            kitMenuSize = SkyWars.get().getConfig().getInt("kit.menuSize");
            noKitPos = SkyWars.get().getConfig().getInt("kit.noKitPos");
            randMat = SkyWars.get().getConfig().getString("kit.randItem");
            noKitMat = SkyWars.get().getConfig().getString("kit.noKitItem");

            particlesEnabled = SkyWars.get().getConfig().getBoolean("particles.enabled");
            ticksPerUpdate = SkyWars.get().getConfig().getInt("particles.ticksperupdate");

            spectateMenuEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.spectate");
            spectateSlot = SkyWars.get().getConfig().getInt("enabledMenus.spectateSlot");
            joinEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.join");
            joinSlot = SkyWars.get().getConfig().getInt("enabledMenus.joinSlot");
            singleSlot = SkyWars.get().getConfig().getInt("items.singleSlot");
            teamSlot = SkyWars.get().getConfig().getInt("items.teamSlot");
            optionsEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.options");
            optionsSlot = SkyWars.get().getConfig().getInt("enabledMenus.optionsSlot");
            glassEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.glass");
            particleEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.particle");
            projectEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.projectile");
            killSoundEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.killsound");
            winSoundEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.winsound");
            tauntSMenuEnabled = SkyWars.get().getConfig().getBoolean("enabledMenus.taunts");
            autojoinEnable = SkyWars.get().getConfig().getBoolean("enabledMenus.autojoin");
            autojoinSlot = SkyWars.get().getConfig().getInt("enabledMenus.autojoinSlot");

            playSounds = SkyWars.get().getConfig().getBoolean("sounds.enabled");
            countdown = SkyWars.get().getConfig().getString("sounds.countdown");
            joinSound = SkyWars.get().getConfig().getString("sounds.join");
            leaveSound = SkyWars.get().getConfig().getString("sounds.leave");
            openJoinMenu = SkyWars.get().getConfig().getString("sounds.openJoinMenu");
            openSpectateMenu = SkyWars.get().getConfig().getString("sounds.openSpectateMenu");
            openOptionsMenu = SkyWars.get().getConfig().getString("sounds.openOptionsMenu");
            openGlassMenu = SkyWars.get().getConfig().getString("sounds.openGlassMenu");
            openWinSoundMenu = SkyWars.get().getConfig().getString("sounds.openWinSoundMenu");
            openKillSoundMenu = SkyWars.get().getConfig().getString("sounds.openKillSoundMenu");
            openParticleMenu = SkyWars.get().getConfig().getString("sounds.openParticleMenu");
            openProjectileMenu = SkyWars.get().getConfig().getString("sounds.openProjectileMenu");
            openTauntMenu = SkyWars.get().getConfig().getString("sounds.openTauntMenu");
            openKitMenu = SkyWars.get().getConfig().getString("sounds.openKitMenu");
            openChestMenu = SkyWars.get().getConfig().getString("sounds.openChestMenu");
            openHealthMenu = SkyWars.get().getConfig().getString("sounds.openHealthMenu");
            openTimeMenu = SkyWars.get().getConfig().getString("sounds.openTimeMenu");
            openWeatherMenu = SkyWars.get().getConfig().getString("sounds.openWeatherMenu");
            openModifierMenu = SkyWars.get().getConfig().getString("sounds.openModifierMenu");
            confirmSelection = SkyWars.get().getConfig().getString("sounds.confirmSelectionSound");
            errorSound = SkyWars.get().getConfig().getString("sounds.errorSound");

            enabledCommands = SkyWars.get().getConfig().getStringList("disable-commands.exceptions");
            disableCommands = SkyWars.get().getConfig().getBoolean("disable-commands.enabled");

            disableCommandsSpectate = SkyWars.get().getConfig().getBoolean("disable-commands-spectate.enabled");
            enabledCommandsSpectate = SkyWars.get().getConfig().getStringList("disable-commands-spectate.exceptions");

            useExternalChat = SkyWars.get().getConfig().getBoolean("chat.externalChat.useExternalChat");
            addPrefix = SkyWars.get().getConfig().getBoolean("chat.externalChat.addPrefix");
            enableFormatter = SkyWars.get().getConfig().getBoolean("chat.enableFormatter");
            limitGameChat = SkyWars.get().getConfig().getBoolean("chat.limitGameChatToGame");
            limitSpecChat = SkyWars.get().getConfig().getBoolean("chat.limitSpecChatToSpec");
            limitLobbyChat = SkyWars.get().getConfig().getBoolean("chat.limitLobbyChatToLobby");

            for (int i = 0; i < itemNames.size(); i++) {
                String name = itemNames.get(i);
                String def;
                if (SkyWars.getNMS().getVersion() < 9) {
                    def = defItems8.get(i);
                } else if (SkyWars.getNMS().getVersion() > 8 && SkyWars.getNMS().getVersion() < 13) {
                    def = defItems12.get(i);
                } else {
                    def = defItems13.get(i);
                }
                addMaterial(name, SkyWars.get().getConfig().getString("items." + name), def);
            }

            for (int i = 0; i < signItems.size(); i++) {
                String name = signItems.get(i);
                String def;
                if (SkyWars.getNMS().getVersion() < 13) {
                    def = signDef8.get(i);
                } else {
                    def = signDef13.get(i);
                }

                addMaterial(name, SkyWars.get().getConfig().getString("signs." + name), def);
            }

            if (requireSave) {
                save();
            }
        }
        loading = false;
    }

    private void addMaterial(String key, String mat, String def) {
        int data = -1;
        String matWithData = "";
        String[] matParts = mat.split(":");
        if (matParts.length == 2) {
            matWithData = matParts[0];
            data = Integer.valueOf(matParts[1]);
        }
        Material material;
        if (data != -1) {
            material = Material.matchMaterial(matWithData);
        } else {
            material = Material.matchMaterial(mat);
        }
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
        SkyWars.get().getConfig().set("bungeeMode", bungeeMode);
        SkyWars.get().getConfig().set("bungeeLobby", bungeeLobby);
        SkyWars.get().getConfig().set("gameEndCommands", gameEndCommands);

        SkyWars.get().getConfig().set("resourcepack", resourcePack);
        SkyWars.get().getConfig().set("promptForResourcePackOnJoin", promptResource);

        SkyWars.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWars.get().getConfig().set("lobbyBoardEnabled", lobbyBoardEnabled);
        SkyWars.get().getConfig().set("enabledLobbyGuard", protectLobby);
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

        SkyWars.get().getConfig().set("game.worldBorder.enabled", borderEnabled);
        SkyWars.get().getConfig().set("game.worldBorder.borderSize", borderSize);
        SkyWars.get().getConfig().set("game.showHealth", showHealth);
        SkyWars.get().getConfig().set("game.ecoForWin", winnerEco);
        SkyWars.get().getConfig().set("game.ecoForKill", killerEco);
        SkyWars.get().getConfig().set("game.snowballDamage", snowballDamage);
        SkyWars.get().getConfig().set("game.eggDamage", eggDamage);
        SkyWars.get().getConfig().set("game.xpForWin", winnerXP);
        SkyWars.get().getConfig().set("game.winCommands", winCommands);
        SkyWars.get().getConfig().set("game.xpForKill", killerXP);
        SkyWars.get().getConfig().set("game.killCommands", killCommands);
        SkyWars.get().getConfig().set("game.vip1Multiplier", vip1);
        SkyWars.get().getConfig().set("game.vip2Multiplier", vip2);
        SkyWars.get().getConfig().set("game.vip3Multiplier", vip3);
        SkyWars.get().getConfig().set("game.vip4Multiplier", vip4);
        SkyWars.get().getConfig().set("game.vip5Multiplier", vip5);
        SkyWars.get().getConfig().set("titles.enabled", titlesEnabled);
        SkyWars.get().getConfig().set("game.waitTimer", waitTimer);
        SkyWars.get().getConfig().set("game.timeAfterMatch", timeAfterMatch);
        SkyWars.get().getConfig().set("fireworks.per5Ticks", fireworksPer5Tick);
        SkyWars.get().getConfig().set("fireworks.enabled", fireworksEnabled);
        SkyWars.get().getConfig().set("game.spectateEnabled", spectateEnabled);
        SkyWars.get().getConfig().set("game.maxMapSize", maxMapSize);
        SkyWars.get().getConfig().set("game.tauntsEnabled", tauntsEnabled);
        SkyWars.get().getConfig().set("enablePressurePlateJoin", pressurePlate);
        SkyWars.get().getConfig().set("teleportToSpawnOnJoin", teleportOnJoin);
        SkyWars.get().getConfig().set("teleportToSpawnOnWorldEnter", teleportOnWorldEnter);
        SkyWars.get().getConfig().set("game.allowFallDamage", allowFallDamage);
        SkyWars.get().getConfig().set("game.kitVotingEnabled", kitVotingEnabled);
        SkyWars.get().getConfig().set("game.modifierLevel.strength", strength);
        SkyWars.get().getConfig().set("game.modifierLevel.speed", speed);
        SkyWars.get().getConfig().set("game.modifierLevel.jump", jump);

        SkyWars.get().getConfig().set("teams.usePlayerNames", usePlayerNames);
        SkyWars.get().getConfig().set("teams.usePlayerGlassColors", usePlayerGlassColors);
        SkyWars.get().getConfig().set("teams.teamCageMaterial", teamMaterial.toUpperCase());

        SkyWars.get().getConfig().set("parties.maxPartySize", maxPartySize);
        SkyWars.get().getConfig().set("parties.enabled", partyEnabled);
        SkyWars.get().getConfig().set("parties.lobbyWorlds", lobbyWorlds);

        SkyWars.get().getConfig().set("chests.maxItemsChest", maxChest);
        SkyWars.get().getConfig().set("chests.maxItemsDoubleChest", maxDoubleChest);


        SkyWars.get().getConfig().set("holograms.enabled", useHolograms);

        SkyWars.get().getConfig().set("items.kitVotePosition", kitVotePos);
        SkyWars.get().getConfig().set("items.kitsEnabled", kitsEnabled);
        SkyWars.get().getConfig().set("items.votingPosition", votePos);
        SkyWars.get().getConfig().set("items.voteEnabled", voteEnabled);
        SkyWars.get().getConfig().set("items.exitPosition", exitPos);
        SkyWars.get().getConfig().set("items.chestVotePosition", chestVotePos);
        SkyWars.get().getConfig().set("items.chestVoteEnabled", chestVoteEnabled);
        SkyWars.get().getConfig().set("items.healthVotePosition", healthVotePos);
        SkyWars.get().getConfig().set("items.healthVoteEnabled", healthVoteEnabled);
        SkyWars.get().getConfig().set("items.timeVotePosition", timeVotePos);
        SkyWars.get().getConfig().set("items.timeVoteEnabled", timeVoteEnabled);
        SkyWars.get().getConfig().set("items.weatherVotePosition", weatherVotePos);
        SkyWars.get().getConfig().set("items.weatherVoteEnabled", weatherVoteEnabled);
        SkyWars.get().getConfig().set("items.modifierVotePosition", modifierVotePos);
        SkyWars.get().getConfig().set("items.modifierVoteEnabled", modifierVoteEnabled);
        SkyWars.get().getConfig().set("items.particleselectslot", particleSelectSlot);
        SkyWars.get().getConfig().set("items.projectileselectslot", projectileSelectSlot);
        SkyWars.get().getConfig().set("items.killsoundselectslot", killSoundSelectSlot);
        SkyWars.get().getConfig().set("items.winsoundselectslot", winSoundSelectSlot);
        SkyWars.get().getConfig().set("items.glassselectslot", glassSelectSlot);
        SkyWars.get().getConfig().set("items.tauntselectslot", tauntSelectSlot);

        SkyWars.get().getConfig().set("tauntCooldown", cooldown);

        SkyWars.get().getConfig().set("kit.randPos", randPos);
        SkyWars.get().getConfig().set("kit.menuSize", kitMenuSize);
        SkyWars.get().getConfig().set("kit.noKitPos", noKitPos);
        SkyWars.get().getConfig().set("kit.randItem", randMat);
        SkyWars.get().getConfig().set("kit.noKitItem", noKitMat);

        SkyWars.get().getConfig().set("particles.enabled", particlesEnabled);
        SkyWars.get().getConfig().set("particles.ticksperupdate", ticksPerUpdate);

        SkyWars.get().getConfig().set("enabledMenus.spectate", spectateMenuEnabled);
        SkyWars.get().getConfig().set("enabledMenus.spectateSlot", spectateSlot);
        SkyWars.get().getConfig().set("enabledMenus.join", joinEnabled);
        SkyWars.get().getConfig().set("enabledMenus.joinSlot", joinSlot);
        SkyWars.get().getConfig().set("items.singleSlot", singleSlot);
        SkyWars.get().getConfig().set("items.teamSlot", teamSlot);
        SkyWars.get().getConfig().set("enabledMenus.options", optionsEnabled);
        SkyWars.get().getConfig().set("enabledMenus.optionsSlot", optionsSlot);
        SkyWars.get().getConfig().set("enabledMenus.glass", glassEnabled);
        SkyWars.get().getConfig().set("enabledMenus.particle", particlesEnabled);
        SkyWars.get().getConfig().set("enabledMenus.projectile", projectEnabled);
        SkyWars.get().getConfig().set("enabledMenus.killsound", killSoundEnabled);
        SkyWars.get().getConfig().set("enabledMenus.winsound", winSoundEnabled);
        SkyWars.get().getConfig().set("enabledMenus.taunts", tauntSMenuEnabled);
        SkyWars.get().getConfig().set("enabledMenus.autojoin", autojoinEnable);
        SkyWars.get().getConfig().set("enabledMenus.autojoinSlot", autojoinSlot);

        SkyWars.get().getConfig().set("sounds.enabled", playSounds);
        SkyWars.get().getConfig().set("sounds.countdown", countdown);
        SkyWars.get().getConfig().set("sounds.join", joinSound);
        SkyWars.get().getConfig().set("sounds.leave", leaveSound);
        SkyWars.get().getConfig().set("sounds.openJoinMenu", openJoinMenu);
        SkyWars.get().getConfig().set("sounds.openSpectateMenu", openSpectateMenu);
        SkyWars.get().getConfig().set("sounds.openOptionsMenu", openOptionsMenu);
        SkyWars.get().getConfig().set("sounds.openGlassMenu", openGlassMenu);
        SkyWars.get().getConfig().set("sounds.openWinSoundMenu", openWinSoundMenu);
        SkyWars.get().getConfig().set("sounds.openKillSoundMenu", openKillSoundMenu);
        SkyWars.get().getConfig().set("sounds.openParticleMenu", openParticleMenu);
        SkyWars.get().getConfig().set("sounds.openProjectileMenu", openProjectileMenu);
        SkyWars.get().getConfig().set("sounds.openTauntMenu", openTauntMenu);
        SkyWars.get().getConfig().set("sounds.openKitMenu", openKitMenu);
        SkyWars.get().getConfig().set("sounds.openChestMenu", openChestMenu);
        SkyWars.get().getConfig().set("sounds.openHealthMenu", openHealthMenu);
        SkyWars.get().getConfig().set("sounds.openTimeMenu", openTimeMenu);
        SkyWars.get().getConfig().set("sounds.openWeatherMenu", openWeatherMenu);
        SkyWars.get().getConfig().set("sounds.openModifierMenu", openModifierMenu);
        SkyWars.get().getConfig().set("sounds.confirmSelectionSound", confirmSelection);
        SkyWars.get().getConfig().set("sounds.errorSound", errorSound);

        SkyWars.get().getConfig().set("disable-commands.exceptions", enabledCommands);
        SkyWars.get().getConfig().set("disable-commands.enabled", disableCommands);

        SkyWars.get().getConfig().set("disable-commands-spectate.enabled", disableCommandsSpectate);
        SkyWars.get().getConfig().set("disable-commands-spectate.exceptions", enabledCommandsSpectate);

        SkyWars.get().getConfig().getBoolean("chat.externalChat.useExternalChat", useExternalChat);
        SkyWars.get().getConfig().getBoolean("chat.externalChat.addPrefix", addPrefix);
        SkyWars.get().getConfig().getBoolean("chat.enableFormatter", enableFormatter);
        SkyWars.get().getConfig().getBoolean("chat.limitGameChatToGame", limitGameChat);
        SkyWars.get().getConfig().getBoolean("chat.limitSpecChatToSpec", limitSpecChat);
        SkyWars.get().getConfig().getBoolean("chat.limitLobbyChatToLobby", limitLobbyChat);

        for (String name : itemNames) {
            SkyWars.get().getConfig().set("items." + name, materials.get(name));
        }

        SkyWars.get().saveConfig();
    }

    public List<String> getEnabledCommands() {
        return enabledCommands;
    }

    public List<String> getEnabledCommandsSpectate() {
        return enabledCommandsSpectate;
    }

    public int getUpdateTime() {
        return leaderboardUpdateInterval;
    }

    public boolean disableCommands() {
        return disableCommands;
    }

    public boolean disableCommandsSpectate() {
        return disableCommandsSpectate;
    }

    public int getWaitTimer() {
        return waitTimer;
    }

    public int getTimeAfterMatch() {
        return timeAfterMatch;
    }

    public int getFireWorksPer5Tick() {
        return fireworksPer5Tick;
    }

    public boolean fireworksEnabled() {
        return fireworksEnabled;
    }

    public boolean titlesEnabled() {
        return titlesEnabled;
    }

    public boolean particlesEnabled() {
        return particlesEnabled;
    }

    public boolean soundsEnabled() {
        return playSounds;
    }

    public boolean spectateEnable() {
        return spectateEnabled;
    }

    public boolean debugEnabled() {
        return debug;
    }

    public int getMaxMapSize() {
        return maxMapSize;
    }

    public void setSpawn(Location location) {
        this.spawn = location;
        if (!lobbyWorlds.contains(spawn.getWorld().getName())) {
            lobbyWorlds.add(spawn.getWorld().getName());
            save();
        }
    }

    public Location getSpawn() {
        return spawn;
    }

    public boolean bungeeMode() {
        return (bungeeMode && SkyWars.get().isEnabled());
    }

    public String getBungeeLobby() {
        return bungeeLobby;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getMaterial(String string) {
        return materials.get(string);
    }

    public boolean pressurePlateJoin() {
        return pressurePlate;
    }

    public String getCountdownSound() {
        return countdown;
    }

    public int getWinnerXP() {
        return winnerXP;
    }

    public int getKillerXP() {
        return killerXP;
    }

    public int getVip1() {
        return vip1;
    }

    public int getVip2() {
        return vip2;
    }

    public int getVip3() {
        return vip3;
    }

    public int getVip4() {
        return vip4;
    }

    public int getVip5() {
        return vip5;
    }

    public List<String> getWinCommands() {
        return winCommands;
    }

    public List<String> getKillCommands() {
        return killCommands;
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
        return killSoundEnabled;
    }

    public boolean winsoundMenuEnabled() {
        return winSoundEnabled;
    }

    public boolean tauntsMenuEnabled() {
        return tauntSMenuEnabled;
    }

    public boolean optionsMenuEnabled() {
        return optionsEnabled;
    }

    public int getOptionsSlot() {
        return optionsSlot;
    }

    public boolean joinMenuEnabled() {
        return joinEnabled;
    }

    public int getJoinSlot() {
        return joinSlot;
    }

    public boolean spectateMenuEnabled() {
        return spectateMenuEnabled;
    }

    public int getSpectateSlot() {
        return spectateSlot;
    }

    public boolean teleportOnJoin() {
        return teleportOnJoin;
    }

    public int getRandPos() {
        return randPos;
    }

    public Material getRandMat() {
        return Material.valueOf(randMat);
    }

    public int getNoKitPos() {
        return noKitPos;
    }

    public Material getNoKitMat() {
        return Material.valueOf(noKitMat);
    }

    public boolean promptForResource() {
        return promptResource;
    }

    public String getResourceLink() {
        return resourcePack;
    }

    public int getLeaderSize() {
        return leaderSize;
    }

    public boolean leaderSignsEnabled() {
        return leaderSignsEnabled;
    }

    public boolean leaderHeadsEnabled() {
        return leaderHeadsEnabled;
    }

    public long getTicksPerUpdate() {
        return ticksPerUpdate;
    }

    public boolean allowFallDamage() {
        return allowFallDamage;
    }

    public boolean kitVotingEnabled() {
        return kitVotingEnabled;
    }

    public int getKitVotePos() {
        return kitVotePos;
    }

    public boolean areKitsEnabled() {
        return kitsEnabled;
    }

    public int getChestVotePos() {
        return chestVotePos;
    }

    public boolean isChestVoteEnabled() {
        return chestVoteEnabled;
    }

    public int getHealthVotePos() {
        return healthVotePos;
    }

    public boolean isHealthVoteEnabled() {
        return healthVoteEnabled;
    }

    public int getTimeVotePos() {
        return timeVotePos;
    }

    public boolean isTimeVoteEnabled() {
        return timeVoteEnabled;
    }

    public int getWeatherVotePos() {
        return weatherVotePos;
    }

    public boolean isWeatherVoteEnabled() {
        return weatherVoteEnabled;
    }

    public int getModifierVotePos() {
        return modifierVotePos;
    }

    public boolean isModifierVoteEnabled() {
        return modifierVoteEnabled;
    }

    public boolean lobbyBoardEnabled() {
        return lobbyBoardEnabled;
    }

    public String getJoinSound() {
        return joinSound;
    }

    public String getLeaveSound() {
        return leaveSound;
    }

    public String getOpenOptionsMenuSound() {
        return openOptionsMenu;
    }

    public String getOpenJoinMenuSound() {
        return openJoinMenu;
    }

    public String getOpenSpectateMenuSound() {
        return openSpectateMenu;
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

    public String getOpenGlassMenuSound() {
        return openGlassMenu;
    }

    public String getOpenTauntMenuSound() {
        return openTauntMenu;
    }

    public String getOpenKitMenuSound() {
        return openKitMenu;
    }

    public String getOpenChestMenuSound() {
        return openChestMenu;
    }

    public String getOpenTimeMenuSound() {
        return openTimeMenu;
    }

    public String getOpenWeatherMenuSound() {
        return openWeatherMenu;
    }

    public String getOpenModifierMenuSound() {
        return openModifierMenu;
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
        return protectLobby;
    }

    public boolean displayPlayerExeperience() {
        return displayPlayerExperience;
    }

    public int maxPartySize() {
        return maxPartySize;
    }

    public boolean partyEnabled() {
        return partyEnabled;
    }

    public List<String> getLobbyWorlds() {
        return lobbyWorlds;
    }

    public boolean votingEnabled() {
        return voteEnabled;
    }

    public int getVotingPos() {
        return votePos;
    }

    public int getExitPos() {
        return exitPos;
    }

    public int getWinnerEco() {
        return winnerEco;
    }

    public int getKillerEco() {
        return killerEco;
    }

    public void setEconomyEnabled(boolean b) {
        economyEnabled = b;
    }

    public boolean hologramsEnabled() {
        return useHolograms;
    }

    public void setHologramsEnabled(boolean b) {
        useHolograms = b;
    }

    public boolean isTypeEnabled(LeaderType type) {
        switch (type) {
            case ELO:
                return eloEnabled;
            case WINS:
                return winsEnabled;
            case LOSSES:
                return lossesEnabled;
            case KILLS:
                return killsEnabled;
            case DEATHS:
                return deathsEnabled;
            case XP:
                return xpEnabled;
            default:
                return false;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public int getStrength() {
        return strength;
    }

    public boolean tauntsEnabled() {
        return tauntsEnabled;
    }

    public String getOpenHealthMenuSound() {
        return openHealthMenu;
    }

    public int getKitMenuSize() {
        return kitMenuSize;
    }

    public int getGlassSlot() {
        return glassSelectSlot;
    }

    public int getParticleSlot() {
        return particleSelectSlot;
    }

    public int getProjectileSlot() {
        return projectileSelectSlot;
    }

    public int getKillSoundSlot() {
        return killSoundSelectSlot;
    }

    public int getWinSoundSlot() {
        return winSoundSelectSlot;
    }

    public int getTauntSlot() {
        return tauntSelectSlot;
    }

    public double getSnowDamage() {
        return snowballDamage;
    }

    public double getEggDamage() {
        return eggDamage;
    }

    public int getMaxDoubleChest() {
        return maxDoubleChest;
    }

    public int getMaxChest() {
        return maxChest;
    }

    public boolean useExternalChat() {
        return useExternalChat;
    }

    public boolean addPrefix() {
        return addPrefix;
    }

    public boolean formatChat() {
        return enableFormatter;
    }

    public boolean limitGameChat() {
        return limitGameChat;
    }

    public boolean limitSpecChat() {
        return limitSpecChat;
    }

    public boolean limitLobbyChat() {
        return limitLobbyChat;
    }

    public int getSingleSlot() {
        return singleSlot;
    }

    public int getTeamSlot() {
        return teamSlot;
    }

    public boolean usePlayerNames() {
        return usePlayerNames;
    }

    public String getTeamMaterial() {
        return teamMaterial;
    }

    public boolean usePlayerGlassColors() {
        return usePlayerGlassColors;
    }

    public boolean showHealth() {
        return showHealth;
    }

    public double getBorderSize() {
        return borderSize;
    }

    public boolean borderEnabled() {
        return borderEnabled;
    }

    public List<String> getGameEndCommands() {
        return gameEndCommands;
    }

    public int getAutojoinSlot() {
        return autojoinSlot;
    }

    public boolean autoJoinEnable() {
        return autojoinEnable;
    }
}