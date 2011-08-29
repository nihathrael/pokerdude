package de.pokerdude;

import java.util.ArrayList;



public class RolloutSimulation {
	public void PreFlopRollouts() {
		ArrayList<Player> DifferentHands = new ArrayList();
		
		for(int i=2; i<15; i++) {
			for(int j=2; j<i; j++) {
				Player p = new Player("");
				ArrayList<Card> Hand = new ArrayList<Card>();
				Hand.add(new Card(Suite.CLUBS, i));
				Hand.add(new Card(Suite.DIAMONDS, j));
				p.setCards(Hand);
				
				DifferentHands.add(p);
			}
		}
		
	}
	
	//Deck
	//Game
	public int rollout(Player p, PokerGame g) {
		Deck d = new Deck(g.getDeck());
		
		Card Turn = g.getTurn();
		if(Turn == null) Turn = d.getCard();
		Card River = g.getRiver();
		if(River == null) River = d.getCard();
		
		
		ArrayList<Card> CommonCards = new ArrayList<Card>();
		CommonCards.add(River);
		CommonCards.add(Turn);
		CommonCards.addAll(g.getFlop());
		
		return doRollout(p, CommonCards, g.getPlayers().size(), d);
	}
	
	
	public int rollout(Player p, ArrayList<Card> CommonCards, int NumPlayers, Deck _d) {
		Deck d = new Deck(_d);	
		while(CommonCards.size()<5) CommonCards.add(d.getCard());
		return doRollout(p,CommonCards,NumPlayers,d);
	}
	
	
	
	private int doRollout(Player p, ArrayList<Card> CommonCards, int NumPlayers, Deck d) {
		
		ArrayList<Card> PlayerCards = d.getCards(NumPlayers*2-2);
		int PlayerRating = 0; //PokerUtil.EvaluateCardSet(p.getCards(), CommonCards);
		boolean tie=false;
		
		for(int i=0;i<(NumPlayers-1);i++) {
			int Rating = 0; //PokerUtil.EvaluateCardSet(PlayerCards.subList(i, i+2));
			if(PlayerRating == Rating) tie=true;;
			if(PlayerRating < Rating) return -1;	
		}
		
		if(tie=true) return 0;
		else return 1;
	}
}
