package de.pokerdude;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.pokerdude.game.PokerGame;
import de.pokerdude.players.Player;
import de.pokerdude.players.PlayerAI;
import de.pokerdude.players.PlayerAIHandStrength;
import de.pokerdude.players.PlayerAIModelling;
import de.pokerdude.players.PlayerAIModelling2;
import de.pokerdude.players.PlayerIRaiser;

public class PokerDude {
	
	static final Logger logger = Logger.getLogger(PokerDude.class);
	public static final Level DEBUGLEVEL = Level.INFO;
	
	public static void main(String args[]) {
		BasicConfigurator.configure();
				
		int rounds = 2000;

		
		logger.setLevel(DEBUGLEVEL);
		logger.info("Welcome to PokerDude!");
		
		logger.info("=========================================");
		logger.info("Phase I: 6 simple players");
		logger.info("Rounds to play: " + rounds);
		PokerGame game = new PokerGame();
		game.addPlayer(new Player("Phase I - simple - 1", game));
		game.addPlayer(new Player("Phase I - simple - 2", game));
		game.addPlayer(new PlayerIRaiser("Phase I - raiser - 1", game));
		game.addPlayer(new Player("Phase I - simple - 3", game));
		game.addPlayer(new PlayerIRaiser("Phase I - raiser - 2", game));
		game.addPlayer(new PlayerIRaiser("Phase I - raiser - 3", game));


		playRounds(rounds, game, false);
		
		logger.info("=========================================");
		logger.info("Phase II: 4 mixed players of type 1 and 2");
		logger.info("Rounds to play: " + rounds);
		game = new PokerGame();		
		game.addPlayer(new Player("Phase I - simple", game));
		game.addPlayer(new PlayerIRaiser("Phase I - raiser", game));
		game.addPlayer(new PlayerAI("Phase II - only rollout", game));
		game.addPlayer(new PlayerAIHandStrength("Phase II - rollout + HS", game));
		
		playRounds(rounds, game, false);
		
		logger.info("=========================================");
		logger.info("Phase III: 5 mixed players of all types");
		logger.info("Rounds to play: " + rounds);
		game = new PokerGame();		
		game.addPlayer(new Player("Phase I - simple", game));
		game.addPlayer(new PlayerAI("Phase II - only rollout", game));
		game.addPlayer(new PlayerAIHandStrength("Phase II - rollout + HS", game));
		game.addPlayer(new PlayerAIModelling("Phase III - Moddeling", game));
		game.addPlayer(new PlayerAIModelling2("Phase III - Moddeling V2", game));
		
		playRounds(rounds, game, true);
		
	}

	private static void playRounds(int rounds, PokerGame game, boolean gatherStatistics) {
		for(int i=12; i<=rounds;i++) {
			if(i%(rounds*0.25) ==0) {
				logger.info("Played rounds: " + i + "/" + rounds);
			}
			game.playRound(gatherStatistics);
		}
		logger.info("Results:");
		game.showCredits();
	}

}
