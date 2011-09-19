package de.pokerdude.actions;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class FoldAction extends PokerAction {
	
	public FoldAction(PokerGame game, Player player) {
		super(game, player);
	}

	@Override
	public void execute() {
		super.execute();
		game.foldPlayer(player);
	} 

}
