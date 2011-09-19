package de.pokerdude;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;



public class RolloutSimulation {
	
	static final Logger logger = Logger.getLogger(RolloutSimulation.class);
	
	//List of propabilities filled by CalculatePreFlopPropabilities
	private ArrayList<PreFlopPropability> PFProps = null;
	
	public RolloutSimulation() {
		logger.setLevel(PokerDude.DEBUGLEVEL);
		
		File file = new File("PreflopPropabilities.csv");
		if(file.exists())
			try {
				ReadTable(file);
			} catch (IOException e) {

				e.printStackTrace();
			}
		else {
			System.out.println("**The table with preflop propabilities could not be found.\n" +
					"**Type either [A] to abort or a Number that specifies the amount of\n" +
					"**Rollout games for each possible Hand.");
			
			Scanner in = new Scanner(System.in);
			String input = in.nextLine();
			int R=10;
			if(input.contains("A")) System.exit(0);
			else {
				R = Integer.parseInt(input);
				CalculatePreFlopPropabilities(R);
				SaveTable(file);
			}
		}
				
	}
	
	public void ReadTable(File f) throws IOException {
	  try{
		  FileInputStream fstream = new FileInputStream(f);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;

		  PFProps = new ArrayList<PreFlopPropability>();
		  
		  while ((strLine = br.readLine()) != null)   {
			  PreFlopPropability p = new PreFlopPropability(strLine);
			  PFProps.add(p);
			  logger.debug(p.toString());
		  }
		  //Close the input stream
		  in.close();
		    }catch (Exception e){//Catch exception if any
		     e.printStackTrace();
		  }
		
	}
	
	public void SaveTable(File f) {
		
		
		try {
			f.createNewFile();

			FileWriter fstream = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fstream);
			
			for(int i=0;i<PFProps.size();i++) {
				String output = PFProps.get(i).encode()+"\n";
				out.write(output);
			}

			out.close();
		} catch (Exception e) {
				logger.error("Error: " + e.getMessage());
		}
	}
	
	public void CalculatePreFlopPropabilities(int R) {
		int numPlayers = 10;
		PFProps = new ArrayList<PreFlopPropability>();
		
		PreFlopPropability p;
		
		for(int i=2; i<15; i++) {
			for(int j=2; j<=i; j++) {
				
				//Prototype hands for all unsuited equivalence classes including pairs
				p = new PreFlopPropability(new Card(Suite.CLUBS, j), new Card(Suite.DIAMONDS, i), numPlayers);
				PFProps.add(p);
			}
		}
					
		for(int i=2; i<15; i++) {
			for(int j=2; j<=i; j++) {
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
			logger.info(prop.toString());
			
			
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
		if(PFProps == null) CalculatePreFlopPropabilities(1000);
		
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
		Deck d = new Deck(g.getDeck(), true);
		
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
		Deck d = new Deck(_d, true);	
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
