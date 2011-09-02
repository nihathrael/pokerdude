package de.pokerdude;

import java.util.ArrayList;

public class PreFlopPropability {
	private ArrayList<Card> hand;
	private double[] props;
	
	public PreFlopPropability(Card c1, Card c2, int maxNumPlayers) {
		hand = new ArrayList<Card>();
		hand.add(c1);
		hand.add(c2);
		props = new double[maxNumPlayers];
	}
	
	public PreFlopPropability(ArrayList<Card> Hand, int maxNumPlayers) {
		this.hand = Hand;
		props = new double[maxNumPlayers];
	}
	
	public void setProp(int numPlayers, double value) {
		props[numPlayers] = value;
	}
	
	public double getProp(int numPlayers) {
		return props[numPlayers];
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}

	
	
}
