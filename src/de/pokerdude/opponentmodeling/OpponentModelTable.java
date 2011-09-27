package de.pokerdude.opponentmodeling;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;
import de.pokerdude.simulation.HandStrength;
import de.pokerdude.simulation.RolloutSimulation;

public class OpponentModelTable {

	static final Logger logger = Logger.getLogger(PokerGame.class);

	HashMap<Context, ContextAverage> modelTable;

	static RolloutSimulation rs = new RolloutSimulation();

	public OpponentModelTable() {
		modelTable = new HashMap<Context, ContextAverage>(1000);

	}

	public void startNewRound() {

	}

	public ArrayList<Context> contextBuffer = new ArrayList<Context>();

	public void recordAction(PokerAction action, GameState gameState) {
		Context c = new Context(action, gameState);
		contextBuffer.add(c);
	}

	public void calculateRatings() {
		for (Context cont : contextBuffer) {
			if(cont.state.equals(GameState.PREFLOP)) {
				// Skip preflop because there is no information!
				continue;
			}
			ContextAverage contextAverage = modelTable.get(cont);
			double rating = calcRating(cont);

			if (contextAverage == null) {
				contextAverage = new ContextAverage(rating);
				modelTable.put(cont, contextAverage);
			} else {
				logger.debug("Ratings size: " + contextAverage.size());
				logger.debug(cont);
				contextAverage.addEntry(rating);
				//if(contextAverage.size() > 10000) {
				//	logger.debug(cont + " size: " + contextAverage.size());
				//}
			}
		}
		logger.debug("Modeltable entries: " + modelTable.size());
	}

	private double calcRating(Context cont) {
		PokerAction action = cont.action;
		PokerGame game = action.getGame();
		ArrayList<Card> commonCards = null;
		switch (cont.state) {
		case PREFLOP:
			commonCards = new ArrayList<Card>(0);
			break;
		case PRETURN:
			commonCards = game.getFlop();
			break;
		case PRERIVER:
			commonCards = new ArrayList<Card>(game.getFlop());
			commonCards.add(game.getTurn());
			break;
		case POSTRIVER:
			commonCards = new ArrayList<Card>(game.getCommonCards());
			break;
		}
		ArrayList<Card> playerCards = action.getPlayer().getCards();

		if (commonCards.size() == 0) {
			return rs.getPropabilityFromList(playerCards,
					game.getNumberOfPlayers());
		} else {
			return HandStrength.calcHandstrength(playerCards, commonCards, game
					.getPlayersInRound().size());
		}

	}

	class ContextAverage {
		private double runningAverage;
		private int numberEntries;

		public ContextAverage(double average) {
			runningAverage = average;
			numberEntries = 1;
		}

		public double addEntry(double value) {
			runningAverage *= numberEntries;
			numberEntries++;
			runningAverage += value;
			runningAverage /= numberEntries;
			
			return runningAverage;
		}

		public double getAverage() {
			return runningAverage;
		}

		public int size() {
			return numberEntries;
		}
	}

	public double getAverageForContext(Context context) {
		ContextAverage contextAverage = modelTable.get(context);
		if (contextAverage == null) {
			return 0;
		}
		return contextAverage.getAverage();
	}

}
