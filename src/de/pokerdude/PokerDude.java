package de.pokerdude;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;
import de.pokerdude.players.PlayerAI;
import de.pokerdude.players.PlayerAIHandStrength;

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
		game.addPlayer(new PlayerAIHandStrength("P3", game));
		game.addPlayer(new Player("P4", game));
		
		int rounds = 100;
		int displayevery = 10;

		for(int i=0; i<rounds;i++) {
			game.playRound();
			if(i%displayevery ==0) {
				logger.info(i);
				game.showCredits();
			}
		}
		game.showCredits();
		game.getOpponenModelTable().print();
		
		// Here all the fun starts
	}

}
