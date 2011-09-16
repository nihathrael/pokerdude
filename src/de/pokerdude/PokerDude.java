package de.pokerdude;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class PokerDude {
	
	static final Logger logger = Logger.getLogger(PokerDude.class);
	static final Level DEBUGLEVEL = Level.DEBUG;
	
	public static void main(String args[]) {
		BasicConfigurator.configure();
		logger.setLevel(DEBUGLEVEL);
		logger.info("Welcome to PokerDude!");
		PokerGame game = new PokerGame();
		game.addPlayer(new Player("P1"));
		game.addPlayer(new PlayerAI("P2",4));
		game.addPlayer(new PlayerAIHandStrength("P3", game));
		game.addPlayer(new Player("P4"));
		int rounds = 200;
		for(int i=0; i<rounds;i++) {
			game.playRound();
		}
		game.showCredits();
		
		// Here all the fun starts
	}

}
