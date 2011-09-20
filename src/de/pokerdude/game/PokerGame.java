package de.pokerdude.game;

import java.util.ArrayList;


import org.apache.log4j.Logger;

import de.pokerdude.PokerDude;
import de.pokerdude.opponentmodeling.OpponentModelTable;
import de.pokerdude.players.Player;
import de.pokerdude.utils.PokerUtils;
import de.pokerdude.utils.Powerrating;

public class PokerGame {
	
	static final Logger logger = Logger.getLogger(PokerGame.class);
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> playersInRound = new ArrayList<Player>();
	
	private OpponentModelTable opponentModelTable = new OpponentModelTable();
	
	int currentPlayer = 0;
	
	private Deck deck = new Deck();
	
	private ArrayList<Card> flop = new ArrayList<Card>();
	private Card turn = null;
	private Card river = null;
	
	
	private int pot;

	private int currentBet;
	
	private int numRaises=0;
	int noChanges = 0;
	int resetNumber = 0;
	int bigblind = 10;
	
	public PokerGame() {
		logger.setLevel(PokerDude.DEBUGLEVEL);
		logger.info("Game generated with " + deck.size() + " cards!");
	}
	
	public boolean addPlayer(Player player) {
		if(players.size() < 10) {
			return players.add(player);
		}
		return false;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Card getTurn() {
		return turn;
	}
	
	public Card getRiver() {
		return river;
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public ArrayList<Card> getFlop() {
		return flop;
	}
	
	public int getPot() {
		return pot;
	}
	
	public int getNumberOfPlayers() {
		return this.players.size();
	}
	
	public int getNumberOfRaises() {
		return numRaises;
	}
	
	public ArrayList<Player> getPlayersInRound() {
		return this.playersInRound;
	}
	
	public OpponentModelTable getOpponenModelTable() {
		return opponentModelTable;
	}
	
	public ArrayList<Card> getCommonCards() {
		ArrayList<Card> commonCards = new ArrayList<Card>();
		if(this.getRiver() != null) commonCards.add(this.getRiver());
		if(this.getTurn() != null) commonCards.add(this.getTurn());
		commonCards.addAll(this.getFlop());
		return commonCards;
	}
	
	public void resetGame() {
		deck.generateNewDeck();
		
		this.flop.clear();
		this.river = null;
		this.pot = 0;
		this.currentBet = 10; // Minimum first blind
		this.numRaises = 0;
		this.turn = null;
		this.players.add(this.players.remove(0));
		this.playersInRound.clear();
		this.playersInRound.addAll(this.players);
	}
	
	
	
	public void playRound() {
		resetGame();
		giveCards();
		showTable();
		bettingRound(GameState.PREFLOP, true);
		fillFlop();
		showTable();
		bettingRound(GameState.PRETURN, false);
		fillTurn();
		showTable();
		bettingRound(GameState.PRERIVER, false);
		fillRiver();
		showTable();
		bettingRound(GameState.POSTRIVER, false);
		logger.debug("Pot at: " + pot);
		showResults();
		//showCredits();
	}
	
	private void bettingRound(GameState round, boolean blinds) {
		logger.debug("Betting round:" + round);
		int i =0;
		noChanges = 0;
		currentBet = 0;
		resetNumber = playersInRound.size();
		while(noChanges < resetNumber) {
			Player player = this.playersInRound.remove(0);
			this.playersInRound.add(player);
			if(blinds && i<2) {
				logger.debug("Blind "+i+" forced on:" + player);
				int blind = bigblind/(2-i);
				if(currentBet < blind) {
					currentBet = blind;
				}
				player.getBet(round, currentBet, true).execute();
				i++;
			} else {
				player.getBet(round, currentBet, false).execute();
			}
		}
		for(Player player: playersInRound) {
			this.pot += player.lastBet;
			player.lastBet = 0;
		}
	}
	
	private void showResults() {
		ArrayList<Card> allCards = new ArrayList<Card>(this.flop);
		allCards.add(this.river);
		allCards.add(this.turn);
		Player winner = null;
		Powerrating winningResult = null;
		
		for(Player player: playersInRound) {
			logger.debug("Player:" + player.name);
			logger.debug("Cards:");
			logger.debug(player.getCards());
			Powerrating result = PokerUtils.evaluateCards(player.getCards(), flop, turn, river);
			if(winningResult == null || result.compareTo(winningResult) == 1) {
				winningResult = result;
				winner = player;
			}
			logger.debug("Results:" );
			logger.debug(result);
		}
		logger.debug("=======================");
		logger.debug("Winner: " + winner.name);
		logger.debug("He wins with: " + winningResult);
		logger.debug("He wins the pot: " + pot);
		logger.debug("=======================");
		winner.credits += pot;
	}
	
	public void showCredits() {
		logger.info("=======================");
		logger.info("Player credits:");
		for(Player player: players) {
			logger.info(player.name + ":" + player.credits + " NOK");
		}
		logger.info("=======================");
	
	
	}

	private void giveCards() {
		for(Player player: playersInRound) {
			ArrayList<Card> hand = new ArrayList<Card>();
			hand.add(deck.getCard());
			hand.add(deck.getCard());
			player.setCards(hand);
		}
	}
	
	private void fillFlop() {
		flop.add(deck.getCard());
		flop.add(deck.getCard());
		flop.add(deck.getCard());
	}
	
	private void fillRiver() {
		river = deck.getCard();
	}
	
	private void fillTurn() {
		turn = deck.getCard();
	}
	
	public void showTable() {
		logger.debug("Current table:");
		logger.debug("Flop: ");
		for(Card card: flop) {
			logger.debug(card);
		}
		logger.debug("Turn:  " + turn);
		logger.debug("River: " + river);
	}

	public GameState getState() {
		if(flop.size() == 0) return GameState.PREFLOP;
		if(turn == null) return GameState.PRETURN;
		if(river == null) return GameState.PRERIVER;
		else return GameState.POSTRIVER;
	}
	
	public void foldPlayer(Player player) {
		noChanges++;
		playersInRound.remove(player);
		this.pot += player.lastBet;
		player.lastBet = 0;
		logger.debug(player + " folding");
	}
	
	public void call(Player player) {
		int amount = currentBet-player.lastBet;
		player.credits -= amount;
		player.lastBet = currentBet;
		this.noChanges++;
		logger.debug(player + " calling:" + currentBet);
	}
	
	
	public void raise(Player player, int amount) {
		if(amount > currentBet) numRaises++;
		
		int difference = amount-player.lastBet;
		player.credits -= difference;
		player.lastBet = amount;
		currentBet = amount;
		this.noChanges = 1;
		this.resetNumber = playersInRound.size();
		logger.debug(player + " raising to:" + currentBet);
	}
	
	public int getCurrentPot() {
		int totalpot = this.pot;
		for(Player player: players) {
			totalpot += player.lastBet;
		}
		return totalpot;
	}
}
