package de.pokerdude;

import java.util.ArrayList;



public class RolloutSimulation {
	
	//List of propapilities filled by CalculatePreFlopPropabilities
	private ArrayList<PreFlopPropability> PFProps = null;
	
	public void CalculatePreFlopPropabilities(int R) {
		int numPlayers = 10;
		PFProps = new ArrayList<PreFlopPropability>();
		
		for(int i=2; i<15; i++) {
			for(int j=2; j<=i; j++) {
				PreFlopPropability p;
				//Prototype hands for all unsuited equivalence classes including pairs
				p = new PreFlopPropability(new Card(Suite.CLUBS, j), new Card(Suite.DIAMONDS, i), numPlayers);
				PFProps.add(p);
				
					
				//Suited prototypes 
				if(i!=j) {
					p = new PreFlopPropability(new Card(Suite.CLUBS, j), new Card(Suite.CLUBS, i), numPlayers);
					PFProps.add(p);
				}
			}
		}
		
		
		//now there is a hand for each equivalence class in PFProps and the
		//propabilities can be calculated
		for(PreFlopPropability prop: PFProps) {
			for(int i=2; i<=numPlayers; i++) {
				double value = PropabilityForRRollouts(prop.getHand(), R, i);
				prop.setProp(i, value);
				
			}
			System.out.println(prop.toString());
			
		}
	}
	
	public double PropabilityForRRollouts(ArrayList<Card> Hand, int R, int numPlayers) {
		double wins=0, ties=0, losses=0;
		
		for(int i=0; i<R; i++) {
			int result = rollout(Hand, null, numPlayers, new Deck(Hand));
			if(result == 0) ties++;
			else if(result == 1) wins++;
			else if(result == -1) losses++;
		}
		
		return wins/(wins+ties+losses);
	}
	
	//crap performance, dont care because not used for calculation of preflopprops
	public double GetPropabilityFromList(ArrayList<Card> Hand, int numPlayers) {
		ArrayList<Card> normHand = normalizeHand(Hand);
		
		//ToDo: Read from file
		if(PFProps == null) CalculatePreFlopPropabilities(100);
		
		for(PreFlopPropability p: PFProps) {
			if(compareHands(normHand, p.getHand())) {
				
				return p.getProp(numPlayers);
			}
				
		}
		

		
		return -1;

	}
	
	
	public boolean compareHands(ArrayList<Card> Hand1, ArrayList<Card> Hand2) {
		for(int i=0;i<2;i++) {
			Card Card1 = Hand1.get(i);
			Card Card2 = Hand2.get(i);
			
			if(Card1.getSuite() != Card2.getSuite()) return false;
			if(Card1.getValue() != Card2.getValue()) return false;
		}
		return true;
		
	}
	
	public ArrayList<Card> normalizeHand(ArrayList<Card> Hand) {
		Suite s1 = Hand.get(0).getSuite();
		Suite s2 = Hand.get(1).getSuite();
		int v1 = Hand.get(0).getValue();
		int v2 = Hand.get(1).getValue();
		if(s1 == s2) {
			s1 = Suite.CLUBS;
			s2 = Suite.CLUBS;
		} else {
			s1 = Suite.CLUBS;
			s2 = Suite.DIAMONDS;
		}
		
		if(v1 > v2) {
			int temp = v1;
			v1 = v2;
			v2 = temp; 			
		}
		ArrayList<Card> resultHand = new ArrayList<Card>();
		resultHand.add(new Card(s1,v1));
		resultHand.add(new Card(s2,v2));
		return resultHand;
	}
	
	
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
		
		return evalRollout(p.getCards(), CommonCards, g.getPlayers().size(), d);
	}
	
	
	public int rollout(ArrayList<Card> PlayerCards, ArrayList<Card> CommonCards, int NumPlayers, Deck _d) {
		Deck d = new Deck(_d);	
		if(CommonCards==null) CommonCards = new ArrayList<Card>();
		while(CommonCards.size()<5) CommonCards.add(d.getCard());
		return evalRollout(PlayerCards,CommonCards,NumPlayers,d);
	}
	
	
	
	private int evalRollout(ArrayList<Card> PlayerCards, ArrayList<Card> CommonCards, int NumPlayers, Deck d) {
		
		ArrayList<Card> OtherPlayerCards = d.getCards(NumPlayers*2-2);
		ArrayList<Card> allcards = new ArrayList<Card>();
		
		allcards.addAll(PlayerCards);
		allcards.addAll(CommonCards);
		CardSet cardset = new CardSet(allcards);
		Powerrating playerRating = cardset.evaluate();
		
		boolean tie=false;
		
		for(int i=0;i<(NumPlayers-1);i++) {
			allcards = new ArrayList<Card>();
			allcards.addAll(OtherPlayerCards.subList(i, i+2));
			allcards.addAll(CommonCards);
			cardset = new CardSet(allcards);
	
			Powerrating rating = cardset.evaluate();
			
			int compareResult = rating.compareTo(playerRating);
			if(compareResult > 0) return -1;
			else if(compareResult == 0) tie = true;	
		}
		
		if(tie==true) return 0;
		else return 1;
	}
}
