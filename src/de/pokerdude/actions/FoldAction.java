package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class FoldAction implements PokerAction {
	
	private final PokerGame game;
	private final Player player;
	
	public FoldAction(PokerGame game, Player player) {
		this.game = game;
		this.player = player;
	}

	@Override
	public void execute() {
		game.foldPlayer(player);
	} 

}
