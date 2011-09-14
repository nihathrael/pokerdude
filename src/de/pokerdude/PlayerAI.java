package de.pokerdude;

import org.apache.log4j.Logger;

public class PlayerAI extends Player {
	
	static final Logger logger = Logger.getLogger(PokerDude.class);
	
	int numPlayers;
	RolloutSimulation RS = new RolloutSimulation();
	
	public PlayerAI(String name, int numPlayers) {
		super(name);
		this.numPlayers = numPlayers;
		logger.setLevel(PokerDude.DEBUGLEVEL);
	}
	
	@Override
	public int getBetPreFlop() {
		double prop = RS.GetPropabilityFromList(Cards, numPlayers);
		if(prop < 0.3) {
			return 0;
		}
		int bet = (int)(30 * prop);
		logger.debug(name + " betting " + bet);
		return bet;
	}
	
	
	@Override
	public int getBetPreTurn() {
		return getBetPreFlop();
	}
	
	@Override
	public int getBetPreRiver() {
		return getBetPreFlop();
	}
	
}
