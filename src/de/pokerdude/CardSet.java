package de.pokerdude;

import java.util.ArrayList;
import java.util.TreeSet;

public class CardSet {

	TreeSet<Card> clubs = new TreeSet<Card>();
	TreeSet<Card> diamonds = new TreeSet<Card>();
	TreeSet<Card> hearts = new TreeSet<Card>();
	TreeSet<Card> spades = new TreeSet<Card>();
	ArrayList<Card> cards = null;

	public CardSet(ArrayList<Card> cards) {
		this.cards = cards;
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
		TreeSet<Card> res = getBiggestSet();
		Card[] cardArray = res.toArray(new Card[0]);
		Card previous = res.first();
		int row = 1;
		for (int i = 1; i < res.size(); i++) {
			if (cardArray[i].getValue() == previous.getValue() + 1) {
				row++;
			}
			previous = cardArray[i];
		}
		return (row >= 5);
	}
}
