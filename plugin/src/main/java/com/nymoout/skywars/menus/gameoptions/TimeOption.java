package com.nymoout.skywars.menus.gameoptions;

import java.util.ArrayList;
import java.util.Arrays;

import com.nymoout.skywars.menus.gameoptions.GameOption;
import com.nymoout.skywars.enums.ScoreVar;
import org.bukkit.entity.Player;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.enums.Vote;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Util;

public class TimeOption extends GameOption {

	public TimeOption(GameMap gameMap, String key) {
		itemList = new ArrayList<>(Arrays.asList("timerandom", "timedawn", "timenoon", "timedusk", "timemidnight"));
		voteList = new ArrayList<>(Arrays.asList(Vote.TIMERANDOM, Vote.TIMEDAWN, Vote.TIMENOON, Vote.TIMEDUSK, Vote.TIMEMIDNIGHT));
		createMenu(key, new Messaging.MessageFormatter().format("menu.time-voting-menu"));
		this.gameMap = gameMap;
	}
	
	@Override
	protected void doSlotNine(Player player) {
		Vote cVote = Vote.TIMERANDOM;
		String type = new Messaging.MessageFormatter().format("items.time-random");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotEleven(Player player) {
		Vote cVote = Vote.TIMEDAWN;
		String type = new Messaging.MessageFormatter().format("items.time-dawn");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotThriteen(Player player) {
		Vote cVote = Vote.TIMENOON;
		String type = new Messaging.MessageFormatter().format("items.time-noon");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotFifteen(Player player) {
		Vote cVote = Vote.TIMEDUSK;
		String type = new Messaging.MessageFormatter().format("items.time-dusk");
		finishEvent(player, cVote, type);
	}

	@Override
	protected void doSlotSeventeen(Player player) {
		Vote cVote = Vote.TIMEMIDNIGHT;
		String type = new Messaging.MessageFormatter().format("items.time-midnight");
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
					.setVariable("time", type).format("game.votetime"));
		}
	}

	@Override
	public void setCard(PlayerCard pCard, Vote vote) {
		pCard.setGameTime(vote);
	}

	@Override
	public Vote getVote(PlayerCard pCard) {
		return pCard.getVote("time");
	}

	@Override
	public Vote getRandomVote() {
		return Vote.getRandom("time");
	}
	
	@Override
	protected void updateScoreboard() {
		gameMap.setCurrentTime(getVoteString(getVoted()));
		gameMap.getGameBoard().updateScoreboardVar(ScoreVar.TIMEVOTE);
	}

	@Override
	protected Vote getDefault() {
		return Vote.TIMENOON;	
	}

	@Override
	public void completeOption() {
		Vote time = gameMap.getTimeOption().getVoted();
		int t = 0;
		if (time == Vote.TIMENOON) {
			t = 6000;
		} else if (time == Vote.TIMEDUSK) {
			t = 12000;
		} else if (time == Vote.TIMEMIDNIGHT) {
			t = 18000;
		}
		gameMap.getCurrentWorld().setTime(t);
	}

}
