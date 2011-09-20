package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class RaiseAction extends PokerAction {

	private int amount;
	
	public RaiseAction(PokerGame game, Player player, int amount) {
		super(game, player);
		this.amount = amount;
	}

	@Override
	public void execute() {
		super.execute();
		game.raise(player, amount);
	}

}
