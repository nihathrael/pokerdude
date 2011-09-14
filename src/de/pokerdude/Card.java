package de.pokerdude;

public class Card implements Comparable<Card> {
	
	private final Suite suite;
	private final int value;
	
	public Card(Suite suite, int value) {
		this.suite = suite;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public Suite getSuite() {
		return suite;
	}
	
	@Override
	public String toString() {
		return "["+value+" of "+suite+"]";
	}

	@Override
	public int compareTo(Card o) {
		if(value == o.value) {
			return this.suite.compareTo(o.suite);
		}
		return ((Integer)value).compareTo(o.value);
	}

}