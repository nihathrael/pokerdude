package de.pokerdude.game;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.pokerdude.PokerDude;
import de.pokerdude.players.Player;
import de.pokerdude.utils.PokerUtils;
import de.pokerdude.utils.Powerrating;

public class PokerGame {
	
	static final Logger logger = Logger.getLogger(PokerGame.class);
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> playersInRound = new ArrayList<Player>();
	
	int currentPlayer = 0;
	
	private Deck deck = new Deck();
	
	private ArrayList<Card> flop = new ArrayList<Card>();
	private Card turn = null;
	private Card river = null;
	
	
	private int pot;

	private int currentBet;
	
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
	
	public int getNumberOfPlayers() {
		return this.players.size();
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
		this.turn = null;
		this.players.add(this.players.remove(0));
		this.playersInRound.clear();
		this.playersInRound.addAll(this.players);
	}
	
	public void playRound() {
		resetGame();
		giveCards();
		showTable();
		int i =0;
		for(Player player: playersInRound.toArray(new Player[0])) {
			if(i<2) {
				player.getBetPreFlop(currentBet, true).execute();
				logger.debug("Blind "+i+" forced on:" + player.name);
				i++;
			} else {
				player.getBetPreFlop(currentBet, false).execute();
			}
		}
		fillFlop();
		showTable();
		for(Player player: playersInRound.toArray(new Player[0])) {
			player.getBetPreTurn(currentBet).execute();
		}
		fillTurn();
		showTable();
		for(Player player: playersInRound.toArray(new Player[0])) {
			player.getBetPreRiver(currentBet).execute();
		}
		fillRiver();
		showTable();
		logger.debug("Pot at: " + pot);
		showResults();
		//showCredits();
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

	public void foldPlayer(Player player) {
		playersInRound.remove(player);
	}
	
	public void call(Player player) {
		bet(player, currentBet);
	}
	
	public void bet(Player player, int amount) {
		player.credits -= amount;
		pot += amount;
		currentBet = amount;
	}
}
