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

	HashMap<Context, ArrayList<Double>> modelTable;

	static RolloutSimulation rs = new RolloutSimulation();

	public ArrayList<Context> contextBuffer = new ArrayList<Context>();

	public OpponentModelTable() {
		modelTable = new HashMap<Context, ArrayList<Double>>();

	}

	public double getAverageForContext(Context context) {
		ArrayList<Double> ratings = modelTable.get(context);
		
		if (ratings == null)
			return 0;

		double sum = 0;
		for (int i = 0; i < ratings.size(); i++)
			sum += ratings.get(i);

		return sum / ratings.size();
	}

	public void recordAction(PokerAction action, GameState gameState) {
		Context c = new Context(action, gameState);
		contextBuffer.add(c);
	}

	public void calculateRatings() {
		for (Context cont : contextBuffer) {
			ArrayList<Double> ratings = modelTable.get(cont);
			double rating = calcRating(cont);

			if (ratings == null) {
				ratings = new ArrayList<Double>();
				ratings.add(rating);
				modelTable.put(cont, ratings);
				return;
			} else {
				ratings.add(rating);
			}
		}
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
			return rs.GetPropabilityFromList(playerCards,
					game.getNumberOfPlayers());
		} else {
			return HandStrength.calcHandstrength(playerCards, commonCards, game
					.getPlayersInRound().size());
		}

	}

	public void print() {
	}

}
