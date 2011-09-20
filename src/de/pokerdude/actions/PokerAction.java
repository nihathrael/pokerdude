package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public abstract class PokerAction {
	
	protected final PokerGame game;
	protected final Player player;
	
	public PokerAction(PokerGame game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	public Player getPlayer() { return player; }
	public PokerGame getGame() { return game; }
	
	public void execute() {
		player.model.recordAction(this, game.getState());
	}

}
