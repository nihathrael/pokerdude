package de.pokerdude;

public class PokerDude {
	
	public static void main(String args[]) {
		System.out.println("Welcome to PokerDude!");
		PokerGame game = new PokerGame();
		game.addPlayer(new Player("P1"));
		//game.addPlayer(new PlayerAI("P2",4));
		game.addPlayer(new Player("P3"));
		game.addPlayer(new Player("P4"));
		int rounds = 100;
		for(int i=0; i<rounds;i++) {
			game.playRound();
		}
		
		// Here all the fun starts
	}

}
