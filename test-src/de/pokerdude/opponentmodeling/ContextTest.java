package de.pokerdude.opponentmodeling;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import de.pokerdude.actions.CallAction;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;

public class ContextTest {

	@Test
	public void testEqualsObject() {
		PokerGame game = new PokerGame();
		Player p1 = new Player("P1", game);
		Context c1 = new Context(new CallAction(game, p1), GameState.PREFLOP);
		Context c2 = new Context(new CallAction(game, p1), GameState.PREFLOP);
		assertTrue(c1.equals(c2));
		assertTrue(c1.hashCode() == c2.hashCode());
		
		HashMap<Context, String> map = new HashMap<Context, String>();
		map.put(c1, "test");
		assertTrue(map.containsKey(c2));
	}

}
