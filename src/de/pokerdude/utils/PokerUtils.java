package de.pokerdude.utils;

import java.util.ArrayList;

import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;

public class PokerUtils {
	
	public static Powerrating evaluateCards(ArrayList<Card> hand, ArrayList<Card> flop, Card turn, Card river) {
		ArrayList<Card> allCards = new ArrayList<Card>(7);
		allCards.addAll(hand);
		allCards.addAll(flop);
		allCards.add(turn);
		allCards.add(river);
		CardSet cardset = new CardSet(allCards);
		return cardset.evaluate();
	}

}

