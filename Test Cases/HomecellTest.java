import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Homecell pile tests: [32 points]
 * Homecell piles in Freecell initially hold 0 cards [4 points]
 * Homecell piles in Baker's Dozen initially hold 0 cards [4 points]
 * Freecell homecell pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Baker's Dozen homecell pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Freecell homecell pile correctly returns if removing top card is legal or illegal (e.g., always false) [4 points]
 * Baker's Dozen homecell pile correctly returns if removing top card is legal or illegal (e.g., always false) [4 points]
 * Adding card to Freecell homecell pile increases its number of cards and results in that card being the homecell pile's new top card [4 points]
 * Adding card to Baker's Dozen homecell pile increases its number of cards and results in that card being the homecell pile's new top card [4 points]
 */

public class HomecellTest {

	//Homecell piles in Baker's Dozen initially hold 0 cards [4 points]
	@Test
	public void testBDInitialHomecellSize() {
		BakersDozen game = new BakersDozen();
		assertEquals("Homecell piles should be empty at the beginning of the game",0, game.getIndivHomecellSize(0));
	}
	
	//Baker's Dozen homecell pile correctly determines if adding a specific card is legal or illegal [4 points]
	@Test
	public void testBDHomecellAdd() {
		BakersDozen game = new BakersDozen();
		Card testAce = new Card(1, 1);	// add Ace of diamonds
		Card testAceClubs = new Card(2, 1); 	// add Ace of clubs
		//game.addtoHomecellPile(c);
		assertTrue("Only Aces can be added to an empty homecell pile",game.addtoHomecellPile(testAce));
		assertTrue("Only Aces (1) can be added to an empty homecell pile",game.addtoHomecellPile(testAceClubs));
	}
}
