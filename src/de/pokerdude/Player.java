package de.pokerdude;

import java.util.ArrayList;



public class Player {
	
	final String name;
	ArrayList<Card> Cards;
	
	public Player(String name) {
		this.name = name;
	}
	
	public ArrayList<Card> getCards() {
		return Cards;
	}
	
	public void setCards(ArrayList<Card> Cards) {
		this.Cards = Cards;
	}
}
