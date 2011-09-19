package de.pokerdude.opponentmodeling;

import java.util.ArrayList;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.PokerGame;
import de.pokerdude.game.GameState;

public class Context {

	protected GameState state;
	protected int numActivePlayers, numRaises;
	protected double potOdds;
	protected PokerAction action;
	protected ArrayList<Double> ratings;
	
	public Context(PokerAction action) {
		this.action = action;
		
		state = action.getGame().getState();
		numActivePlayers = action.getGame().getPlayersInRound().size();
		numRaises = action.getGame().getNumberOfRaises();
		
		int callAmount = 10; //Not yet implemented. I think in PokerGame the amount to call equals currentbet is that correct?
		int potSize = action.getGame().getPot();
		
		potOdds = callAmount / (callAmount + potSize);
		
		ratings = new ArrayList<Double>();
		
	}
	
	public void addRating(double val) {
		ratings.add(val);
	}
	
	public double getAverageRating() {
		if(ratings.size() == 0) return 0;
		
		double sum = 0;
		for(int i=0; i<ratings.size();i++) {
			sum += ratings.get(i);
		}
		return sum / ratings.size();
	}
	
	public boolean contains(PokerAction action) {
		
		//equal GameState, action and player		
		if((action.getGame().getState() == state) && 
		   (this.action.getClass() == action.getClass()) &&
		   (action.getPlayer().equals(this.action.getPlayer()))) {
			return true;
		}

		return false;
	}
	
	public String toString() {
		return action.getPlayer().name + ": " + getAverageRating();
	}
	
	
}
