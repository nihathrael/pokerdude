package de.pokerdude;

import java.util.ArrayList;



public class Player {
	ArrayList<Card> Cards;
	
	public Player() {
	}
	
	public ArrayList<Card> getCards() {
		return Cards;
	}
	
	public void setCards(ArrayList<Card> Cards) {
		this.Cards = Cards;
	}
}
