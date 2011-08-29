package de.pokerdude;

public class Card {
	
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

}