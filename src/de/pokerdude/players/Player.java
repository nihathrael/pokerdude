package de.pokerdude.players;

import java.util.ArrayList;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;



public class Player {
	
	public final String name;
	public int credits;
	ArrayList<Card> Cards;
	final PokerGame game;
	public int lastBet=0;
	
	public Player(String name, PokerGame game) {
		this.name = name;
		this.game = game;
		credits = 1000;
	}
	 
	public ArrayList<Card> getCards() {
		return Cards;
	}
	
	public void setCards(ArrayList<Card> Cards) {
		this.Cards = Cards;
	}
	
	public PokerAction getBet(GameState state, int minBet, boolean force) {
		switch(state) {
		case PREFLOP:
			return getBetPreFlop(minBet, force);
		case PRETURN:
			return getBetPreTurn(minBet);
		case PRERIVER:
			return getBetPreRiver(minBet);
		case POSTRIVER:
			return getBetPreRiver(minBet);
		default:
			return getBetPreFlop(minBet, force);
		}
	}
	
	public PokerAction getBetPreFlop(int i, boolean b) {
		if(b) {
			return new RaiseAction(game, this, i);
		}
		return new CallAction(game, this);
	}
	
	public PokerAction getBetPreTurn(int minBet) {
		return new CallAction(game, this);
	}
	
	public PokerAction getBetPreRiver(int minBet) {
		return new CallAction(game, this);
	}
	
	public String toString() {
		return "Player("+ name + ")";
	}
}
