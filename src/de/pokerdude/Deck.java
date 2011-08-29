package de.pokerdude;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	public Deck() {
		generateNewDeck();
		for(Card card: deck) {
			System.out.println(card);
		}
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
	
	public Card getCard() {
		return deck.remove(deck.size()-1);
	}
	
	public int size() {
		return deck.size();
	}
}
