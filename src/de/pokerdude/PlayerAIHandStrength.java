package de.pokerdude;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class PlayerAIHandStrength extends Player {
	
	static final Logger logger = Logger.getLogger(PokerDude.class);
	private PokerGame game;
	RolloutSimulation RS = new RolloutSimulation();
	
	public PlayerAIHandStrength(String name, PokerGame game) {
		super(name);
		this.game = game;
		logger.setLevel(PokerDude.DEBUGLEVEL);
	}
	
	@Override
	public int getBetPreFlop() {
		double prop = RS.GetPropabilityFromList(Cards, game.getPlayers().size());
		if(prop < 0.3) {
			return 0;
		}
		int bet = (int)(30 * prop);
		logger.debug(name + " betting " + bet);
		return bet;
	}
	
	
	@Override
	public int getBetPreTurn() {
		int bet=0;
		
		ArrayList<Card> knownCards = new ArrayList<Card>();
		knownCards.addAll(this.Cards);
		
		double handStrength = calcHandstrength(knownCards);
		
		bet = (int)(30 * handStrength);
		
		return bet;
	}
	
	@Override
	public int getBetPreRiver() {
		int bet=0;
		
		ArrayList<Card> knownCards = new ArrayList<Card>();
		knownCards.addAll(this.Cards);
		knownCards.add(game.getTurn());
		
		
		double handStrength = calcHandstrength(knownCards);

		bet = (int)(30 * handStrength);
		
		return bet;
	}
	
	private double calcHandstrength(ArrayList<Card> knownCards) {
		
		Deck deckWithoutKnownCards = new Deck(knownCards);
		Deck deckClone1 = new Deck(deckWithoutKnownCards);
		Deck deckClone2 = new Deck(deckWithoutKnownCards);
		
		int numCards = deckWithoutKnownCards.size();
		double wins=0;
		double ties=0;
		double losses=0;
		
		Card card1,card2;
		ArrayList<Card> commonCards = new ArrayList<Card>();
		if(game.getRiver() != null) commonCards.add(game.getRiver());
		if(game.getTurn() != null) commonCards.add(game.getTurn());
		commonCards.addAll(game.getFlop());
		
		for(int i=0; i<numCards;i++) {
			card1 = deckClone1.getCard();
			
			for(int j=0; j<numCards;j++) {
				card2 = deckClone2.getCard();
				if(card1.compareTo(card2) != 0) {
					ArrayList<Card> opponentCards=new ArrayList<Card>();
					ArrayList<Card> playerCards = new ArrayList<Card>();

					playerCards.addAll(this.Cards);
					playerCards.addAll(commonCards);
					opponentCards.add(card1);
					opponentCards.add(card2);
					opponentCards.addAll(commonCards);
					
					CardSet setPlayer = new CardSet(playerCards);
					CardSet setOpponent = new CardSet(opponentCards);
					
					int result = setPlayer.evaluate().compareTo(setOpponent.evaluate());
					
					if(result > 0) wins++;
					else if(result == 0) ties++;
					else if(result < 0) losses++;		
				}
				
			}
			
			deckClone2 = new Deck(deckWithoutKnownCards);
				
		}
		
		
		
		//ToDo: (!!!) only consider active players
		return Math.pow(((wins + ties/2) / (wins + ties + losses)), (double)game.getPlayers().size());
	}
	
	
}

