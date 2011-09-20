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
	public PokerAction getBetPreFlop(int minBet, boolean forcedBlind) {
		double prop = RS.GetPropabilityFromList(Cards, game.getNumberOfPlayers());
		int bet = (int)(30 * prop);
		if(forcedBlind) {
			// We have to because of forcedBlind
			if(prop < 0.3 || bet < minBet) {
				// We really don't want to play (that much), but we have to
				return new RaiseAction(game, this, minBet);
			} else {
				// We want to bet more than we have to!
				return new RaiseAction(game, this, bet);
			}
		} else {
			if(prop < 0.3) {
				// Chances are bad -> fold
				return new FoldAction(game, this);
			} else if (bet <= minBet) {
				// We want to stay in,  but not bet more than we have to
				return new CallAction(game, this);
			} 
			// We want MORE!
			return new RaiseAction(game, this, bet); 
		}
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
