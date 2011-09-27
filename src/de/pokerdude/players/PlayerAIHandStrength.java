package de.pokerdude.players;

import org.apache.log4j.Logger;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.PokerGame;
import de.pokerdude.simulation.HandStrength;

public class PlayerAIHandStrength extends PlayerAI {

	static final Logger logger = Logger.getLogger(PlayerAIHandStrength.class);

	public PlayerAIHandStrength(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public PokerAction getBetPreTurn(int minBet) {
		double handStrength = HandStrength.calcHandstrength(
				this.Cards, game.getCommonCards(), game.getPlayersInRound().size());
		
		double potOdds = game.getPotOdds(this);
		
		int callAmount = game.getCurrentBet() - this.lastBet;
		
		int raise = 20;
		
		
		if(handStrength > (potOdds+0.1)) {
			
			if(raise > callAmount)
				return new RaiseAction(game, this, game.getCurrentBet()+raise);
			else return new RaiseAction(game, this, game.getCurrentBet()+2);
		}
		else if(handStrength >= potOdds) {
			return new CallAction(game, this);
		}
		else return new FoldAction(game, this);
		
		
		/*
		int bet = (int)(30 * handStrength);
		if(handStrength < 0.3) {
			// Chances are bad -> fold
			return new FoldAction(game, this);
		} else if (bet <= minBet) {
			// We want to stay in,  but not bet more than we have to
			return new CallAction(game, this);
		} 
		// We want MORE!
		return new RaiseAction(game, this, bet); 
		*/
	}

	@Override
	public PokerAction getBetPreRiver(int minBet) {
		return getBetPreTurn(minBet);
	}
}
