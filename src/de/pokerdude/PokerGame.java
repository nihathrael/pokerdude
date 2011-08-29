package de.pokerdude;

import java.util.ArrayList;

public class PokerGame {
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private Deck deck = new Deck();
	
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
	
	
	public int getSmallBlind() {
		return smallblind;
	}
	
	public int getBigBlind() {
		return bigblind;
	}
	
	public void startGame() {
		giveCards();
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
}
