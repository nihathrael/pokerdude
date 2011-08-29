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
	
	public void startGame() {
		giveCards();
		for(Player player: players) {
			pot += player.getBetPreFlop();
		}
		fillFlop();
		for(Player player: players) {
			pot += player.getBetPreTurn();
		}
		fillTurn();
		for(Player player: players) {
			pot += player.getBetPreRiver();
		}
		fillRiver();
		System.out.println("Pot at: " + pot);
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
		System.out.println("Filled flop:");
		for(Card card: flop) {
			System.out.println(card);
		}
	}
	
	private void fillRiver() {
		river = deck.getCard();
		System.out.println("River set: " + river);
	}
	
	private void fillTurn() {
		turn = deck.getCard();
		System.out.println("Turn set: " + turn);
	}
	
	public void showTable() {
		System.out.println("Current table:");
		
	}
}
