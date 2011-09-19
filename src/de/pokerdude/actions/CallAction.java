package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class CallAction implements PokerAction {
	
	private final PokerGame game;
	private final Player player;
	
	public CallAction(PokerGame game, Player player) {
		this.game = game;
		this.player = player;
	}

	@Override
	public void execute() {
		game.call(player);
	}

}
