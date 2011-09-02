package de.pokerdude;

public class Powerrating implements Comparable<Powerrating> {

	private final int[] rating;
	
	public Powerrating(int[] rating) {
		this.rating = rating;
	}
	
	@Override
	public int compareTo(Powerrating other) {
		int i=0;
		while(i<rating.length && rating[i] == other.rating[0]) {
			i++;
		}
		if(i == rating.length) {
			return 0; // They are equal
		} else if (rating[i] < other.rating[i]) {
			return -1; // The other is bigger
		} else {
			return 1; // This is bigger
		}
	}
	
	
}
