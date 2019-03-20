import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Freecell pile tests: [8 points]
 * Freecell piles in Freecell begin holding 0 cards [2 points]
 * Freecell freecell pile correctly returns if adding a specific card is legal or illegal (e.g., if the freecell pile is empty) [2 points]
 * Freecell freecell pile correctly returns if removing top card is legal or illegal (e.g., if the freecell pile is NOT empty) [2 points]
 * Adding card to Freecell freecell pile increases its number of cards and results in that card being the freecell pile's new top card [2 points]
 * @author dianagao
 *
 */
public class FreecellPileTests {
	Freecell game = new Freecell();
	Card ThreeClubs = new Card(2, 3);
	Card FiveSpades = new Card(4, 5);
	// Freecell piles in Freecell begin holding 0 cards
	@Test
	public void testFreecellInitialSize() {
		for(int i = 0; i < 4; i++) 
			assertEquals("Freecell piles begin with 0 cards", 0, game.getIndivFreecellSize(i));
	}
	
	// Freecell freecell pile correctly returns if adding a specific card is legal or illegal (e.g., if the freecell pile is empty)
	@Test
	public void testAddLegality() {
		assertTrue("A card can only be added onto an empty freecell pile",game.addtoFreecellPile(0, ThreeClubs));
		assertFalse("A card can only be added onto an empty freecell pile",game.addtoFreecellPile(0, FiveSpades));
	}
	
	// Freecell freecell pile correctly returns if removing top card is legal or illegal (e.g., if the freecell pile is NOT empty)
	public void testRemove() {
		assertTrue("You can only remove a card from a non-empty freecell pile", game.removeHomecellCard(ThreeClubs, 0));
		assertFalse("You can only remove a card from a non-empty freecell pile", game.removeHomecellCard(ThreeClubs, 1));
	}
	
	// Adding card to Freecell freecell pile increases its number of cards and results in that card being the freecell pile's new top card
	public void testAddResults() {
		game.addtoFreecellPile(2, ThreeClubs);
		assertEquals("Size of freecell pile should be 1 after adding a card to it", 1, game.getIndivFreecellSize(2));
		assertEquals("Freecell pile's top card should've been updated after adding a card to it", ThreeClubs, game.getHomecellTopCard(2));
	}
}
