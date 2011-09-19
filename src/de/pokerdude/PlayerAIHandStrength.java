package de.pokerdude;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class PlayerAIHandStrength extends PlayerAI {

	static final Logger logger = Logger.getLogger(PlayerAIHandStrength.class);

	public PlayerAIHandStrength(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public int getBetPreTurn() {
		double handStrength = calcHandstrength();
		int bet = 0;
		if(handStrength < 0.3) {
			bet = (int) (30 * handStrength);
		}

		return bet;
	}

	@Override
	public int getBetPreRiver() {
		return getBetPreTurn();
	}

	private ArrayList<Card> getKnownCards() {
		ArrayList<Card> known = game.getCommonCards();
		known.addAll(this.Cards);
		return known;
	}

	private double calcHandstrength() {

		Deck currentDeck = new Deck(getKnownCards());
		Deck deckToCompare = null;

		double wins = 0;
		double ties = 0;
		double losses = 0;

		Card card1, card2;

		ArrayList<Card> playerCards = getKnownCards();
		CardSet setPlayer = new CardSet(playerCards);
		Powerrating playerResult = setPlayer.evaluate();
		ArrayList<Card> opponentCards = new ArrayList<Card>(10);

		while (currentDeck.size() > 0) {
			card1 = currentDeck.getCard(); // First take a card
			deckToCompare = new Deck(currentDeck, false); // Make a copy for comparison work

			while (deckToCompare.size() > 0) {
				card2 = deckToCompare.getCard();

				opponentCards.clear();
				opponentCards.add(card1);
				opponentCards.add(card2);
				opponentCards.addAll(game.getCommonCards());

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

		// TODO: (!!!) only consider active players
		return Math.pow(((wins + ties / 2) / (wins + ties + losses)),
				(double) game.getPlayers().size());
	}

}
