package de.pokerdude;

public class PlayerAI extends Player {
	
	int numPlayers;
	
	public PlayerAI(String name, int numPlayers) {
		super(name);
		this.numPlayers = numPlayers;
	}
	
	public int getBetPreFlop() {
		RolloutSimulation RS = new RolloutSimulation();
		
		return (int)((double)Credits * RS.GetPropabilityFromList(Cards, numPlayers));
	}
	
}
