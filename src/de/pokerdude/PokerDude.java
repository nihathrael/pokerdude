package de.pokerdude;

public class PokerDude {
	
	public static void main(String args[]) {
		System.out.println("Welcome to PokerDude!");
		PokerGame game = new PokerGame();
		game.addPlayer(new Player("P1"));
		game.addPlayer(new Player("P2"));
		game.startGame();
		// Here all the fun starts
	}

}
