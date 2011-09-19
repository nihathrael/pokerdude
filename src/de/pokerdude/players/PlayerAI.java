package de.pokerdude.players;

import org.apache.log4j.Logger;

import de.pokerdude.PokerDude;
import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.PokerGame;
import de.pokerdude.simulation.RolloutSimulation;

public class PlayerAI extends Player {
	
	static final Logger logger = Logger.getLogger(PlayerAI.class);
	
	protected static RolloutSimulation RS = new RolloutSimulation();
	protected PokerGame game;
	
	public PlayerAI(String name, PokerGame game) {
		super(name, game);
		this.game = game;
		logger.setLevel(PokerDude.DEBUGLEVEL);
	}
	
	@Override
	public PokerAction getBetPreFlop(int minBet, boolean force) {
		double prop = RS.GetPropabilityFromList(Cards, game.getNumberOfPlayers());
		if(!force && prop < 0.3) {
			return new FoldAction(game, this);
		}
		int bet = (int)(30 * prop);
		if(bet < minBet) {
			return new CallAction(game, this);
		}
		logger.debug(name + " betting " + bet);
		return new RaiseAction(game, this, bet);
	}
	
	
	@Override
	public PokerAction getBetPreTurn(int minBet) {
		return getBetPreFlop(minBet, false);
	}
	
	@Override
	public PokerAction getBetPreRiver(int minBet) {
		return getBetPreFlop(minBet, false);
	}
	
}
