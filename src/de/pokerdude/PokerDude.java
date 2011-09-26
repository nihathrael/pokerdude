package de.pokerdude;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;
import de.pokerdude.players.PlayerAI;
import de.pokerdude.players.PlayerAIHandStrength;
import de.pokerdude.players.PlayerAIModelling;

public class PokerDude {
	
	static final Logger logger = Logger.getLogger(PokerDude.class);
	public static final Level DEBUGLEVEL = Level.INFO;
	
	public static void main(String args[]) {
		BasicConfigurator.configure();
		logger.setLevel(DEBUGLEVEL);
		logger.info("Welcome to PokerDude!");
		PokerGame game = new PokerGame();
		
		game.addPlayer(new Player("P1", game));
		game.addPlayer(new PlayerAI("P2", game));
		game.addPlayer(new PlayerAIModelling("P3", game));
		game.addPlayer(new PlayerAIHandStrength("P4", game));
		
		int rounds = 10000;
		int displayevery = 100;

		for(int i=0; i<rounds;i++) {
			game.playRound();
			if(i%displayevery ==0) {
				logger.info("Round: " + i);
				game.showCredits();
			}
		}
		game.showCredits();
		
		// Here all the fun starts
	}

}
