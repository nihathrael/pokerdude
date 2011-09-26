package de.pokerdude.players;

import java.util.ArrayList;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;
import de.pokerdude.game.PokerGame;
import de.pokerdude.utils.Powerrating;

public class PlayerIRaiser extends Player {

	public PlayerIRaiser(String name, PokerGame game) {
		super(name, game);
	}
	
	public PokerAction getBetPreFlop(int minBet, boolean b) {
		if(b) {
			return new RaiseAction(game, this, minBet);
		}
		if(Math.random() > 0.3) {
			return new RaiseAction(game, this, minBet+10);
		}
		return new CallAction(game, this);
	}
	
	public PokerAction getBetPreTurn(int minBet) {
		ArrayList<Card> allCards = getCards();
		allCards.addAll(game.getCommonCards());
		CardSet cardset = new CardSet(allCards);
		Powerrating rating = cardset.evaluate();
		if(rating.compareTo(new Powerrating(new int[] {1, 0})) <= 0) {
			new FoldAction(game, this);
		}
		return new CallAction(game, this);
	}

}
