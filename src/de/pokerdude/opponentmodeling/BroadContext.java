package de.pokerdude.opponentmodeling;

import de.pokerdude.actions.PokerAction;
import de.pokerdude.game.PokerGame;

public class BroadContext extends Context{
	
	int raiseBucketSize=5;

	public BroadContext(PokerAction action) {
		super(action);
	}	

	public BroadContext(PokerAction action, int bucketSizeForNumberOfRaises) {
		super(action);
		this.raiseBucketSize = bucketSizeForNumberOfRaises;
	}
	
	
	public boolean isInContext(PokerAction action) {
		boolean result = false;
		
		if((this.numRaises / raiseBucketSize) == (action.getGame().getNumberOfRaises() / raiseBucketSize))
			result = true;
		
		return result && super.contains(action);
	}
}
