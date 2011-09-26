package de.pokerdude.opponentmodeling;

import java.util.TreeSet;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;

public class Context {

	protected GameState state;
	protected int numActivePlayers, numRaises;
	protected double potOdds;
	protected PokerAction action;
	protected int potOddsBin;
	protected String commondCards;
	protected String identName;

	public Context(PokerAction action, GameState gameState) {
		this.action = action;

		state = gameState;
		PokerGame game = action.getGame();
		numActivePlayers = game.getPlayersInRound().size();
		numRaises = game.getNumberOfRaises();

		TreeSet<Card> cards = new TreeSet<Card>(game.getCommonCards());
		StringBuffer buffer = new StringBuffer(20);
		for (Card card : cards) {
			buffer.append(card.getValue());
			if (card.compareTo(cards.last()) != 0) {
				buffer.append('-');
			}
		}
		commondCards = buffer.toString();

		int callAmount = game.getCurrentBet() - action.getPlayer().lastBet;
		int potSize = game.getPot();
		if (callAmount + potSize > 0) {
			potOdds = callAmount / (callAmount + potSize);
		} else {
			potOdds = 0;
		}

		if (potOdds < 0.1) {
			potOddsBin = 0;
		} else if (potOddsBin < 0.2) {
			potOddsBin = 1;
		} else if (potOddsBin < 0.3) {
			potOddsBin = 2;
		} else {
			potOddsBin = 4;
		}
		
		identName = new StringBuffer().append("Context(").append(action.getPlayer())
				.append("PotOdds(").append(potOddsBin).append(") Ident(")
				.append(commondCards).append(')').append("State(")
				.append(state).append(")").toString();

	}

	public String toString() {
		return identName;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Context))
			return false;

		Context c = (Context) o;

		boolean stateMatch = this.state == c.state;
		boolean actionMatch = this.action.getClass()
				.equals(c.action.getClass());
		boolean playerMatch = this.action.getPlayer().name.equals(c.action
				.getPlayer().name);

		boolean potOddsMatch = potOddsBin == c.potOddsBin;
		boolean raisesMatch = numRaises == c.numRaises;
		boolean cardsMatch = commondCards.equals(c.commondCards);

		return stateMatch && actionMatch && playerMatch && potOddsMatch
				&& raisesMatch && cardsMatch;

	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

}
