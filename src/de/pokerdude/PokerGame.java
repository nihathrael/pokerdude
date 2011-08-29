package de.pokerdude;

import java.util.ArrayList;
import java.util.Collections;

public class PokerGame {
	
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private int smallblind;
	private int bigblind;
	
	public PokerGame() {
		generateNewDeck();
		for(Card card: deck) {
			System.out.println(card);
		}
		System.out.println("Game generated with " + deck.size() + " cards!");
	}
	
	public boolean addPlayer(Player player) {
		if(players.size() < 10) {
			return players.add(player);
		}
		return false;
	}
	
	public void generateNewDeck() {
		addSuiteCards(Suite.CLUBS);
		addSuiteCards(Suite.DIAMONDS);
		addSuiteCards(Suite.HEARTS);
		addSuiteCards(Suite.SPADES);
		Collections.shuffle(deck);
	}
	
	public void addSuiteCards(Suite suite) {
		for(int i=2; i<=14;i++) {
			deck.add(new Card(suite, i));
		}
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
		for(Player player: players) {
			ArrayList<Card> hand = new ArrayList<Card>();
		}
	}
}
