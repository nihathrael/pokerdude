package de.pokerdude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

public class CardSet {

	TreeSet<Card> clubs = new TreeSet<Card>();
	TreeSet<Card> diamonds = new TreeSet<Card>();
	TreeSet<Card> hearts = new TreeSet<Card>();
	TreeSet<Card> spades = new TreeSet<Card>();
	TreeSet<Card> cards = null;
	HashMap<Integer, Integer> mapping = new HashMap<Integer, Integer>();

	public CardSet(ArrayList<Card> cards) {
		this.cards = new TreeSet<Card>(cards);
		for (Card card : cards) {
			Suite suite = card.getSuite();
			if (suite.equals(Suite.CLUBS)) {
				clubs.add(card);
			} else if (suite.equals(Suite.DIAMONDS)) {
				diamonds.add(card);
			} else if (suite.equals(Suite.HEARTS)) {
				hearts.add(card);
			} else if (suite.equals(Suite.SPADES)) {
				spades.add(card);
			}
			int value = card.getValue();
			if(mapping.containsKey(value)) {
				mapping.put(value, mapping.get(value)+1);
			} else {
				mapping.put(value, 1);
			}
		}
	}

	private TreeSet<Card> getBiggestSet() {
		TreeSet<Card> res = clubs;
		if (diamonds.size() > res.size()) {
			res = diamonds;
		}
		if (hearts.size() > res.size()) {
			res = hearts;
		}
		if (spades.size() > res.size()) {
			res = spades;
		}
		return res;
	}

	public boolean containsFlush() {
		return getBiggestSet().size() > 5;
	}

	public boolean containsStraightFlush() {
		if (!containsFlush())
			return false;
		return findRow(getBiggestSet());
	}
	
	public boolean containsOnePair() {
		return containsOfAKind(2);
	}
	
	public boolean containsTwoPairs() {
		int pairs = 0;
		for(Entry<Integer, Integer> entry: mapping.entrySet()) {
			if(entry.getValue() >= 2) {
				++pairs;
			}
		}
		return pairs >= 2;
	}
	
	public boolean containsFullHouse() {
		boolean pair = false;
		boolean triple = false;
		for(Entry<Integer, Integer> entry: mapping.entrySet()) {
			if(entry.getValue() == 2) {
				pair = true;
			}
			if(entry.getValue() >= 3) {
				triple = true;
			}
		}
		return pair && triple;
	}
	
	public boolean containsFourOfAKind() {
		return containsOfAKind(4);
	}
	
	public boolean containsThreeOfAKind() {
		return containsOfAKind(4);
	}
	
	public boolean containsOfAKind(int number) {
		for(Entry<Integer, Integer> entry: mapping.entrySet()) {
			if(entry.getValue() >= number) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsStraight() {
		return findRow(cards);
	}

	/**
	 * Check if a straight row of cards for a given card set exists
	 */
	private boolean findRow(TreeSet<Card> cardSet) {
		Card[] cardArray = cardSet.toArray(new Card[0]);
		Card previous = cardSet.first();
		int row = 1;
		for (int i = 1; i < cardSet.size(); i++) {
			if (cardArray[i].getValue() == previous.getValue() + 1) {
				row++;
			} else {
				row = 1;
			}
			previous = cardArray[i];
		}
		return (row >= 5);
	}
	
	
	
}