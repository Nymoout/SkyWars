package com.nymoout.skywars.menus.gameoptions;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.enums.Vote;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.menus.IconMenu;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class GameOption {

    ArrayList<String> itemList;
    ArrayList<Vote> voteList;
    private IconMenu iconMenu;
    protected GameMap gameMap;
    protected String key;

    protected abstract void doSlotNine(Player player);

    protected abstract void doSlotEleven(Player player);

    protected abstract void doSlotThriteen(Player player);

    protected abstract void doSlotFifteen(Player player);

    protected abstract void doSlotSeventeen(Player player);

    public abstract void setCard(PlayerCard pCard, Vote vote);

    public abstract Vote getVote(PlayerCard pCard);

    public abstract Vote getRandomVote();

    protected abstract void updateScoreboard();

    protected abstract Vote getDefault();

    public abstract void completeOption();

    void createMenu(String key, String name) {
        this.key = key;
        ArrayList<Inventory> invs = new ArrayList<>();
        Inventory inv = Bukkit.createInventory(null, 36, new Messaging.MessageFormatter().format(name));
        inv.clear();
        inv.setItem(9, SkyWars.getItemsManager().getItem(itemList.get(0)));
        inv.setItem(11, SkyWars.getItemsManager().getItem(itemList.get(1)));
        inv.setItem(13, SkyWars.getItemsManager().getItem(itemList.get(2)));
        inv.setItem(15, SkyWars.getItemsManager().getItem(itemList.get(3)));
        if (!(this instanceof ChestOption) || (this instanceof ChestOption && gameMap.allowScanvenger())) {
            inv.setItem(17, SkyWars.getItemsManager().getItem(itemList.get(4)));
        }
        invs.add(inv);

        SkyWars.getIconMenuController().create(key, invs, event -> {
            String itemName = event.getName();
            if (itemName.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                new com.nymoout.skywars.menus.gameoptions.VotingMenu(event.getPlayer());
                return;
            }
            final GameMap gMap = MatchManager.get().getPlayerMap(event.getPlayer());
            if (!gMap.equals(gameMap)) {
                return;
            }
            if (gameMap.getMatchState() == MatchState.WAITINGSTART) {
                int slot = event.getSlot();
                if (slot == 9) {
                    doSlotNine(event.getPlayer());
                } else if (slot == 11) {
                    doSlotEleven(event.getPlayer());
                } else if (slot == 13) {
                    doSlotThriteen(event.getPlayer());
                } else if (slot == 15) {
                    doSlotFifteen(event.getPlayer());
                } else if (slot == 17) {
                    if (!(this instanceof ChestOption) || (this instanceof ChestOption && gameMap.allowScanvenger())) {
                        doSlotSeventeen(event.getPlayer());
                    }
                }
            }
        });
        iconMenu = SkyWars.getIconMenuController().getMenu(key);
    }

    public void restore() {
        Inventory inv = iconMenu.getInventory(0);
        inv.setItem(9, SkyWars.getItemsManager().getItem(itemList.get(0)));
        inv.setItem(11, SkyWars.getItemsManager().getItem(itemList.get(1)));
        inv.setItem(13, SkyWars.getItemsManager().getItem(itemList.get(2)));
        inv.setItem(15, SkyWars.getItemsManager().getItem(itemList.get(3)));
        if (!(this instanceof ChestOption) || (this instanceof ChestOption && gameMap.allowScanvenger())) {
            inv.setItem(17, SkyWars.getItemsManager().getItem(itemList.get(4)));
        }
        if (this instanceof ChestOption && gameMap.allowScanvenger()) {

        }
        updateScoreboard();
    }

    void setVote(Player player, Vote vote) {
        for (PlayerCard pCard : gameMap.getPlayerCards()) {
            if (pCard.getUUID() != null && pCard.getUUID().equals(player.getUniqueId())) {
                setCard(pCard, vote);
            }
        }
    }

    private HashMap<Vote, Integer> getVotes(boolean getRandom) {
        HashMap<Vote, Integer> votes = new HashMap<>();
        votes.put(voteList.get(0), 0);
        votes.put(voteList.get(1), 0);
        votes.put(voteList.get(2), 0);
        votes.put(voteList.get(3), 0);
        votes.put(voteList.get(4), 0);

        for (PlayerCard pCard : gameMap.getPlayerCards()) {
            Player player = pCard.getPlayer();
            if (player != null) {
                Vote vote = getVote(pCard);
                if (vote != null) {
                    if ((vote == Vote.TIMERANDOM || vote == Vote.WEATHERRANDOM || vote == Vote.MODIFIERRANDOM || vote == Vote.CHESTRANDOM) && getRandom) {
                        vote = getRandomVote();
                    }
                    int multiplier = Util.get().getMultiplier(player);
                    votes.put(vote, votes.get(vote) + (multiplier));
                }
            }
        }
        return votes;
    }

    void updateVotes() {
        HashMap<Vote, Integer> votes = getVotes(false);

        for (Vote vote : votes.keySet()) {
            if (vote == voteList.get(0)) {
                updateSlot(votes, vote, 0, 9, itemList);
            } else if (vote == voteList.get(1)) {
                updateSlot(votes, vote, 1, 11, itemList);
            } else if (vote == voteList.get(2)) {
                updateSlot(votes, vote, 2, 13, itemList);
            } else if (vote == voteList.get(3)) {
                updateSlot(votes, vote, 3, 15, itemList);
            } else if (vote == voteList.get(4)) {
                if (!(this instanceof ChestOption) || (this instanceof ChestOption && gameMap.allowScanvenger())) {
                    updateSlot(votes, vote, 4, 17, itemList);
                }
            }
        }
        updateScoreboard();
    }

    private void updateSlot(HashMap<Vote, Integer> votes, Vote vote, int count, int slot, ArrayList<String> itemList) {
        ItemStack item = SkyWars.getItemsManager().getItem(itemList.get(count));
        item.setAmount(votes.get(vote) == 0 ? 1 : votes.get(vote));
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lores = itemMeta.getLore();
        lores.add(" ");
        lores.add(new Messaging.MessageFormatter().setVariable("number", "" + votes.get(vote)).format("game.vote-display"));
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        iconMenu.getInventory(0).setItem(slot, item);
    }

    Vote getVoted() {
        HashMap<Vote, Integer> votes = getVotes(true);
        int highest = 0;
        Vote voted = null;
        for (Vote vote : votes.keySet()) {
            if (votes.get(vote) >= highest) {
                highest = votes.get(vote);
                voted = vote;
            }
        }
        if (highest == 0) {
            voted = getDefault();
        }
        return voted;
    }

    String getVoteString(Vote vote) {
        switch (vote) {
            case CHESTRANDOM:
                return new Messaging.MessageFormatter().format("items.chest-random");
            case CHESTBASIC:
                return new Messaging.MessageFormatter().format("items.chest-basic");
            case CHESTNORMAL:
                return new Messaging.MessageFormatter().format("items.chest-normal");
            case CHESTOP:
                return new Messaging.MessageFormatter().format("items.chest-op");
            case CHESTSCAVENGER:
                return new Messaging.MessageFormatter().format("items.chest-scavenger");
            case TIMERANDOM:
                return new Messaging.MessageFormatter().format("items.time-random");
            case TIMEDAWN:
                return new Messaging.MessageFormatter().format("items.time-dawn");
            case TIMENOON:
                return new Messaging.MessageFormatter().format("items.time-noon");
            case TIMEDUSK:
                return new Messaging.MessageFormatter().format("items.time-dusk");
            case TIMEMIDNIGHT:
                return new Messaging.MessageFormatter().format("items.time-midnight");
            case WEATHERRANDOM:
                return new Messaging.MessageFormatter().format("items.weather-random");
            case WEATHERSUN:
                return new Messaging.MessageFormatter().format("items.weather-sunny");
            case WEATHERRAIN:
                return new Messaging.MessageFormatter().format("items.weather-rain");
            case WEATHERTHUNDER:
                return new Messaging.MessageFormatter().format("items.weather-storm");
            case WEATHERSNOW:
                return new Messaging.MessageFormatter().format("items.weather-snow");
            case MODIFIERRANDOM:
                return new Messaging.MessageFormatter().format("items.modifier-random");
            case MODIFIERSPEED:
                return new Messaging.MessageFormatter().format("items.modifier-speed");
            case MODIFIERJUMP:
                return new Messaging.MessageFormatter().format("items.modifier-jump");
            case MODIFIERSTRENGTH:
                return new Messaging.MessageFormatter().format("items.modifier-strength");
            case MODIFIERNONE:
                return new Messaging.MessageFormatter().format("items.modifier-none");
            case HEALTHRANDOM:
                return new Messaging.MessageFormatter().format("items.health-random");
            case HEALTHFIVE:
                return new Messaging.MessageFormatter().format("items.health-five");
            case HEALTHTEN:
                return new Messaging.MessageFormatter().format("items.health-ten");
            case HEALTHFIFTEEN:
                return new Messaging.MessageFormatter().format("items.health-fifteen");
            case HEALTHTWENTY:
                return new Messaging.MessageFormatter().format("items.health-twenty");
            default:
                return "";
        }
    }

    public String getKey() {
        return key;
    }
}


