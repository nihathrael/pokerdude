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
	
	public Deck(Deck copy) {
		this.deck = (ArrayList<Card>) copy.deck.clone();
		Collections.shuffle(deck);
		
		
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
	
	public ArrayList<Card> getCards(int count) {
		ArrayList<Card> result = (ArrayList<Card>) deck.subList(deck.size()-count, deck.size());
		deck = (ArrayList<Card>) deck.subList(0, deck.size()-count);
		return result;
		
	}
	
	public int size() {
		return deck.size();
	}

}
