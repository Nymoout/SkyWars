package com.nymoout.skywars.menus.gameoptions;

import java.util.ArrayList;
import java.util.Arrays;
import com.nymoout.skywars.enums.ScoreVar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.enums.Vote;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.menus.gameoptions.objects.CoordLoc;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class ChestOption extends com.nymoout.skywars.menus.gameoptions.GameOption {
	
	public ChestOption(GameMap gameMap, String key) {
		this.gameMap = gameMap;
		itemList = new ArrayList<>(Arrays.asList("chestrandom", "chestbasic", "chestnormal", "chestop", "chestscavenger"));
		voteList = new ArrayList<>(Arrays.asList(Vote.CHESTRANDOM, Vote.CHESTBASIC, Vote.CHESTNORMAL, Vote.CHESTOP, Vote.CHESTSCAVENGER));
		createMenu(key, new Messaging.MessageFormatter().format("menu.chest-voting-menu"));
	}
	
	@Override
	protected void doSlotNine(Player player) {
		Vote cVote = Vote.CHESTRANDOM;
		String type = new Messaging.MessageFormatter().format("items.chest-random");
		finishEvent(gameMap, player, cVote, type);
	}

	@Override
	protected void doSlotEleven(Player player) {
		Vote cVote = Vote.CHESTBASIC;
		String type = new Messaging.MessageFormatter().format("items.chest-basic");	
		finishEvent(gameMap, player, cVote, type);
	}

	@Override
	protected void doSlotThriteen(Player player) {
		Vote cVote = Vote.CHESTNORMAL;
		String type = new Messaging.MessageFormatter().format("items.chest-normal");
		finishEvent(gameMap, player, cVote, type);
	}

	@Override
	protected void doSlotFifteen(Player player) {
		Vote cVote = Vote.CHESTOP;
		String type = new Messaging.MessageFormatter().format("items.chest-op");
		finishEvent(gameMap, player, cVote, type);
	}

	@Override
	protected void doSlotSeventeen(Player player) {
		Vote cVote = Vote.CHESTSCAVENGER;
		String type = new Messaging.MessageFormatter().format("items.chest-scavenger");
		finishEvent(gameMap, player, cVote, type);
	}
	
	private void finishEvent(GameMap gameMap, Player player, Vote vote, String type) {
		if (vote != null) {
			setVote(player, vote);
			updateVotes();
			Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getConfirmeSelctionSound(), 1, 1);
			if (gameMap.getMatchState().equals(MatchState.WAITINGSTART)) {
				new com.nymoout.skywars.menus.gameoptions.VotingMenu(player);
			}
			MatchManager.get().message(gameMap, new Messaging.MessageFormatter()
					.setVariable("player", player.getName())
					.setVariable("chest", type).format("game.votechest"));
		}
	}

	@Override
	public void setCard(PlayerCard pCard, Vote vote) {
		pCard.setChestVote(vote);
	}

	@Override
	public Vote getVote(PlayerCard pCard) {
		return pCard.getVote("chest");
	}

	@Override
	public Vote getRandomVote() {
		return Vote.getRandom("chest");
	}

	@Override
	protected void updateScoreboard() {
		gameMap.setCurrentChest(getVoteString(getVoted()));
		gameMap.getGameBoard().updateScoreboardVar(ScoreVar.CHESTVOTE);
	}

	@Override
	protected Vote getDefault() {
		return Vote.CHESTNORMAL;	
	}

	@Override
	public void completeOption() {

        Vote cVote = gameMap.getChestOption().getVoted();
        populateChests(gameMap.getChests(), cVote, false);
		populateChests(gameMap.getCenterChests(), cVote, true);
	}

	private void populateChests(ArrayList<CoordLoc> chests, Vote cVote, boolean center) {
		World mapWorld = gameMap.getCurrentWorld();
		for (CoordLoc eChest: chests) {
			Location loc;
			int x = eChest.getX();
			int y = eChest.getY();
			int z = eChest.getZ();
			loc = new Location (mapWorld, x, y, z);
			if (loc.getBlock().getState() instanceof Chest) {
				Chest chest = (Chest) loc.getBlock().getState();
				InventoryHolder ih = chest.getInventory().getHolder();
				if (ih instanceof DoubleChest) {
					DoubleChest dc = (DoubleChest) ih;
					SkyWars.getChestManager().populateChest(dc, cVote, center);
				} else {
					SkyWars.getChestManager().populateChest(chest, cVote, center);
				}
			}
		}
	}

}
