package de.pokerdude.opponentmodeling;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.PokerGame;
import de.pokerdude.simulation.HandStrength;
import de.pokerdude.simulation.RolloutSimulation;


public class OpponentModelTable {
	
	static final Logger logger = Logger.getLogger(PokerGame.class);
	
	HashMap<Context, ArrayList<Double>> ModelTable;
	
	static RolloutSimulation rs = new RolloutSimulation();

	
	
	public OpponentModelTable() {
		ModelTable = new HashMap<Context, ArrayList<Double>>();
		
	}
	
	public void startNewRound() {
		
	}
	
	
	public ArrayList<Double> getRatingsForAction(PokerAction action) {
		
		Context queryContext = new Context(action);
		return ModelTable.get(queryContext);	
		
		
	}
	
	public double getAverageForContextThatContains(PokerAction action) {
		ArrayList<Double> ratings = getRatingsForAction(action);
		
		if(ratings == null) return 0;
		
		double sum=0;
		for(int i=0; i<ratings.size();i++)
			sum += ratings.get(i);
		
		return sum / ratings.size();
		
	}
	
	public void recordAction(PokerAction action) {
		
		double rating = calcRating(action);
		
		Context c = new Context(action);
		
		ArrayList<Double> ratings = ModelTable.get(c);
		
		if(ratings == null) {
			ratings = new ArrayList<Double>();
			ratings.add(rating);
			ModelTable.put(c, ratings);
			return;
		} else {
			ratings.add(rating);
		}	
	}
	
	private double calcRating(PokerAction action) {
		
		PokerGame game = action.getGame();
		ArrayList<Card> commonCards = game.getCommonCards();
		ArrayList<Card> playerCards = action.getPlayer().getCards();
		
		if(commonCards.size() == 0) {
			return rs.GetPropabilityFromList(playerCards, game.getNumberOfPlayers());			
		} else {
			return HandStrength.calcHandstrength(playerCards, commonCards, game.getPlayersInRound().size());
		}
		
		
	}
	
	
	public void print() {
	}
	
	
}

	