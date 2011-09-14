package de.pokerdude;

import java.util.ArrayList;

public class PokerGame {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private Deck deck = new Deck();
	
	private ArrayList<Card> flop = new ArrayList<Card>();
	private Card turn = null;
	private Card river = null;
	
	
	private int pot;
	private int smallblind;
	private int bigblind;
	
	public PokerGame() {
		System.out.println("Game generated with " + deck.size() + " cards!");
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
	
	
	public int getSmallBlind() {
		return smallblind;
	}
	
	public int getBigBlind() {
		return bigblind;
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
	
	public void startGame() {
		giveCards();
		showTable();
		for(Player player: players) {
			pot += player.getBetPreFlop();
		}
		fillFlop();
		showTable();
		for(Player player: players) {
			pot += player.getBetPreTurn();
		}
		fillTurn();
		showTable();
		for(Player player: players) {
			pot += player.getBetPreRiver();
		}
		fillRiver();
		showTable();
		System.out.println("Pot at: " + pot);
		showResults();
	}
	
	private void showResults() {
		ArrayList<Card> allCards = new ArrayList<Card>(this.flop);
		allCards.add(this.river);
		allCards.add(this.turn);
		Player winner = null;
		Powerrating winningResult = null;
		
		for(Player player: players) {
			System.out.println("Player:" + player.name);
			System.out.print("Cards:");
			System.out.println(player.getCards());
			Powerrating result = PokerUtils.evaluateCards(player.getCards(), flop, turn, river);
			if(winningResult == null || result.compareTo(winningResult) == 1) {
				winningResult = result;
				winner = player;
			}
			System.out.print("Results:" );
			System.out.println(result);
		}
		System.out.println("=======================");
		System.out.println("Winner: " + winner.name);
		System.out.println("He wins with: " + winningResult);
		System.out.println("=======================");
	}

	private void giveCards() {
		System.out.println("Giving cards...");
		for(Player player: players) {
			ArrayList<Card> hand = new ArrayList<Card>();
			hand.add(deck.getCard());
			hand.add(deck.getCard());
			player.setCards(hand);
		}
		System.out.println("Done!");
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
		System.out.println("Current table:");
		System.out.print("Flop: ");
		for(Card card: flop) {
			System.out.print(card);
		}
		System.out.println();
		System.out.println("Turn:  " + turn);
		System.out.println("River: " + river);
	}
}
