package de.pokerdude.players;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;
import de.pokerdude.game.Deck;
import de.pokerdude.game.PokerGame;
import de.pokerdude.utils.Powerrating;

public class PlayerAIHandStrength extends PlayerAI {

	static final Logger logger = Logger.getLogger(PlayerAIHandStrength.class);

	public PlayerAIHandStrength(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public PokerAction getBetPreTurn(int minBet) {
		double handStrength = calcHandstrength();
		int bet = 0;
		if(handStrength < 0.3) {
			return new FoldAction(game, this);
		}
		
		bet = (int) (30 * handStrength);
		if (bet < minBet) {
			return new CallAction(game, this);
		}
		return new RaiseAction(game, this, bet);
	}

	@Override
	public PokerAction getBetPreRiver(int minBet) {
		return getBetPreTurn(minBet);
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
			card1 = currentDeck.getCard();                  // First take a card
			deckToCompare = new Deck(currentDeck, false);   // Make a copy for
															// comparison work

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
