package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class RaiseAction implements PokerAction {

	private final PokerGame game;
	private final Player player;
	private int amount;
	
	public RaiseAction(PokerGame game, Player player, int amount) {
		this.game = game;
		this.player = player;
		this.amount = amount;
	}

	@Override
	public void execute() {
		game.bet(player, amount);
	}

}
