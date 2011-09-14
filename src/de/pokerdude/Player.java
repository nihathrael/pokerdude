package de.pokerdude;

import java.util.ArrayList;



public class Player {
	
	final String name;
	int credits;
	ArrayList<Card> Cards;
	
	public Player(String name) {
		this.name = name;
		credits = 1000;
	}
	
	public ArrayList<Card> getCards() {
		return Cards;
	}
	
	public void setCards(ArrayList<Card> Cards) {
		this.Cards = Cards;
		System.out.println(name+": I just got two cards:");
		System.out.println(Cards.get(0));
		System.out.println(Cards.get(1));
	}
	
	public int getBetPreFlop() {
		return 10;
	}
	
	public int getBetPreTurn() {
		return 10;
	}
	
	public int getBetPreRiver() {
		return 10;
	}
}
