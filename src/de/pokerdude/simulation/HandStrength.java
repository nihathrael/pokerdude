package de.pokerdude.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;
import de.pokerdude.game.Deck;
import de.pokerdude.utils.Powerrating;

public class HandStrength {
	
	static HashMap<String, Double> buffer = new HashMap<String, Double>();
	
	public static double calcHandstrength(ArrayList<Card> playerCards, ArrayList<Card> commonCards, int numberOfActivePlayers) {
		
		ArrayList<Card> knownCards = new ArrayList<Card>(playerCards);
		knownCards.addAll(commonCards);
		
		TreeSet<Card> cards = new TreeSet<Card>(knownCards);
		StringBuffer ident = new StringBuffer();
		for(Card card: cards) {
			ident.append(card.getSuite());
			ident.append(card.getValue());
		}
		String identString = ident.toString();
		
		if(buffer.containsKey(identString)) {
			return Math.pow(buffer.get(identString),(double)numberOfActivePlayers);
		}
		Deck currentDeck = new Deck(knownCards);
		Deck deckToCompare = null;

		double wins = 0;
		double ties = 0;
		double losses = 0;

		Card card1, card2;

		CardSet setPlayer = new CardSet(knownCards);
		Powerrating playerResult = setPlayer.evaluate();
		ArrayList<Card> opponentCards = new ArrayList<Card>(10);

		while (currentDeck.size() > 0) {
			card1 = currentDeck.getCard();                  // First take a card
			deckToCompare = new Deck(currentDeck, false);   // Make a copy for
															// comparison work

			while (deckToCompare.size() > 0) {
				card2 = deckToCompare.getCard();

				opponentCards.clear();
				opponentCards.add(card1);
				opponentCards.add(card2);
				opponentCards.addAll(commonCards);

				CardSet setOpponent = new CardSet(opponentCards);

				int result = playerResult.compareTo(setOpponent.evaluate());

				if (result > 0)
					wins++;
				else if (result == 0)
					ties++;
				else if (result < 0)
					losses++;

			}
		}
		
		double result = ((wins + ties / 2) / (wins + ties + losses));
		buffer.put(identString, result);

		return Math.pow(result,(double)numberOfActivePlayers);
	}
}

