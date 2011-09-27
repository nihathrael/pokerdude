package de.pokerdude.players;

import java.util.ArrayList;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;
import de.pokerdude.opponentmodeling.Context;
import de.pokerdude.opponentmodeling.OpponentModelTable;
import de.pokerdude.simulation.HandStrength;

public class PlayerAIModelling2 extends PlayerAIModelling {

	public PlayerAIModelling2(String name, PokerGame game) {
		super(name, game);
	}

	@Override
	public PokerAction getBetPreTurn(int minBet) {

		ArrayList<Double> results = new ArrayList<Double>();
		double handStrength = HandStrength.calcHandstrength(this.Cards,
				game.getCommonCards(), game.getPlayersInRound().size());
		int wins = 0;
		for (Player player : game.getPlayersInRound()) {
			if (player == this) {
				continue;
			}
			OpponentModelTable model = player.model;
			double result = 0.0;

			Context currentContext = model.contextBuffer
					.get(model.contextBuffer.size() - 1);

			int numContexts = 0;
			while (currentContext.getState() != GameState.PREFLOP) {
				result *= numContexts;
				double avg = model.getAverageForContext(currentContext);
				result += avg;
				numContexts++;
				result /= numContexts;
				currentContext = model.contextBuffer.get(model.contextBuffer
						.size() - numContexts - 1);
			}

			if (result > 0) {
				results.add(result);
			}
			if (result < handStrength) {
				wins++;
			}
		}

		int callAmount = game.getCurrentBet() - this.lastBet;
		
		if (wins == 0 && results.size() >= 2) {
			return new FoldAction(game, this);
		}

		if (wins == game.getPlayersInRound().size() - 1) {
			return new RaiseAction(game, this, game.getCurrentBet()
					+ (int)(callAmount * 1.5));
		}

		double potOdds = game.getPotOdds(this);
		if (handStrength > (potOdds + 0.2)) {
			return new RaiseAction(game, this, game.getCurrentBet()
					+ (int)(callAmount * 1.5));
		} else if (handStrength >= potOdds) {
			return new CallAction(game, this);
		} else {
			return new FoldAction(game, this);
		}

	}

}
