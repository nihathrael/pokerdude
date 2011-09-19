package de.pokerdude.opponentmodeling;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;
import de.pokerdude.simulation.HandStrength;
import de.pokerdude.simulation.RolloutSimulation;



public class OpponentModelTable {
	
	static final Logger logger = Logger.getLogger(PokerGame.class);
	
	private ArrayList<Context> contexts;
	
	public OpponentModelTable() {
		contexts = new ArrayList<Context>();

	}
	
	
	public Context getContextThatContains(PokerAction action) {
		for(Context entry : contexts) {
			if(entry.contains(action)) {
				return entry;
			}
		}
		return null;
	}
	
	public double getAverageForContextThatContains(PokerAction action) {
		Context c = getContextThatContains(action);
		if(c == null) return 0;
		else return c.getAverageRating();
		
	}
	
	public void recordAction(PokerAction action) {
		
		double rating = calcRating(action);
		
		for(Context entry : contexts) {
			if(entry.contains(action)) {
				entry.addRating(rating);
				return;
			}
		}
		
		Context newContext = new Context(action);
		newContext.addRating(rating);
		contexts.add(newContext);
	}
	
	private double calcRating(PokerAction action) {
		PokerGame game = action.getGame();
		ArrayList<Card> commonCards = game.getCommonCards();
		ArrayList<Card> playerCards = action.getPlayer().getCards();
		
		if(commonCards.size() == 0) {
			RolloutSimulation rs = new RolloutSimulation();
			return rs.GetPropabilityFromList(playerCards, game.getNumberOfPlayers());			
		} else {
			return HandStrength.calcHandstrength(playerCards, commonCards, game.getPlayersInRound().size());
		}
		
	}
	
	
	public void print() {
		logger.info("Contexts:");
		
		for(Context c : contexts) {
			logger.info(c.toString());
			
		}
	}
	
	
}

	