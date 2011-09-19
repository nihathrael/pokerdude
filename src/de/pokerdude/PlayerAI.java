package de.pokerdude;

import org.apache.log4j.Logger;

public class PlayerAI extends Player {
	
	static final Logger logger = Logger.getLogger(PlayerAI.class);
	
	protected static RolloutSimulation RS = new RolloutSimulation();
	protected PokerGame game;
	
	public PlayerAI(String name, PokerGame game) {
		super(name);
		this.game = game;
		logger.setLevel(PokerDude.DEBUGLEVEL);
	}
	
	@Override
	public int getBetPreFlop() {
		double prop = RS.GetPropabilityFromList(Cards, game.getNumberOfPlayers());
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
