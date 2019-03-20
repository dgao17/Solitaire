import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DeckTest {

	
	/**
	 * Defines Deck class which has 52 Card instances. 
	 * Each Card instance has unique combination of suit & rank
	 */
	@Test
	public void testProperDeck() {
		Deck deck = new Deck();
		assertEquals("Deck class should have 52 card instances", 52, deck.getDeckSize());
		}
	
	/**
	 * Tests that all the cards are different.
	 */
	@Test
	public void testCardUniqueness() {
		Deck deck = new Deck();
		int index = 0;
		while(index != 51) {
			if(deck.getDeck().get(index).getSuit() == (deck.getDeck().get(index+1).getSuit())) {
				assertNotEquals("There should not be duplicates in the deck",deck.getDeck().get(index).getRank(),deck.getDeck().get(index+1).getRank());
			}
			index++;
		}	
	}
	
	@Test
	public void testCardDuplicates() {
		Deck deck = new Deck();
		Set<Card> set = new HashSet<Card>(deck.getDeck());

		if(set.size() < deck.getDeckSize()){
		    fail("There should be no duplicate cards in a deck");
		}
	}
}

