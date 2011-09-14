package de.pokerdude;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CardSetTest {

	private void assertCompareToEquals(Powerrating o, Powerrating o2) {		
		assertTrue(o.compareTo(o2)==0);
	}
	
	@Test
	public void testGetHighestRating() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for(int i=2;i<=6;i+=2) {
			cards.add(new Card(Suite.CLUBS, i));
		}
		for(int i=8;i<=14;i+=2) {
			cards.add(new Card(Suite.DIAMONDS, i));
		}
		CardSet cardSet = new CardSet(cards);
		assertCompareToEquals(new Powerrating(new int[] {1, 14, 12, 10, 8}), cardSet.evaluate());
	}

	@Test
	public void testGetPairRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDoublePairRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetThreeOfAKindRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStraightRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFlushRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFullHouseRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFourOfAKindRating() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStraightFlushRating() {
		fail("Not yet implemented");
	}

}
