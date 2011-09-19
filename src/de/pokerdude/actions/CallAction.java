package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class CallAction extends PokerAction {
	
	public CallAction(PokerGame game, Player player) {
		super(game, player);
	}

	@Override
	public void execute() {
		super.execute();
		game.call(player);
	}

}
