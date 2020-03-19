package com.nymoout.skywars.menus.gameoptions;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.enums.ScoreVar;
import com.nymoout.skywars.enums.Vote;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class HealthOption extends GameOption {

	public HealthOption(GameMap gameMap, String key) {
		itemList = new ArrayList<>(Arrays.asList("healthrandom", "healthfive", "healthten", "healthfifteen", "healthtwenty"));
		voteList = new ArrayList<>(Arrays.asList(Vote.HEALTHRANDOM, Vote.HEALTHFIVE, Vote.HEALTHTEN, Vote.HEALTHFIFTEEN, Vote.HEALTHTWENTY));
		createMenu(key, new Messaging.MessageFormatter().format("menu.health-voting-menu"));
		this.gameMap = gameMap;
	}
	
	@Override
	protected void doSlotNine(Player player) {
		Vote cVote = Vote.HEALTHRANDOM;
		String type = new Messaging.MessageFormatter().format("items.health-random");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotEleven(Player player) {
		Vote cVote = Vote.HEALTHFIVE;
		String type = new Messaging.MessageFormatter().format("items.health-five");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotThriteen(Player player) {
		Vote cVote = Vote.HEALTHTEN;
		String type = new Messaging.MessageFormatter().format("items.health-ten");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotFifteen(Player player) {
		Vote cVote = Vote.HEALTHFIFTEEN;
		String type = new Messaging.MessageFormatter().format("items.health-fifteen");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotSeventeen(Player player) {
		Vote cVote = Vote.HEALTHTWENTY;
		String type = new Messaging.MessageFormatter().format("items.health-twenty");
		finishEvent(player, cVote, type);
	}
	
	private void finishEvent(Player player, Vote vote, String type) {
		if (vote != null) {
			setVote(player, vote);
			updateVotes();
			Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getConfirmeSelctionSound(), 1, 1);
			if (gameMap.getMatchState().equals(MatchState.WAITINGSTART)) {
				new com.nymoout.skywars.menus.gameoptions.VotingMenu(player);
			}
			MatchManager.get().message(gameMap, new Messaging.MessageFormatter()
					.setVariable("player", player.getName())
					.setVariable("health", type).format("game.votehealth"));
		}
	}

	@Override
	public void setCard(PlayerCard pCard, Vote vote) {
		pCard.setHealth(vote);
	}

	@Override
	public Vote getVote(PlayerCard pCard) {
		return pCard.getVote("health");
	}

	@Override
	public Vote getRandomVote() {
		return Vote.getRandom("health");
	}
	
	@Override
	protected void updateScoreboard() {
		gameMap.setCurrentHealth(getVoteString(getVoted()));
		gameMap.getGameBoard().updateScoreboardVar(ScoreVar.HEALTHVOTE);
	}

	@Override
	protected Vote getDefault() {
		return Vote.HEALTHTEN;	
	}

	@Override
	public void completeOption() {
		Vote time = gameMap.getHealthOption().getVoted();
		int t = 10;
		if (time == Vote.HEALTHFIVE) {
			t = 5;
		} else if (time == Vote.HEALTHFIFTEEN) {
			t = 15;
		} else if (time == Vote.HEALTHTWENTY) {
			t = 20;
		}
		for (Player player: gameMap.getAlivePlayers()) {
			SkyWars.getNMS().setMaxHealth(player, t * 2);
			player.setHealth(t * 2);
		}
	}

}
