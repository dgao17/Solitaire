import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Homecell piles in Freecell initially hold 0 cards [4 points]
 * Homecell piles in Baker's Dozen initially hold 0 cards [4 points]
 * Freecell homecell pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Baker's Dozen homecell pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Freecell homecell pile correctly returns if removing top card is legal or illegal (e.g., always false) [4 points]
 * Baker's Dozen homecell pile correctly returns if removing top card is legal or illegal (e.g., always false) [4 points]
Adding card to Freecell homecell pile increases its number of cards and results in that card being the homecell pile's new top card [4 points]
Adding card to Baker's Dozen homecell pile increases its number of cards and results in that card being the homecell pile's new top card [4 points]

 */

/**
 * @author dianagao
 *
 */
public class HomecellPileTests {
	Freecell fgame = new Freecell();
	BakersDozen bgame = new BakersDozen();
	Card AceDiamonds = new Card(1,1);				// Ace of Diamonds
	Card TwoClubs = new Card(2,2);			// 2 of Clubs
	Card TwoDiamonds = new Card(1,2);		// 2 of Diamonds
	Card FourDiamonds = new Card(1,4);		// 4 of Diamonds
	
	// Homecell piles in Freecell & Baker's Dozen initially hold 0 cards
	@Test
	public void testHomecellInitialSize() {	
		for(int i = 0; i < 4; i++) {
			assertEquals("Homecell piles in Freecell should initially be empty",0,fgame.getIndivHomecellSize(i));
			assertEquals("Homecell piles in Baker's Dozen should initially be empty",0,bgame.getIndivHomecellSize(i));
		}
	}
	
	// Freecell homecell pile correctly determines if adding a specific card is legal or illegal
	@Test
	public void testFreecellAddLegality() {
		assertTrue("Only Aces can be added to an empty homecell pile",fgame.addtoHomecellPile(AceDiamonds));
		assertFalse("Only Aces can be added to an empty homecell pile", fgame.addtoHomecellPile(TwoClubs));
		assertTrue("Only cards that have an identical suit and a rank that's one bigger than the top card can be added", fgame.addtoHomecellPile(TwoDiamonds));
		assertFalse("Only cards that have an identical suit and a rank that's one bigger than the top card can be added", fgame.addtoHomecellPile(FourDiamonds));
	}
	
	// Baker's Dozen homecell pile correctly determines if adding a specific card is legal or illegal
	@Test
	public void testBakersDozenAddLegality() {
		assertTrue("Only Aces can be added to an empty homecell pile", bgame.addtoHomecellPile(AceDiamonds));
		assertFalse("Only Aces can be added to an empty homecell pile", bgame.addtoHomecellPile(TwoClubs));
		assertTrue("Only cards that have an identical suit and a rank that's one bigger than the top card can be added", bgame.addtoHomecellPile(TwoDiamonds));
		assertFalse("Only cards that have an identical suit and a rank that's one bigger than the top card can be added", bgame.addtoHomecellPile(FourDiamonds));
	}
	
	// Freecell & Baker's Dozen homecell pile correctly returns if removing top card is legal or illegal (e.g., always false)
	@Test
	public void testRemove() {
		assertFalse("No cards can be removed from the homecell piles once added", fgame.removeHomecellCard(TwoDiamonds, 0));
		assertFalse("No cards can be removed from the homecell piles once added", bgame.removeHomecellCard(TwoDiamonds, 0));
	}
	
	// Adding card to Freecell homecell pile increases its number of cards and results in that card being the homecell pile's new top card
	@Test
	public void testFreecellAddResults () {
		Card threeDiamonds = new Card(1, 3);
		fgame.addtoHomecellPile(AceDiamonds);
		fgame.addtoHomecellPile(TwoDiamonds);
		fgame.addtoHomecellPile(threeDiamonds);
		assertEquals("Number of cards on the homecell pile should increase by 1 after adding a card",3,fgame.getIndivHomecellSize(0));
		assertEquals("Top card should be updated after addition", threeDiamonds, fgame.getHomecellTopCard(0));
	}
	
	// Adding card to Baker's Dozen homecell pile increases its number of cards and results in that card being the homecell pile's new top card
		@Test
		public void testBakersDozenAddResults () {
			Card threeDiamonds = new Card(1, 3);
			bgame.addtoHomecellPile(AceDiamonds);
			bgame.addtoHomecellPile(TwoDiamonds);
			bgame.addtoHomecellPile(threeDiamonds);
			assertEquals("Number of cards on the homecell pile should increase by 1 after adding a card",3,bgame.getIndivHomecellSize(0));
			assertEquals("Top card should be updated after addition", threeDiamonds, bgame.getHomecellTopCard(0));
		}

}
