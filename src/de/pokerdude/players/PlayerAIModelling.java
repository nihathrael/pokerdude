package de.pokerdude.players;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.PokerGame;
import de.pokerdude.opponentmodeling.OpponentModelTable;
import de.pokerdude.simulation.HandStrength;

public class PlayerAIModelling extends PlayerAI {

	static final Logger logger = Logger.getLogger(PlayerAIModelling.class);

	public PlayerAIModelling(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public PokerAction getBetPreTurn(int minBet) {
		ArrayList<Double> results = new ArrayList<Double>();
		double handStrength = HandStrength.calcHandstrength(
				this.Cards, game.getCommonCards(), game.getPlayersInRound().size());
		int wins = 0;
		for(Player player: game.getPlayersInRound()) {
			if(player == this) {
				continue;
			}
			OpponentModelTable model = player.model;
			double result = model.getAverageForContext(model.contextBuffer.get(model.contextBuffer.size()-1));
			if(result > 0) {
				results.add(result);
			}
			if(result < handStrength) {
				wins++;
			}
		}
		if(wins == 0 && results.size() > 0) {
			return new FoldAction(game, this);
		}
		if(wins == game.getPlayersInRound().size()-1) {
			return new RaiseAction(game, this, minBet*2);
		}
		
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
	}

}
