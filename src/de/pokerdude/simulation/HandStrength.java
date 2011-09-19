package de.pokerdude.simulation;

import java.util.ArrayList;

import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;
import de.pokerdude.game.Deck;
import de.pokerdude.utils.Powerrating;

public class HandStrength {
	
	public static double calcHandstrength(ArrayList<Card> playerCards, ArrayList<Card> commonCards, int numberOfActivePlayers) {
		
		ArrayList<Card> knownCards = new ArrayList<Card>();
		knownCards.addAll(playerCards);
		knownCards.addAll(commonCards);
		
		
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

		return Math.pow(((wins + ties / 2) / (wins + ties + losses)),
				(double) numberOfActivePlayers);
	}
}

