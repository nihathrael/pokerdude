package de.pokerdude.players;

import java.util.ArrayList;

import de.pokerdude.actions.CallAction;
import de.pokerdude.actions.FoldAction;
import de.pokerdude.actions.PokerAction;
import de.pokerdude.actions.RaiseAction;
import de.pokerdude.game.Card;
import de.pokerdude.game.CardSet;
import de.pokerdude.game.GameState;
import de.pokerdude.game.PokerGame;
import de.pokerdude.opponentmodeling.OpponentModelTable;
import de.pokerdude.utils.Powerrating;



public class Player {
	
	public final String name;
	public int credits;
	ArrayList<Card> Cards;
	final PokerGame game;
	public int lastBet=0;
	public OpponentModelTable model = new OpponentModelTable();
	
	public Player(String name, PokerGame game) {
		this.name = name;
		this.game = game;
		credits = 1000;
	}
	 
	public ArrayList<Card> getCards() {
		return new ArrayList<Card>(Cards);
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
	
	public PokerAction getBetPreFlop(int minBet, boolean b) {
		if(b) {
			return new RaiseAction(game, this, minBet);
		}
		if(Math.random() > 0.5) {
			return new RaiseAction(game, this, minBet+10);
		}
		return new CallAction(game, this);
	}
	
	public PokerAction getBetPreTurn(int minBet) {
		ArrayList<Card> allCards = getCards();
		allCards.addAll(game.getCommonCards());
		CardSet cardset = new CardSet(allCards);
		Powerrating rating = cardset.evaluate();
		if(rating.compareTo(new Powerrating(new int[] {2, 0})) <= 0) {
			new FoldAction(game, this);
		}
		return new CallAction(game, this);
	}
	
	public PokerAction getBetPreRiver(int minBet) {
		return getBetPreTurn(minBet);
	}
	
	public String toString() {
		return "Player("+ name + ")";
	}
}
