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
import de.pokerdude.simulation.HandStrength;
import de.pokerdude.utils.Powerrating;

public class PlayerAIHandStrength extends PlayerAI {

	static final Logger logger = Logger.getLogger(PlayerAIHandStrength.class);

	public PlayerAIHandStrength(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public PokerAction getBetPreTurn(int minBet) {
		double handStrength = HandStrength.calcHandstrength(
				this.Cards, game.getCommonCards(), game.getPlayersInRound().size());
		
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
}
