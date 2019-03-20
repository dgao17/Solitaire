import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Tableau pile tests:
 * Tableau piles in Ace's Up initially hold 1 card [2 points]
 * Ace's Up tableau pile method correctly determines if adding a specific card is legal or illegal. To receive points, your test cannot call two separate methods (e.g., one for when the card comes from the stock pile and one for when the card comes from another tableau) to perform this check. [4 points]
 * Ace's Up tableau pile method correctly determines if removing a specific card is legal or illegal. To receive points, your test cannot call two separate methods (e.g., one for when the card is being moved to the homecell pile and one for when the card is moved to another tableau pile) to perform this check. [4 points]
 * Adding card to Ace's Up tableau pile increases its number of cards and results in that card being the tableau pile's new top card [4 points]
 * Removing card from Ace's Up tableau pile decreases its number of cards and results in following card being the new top card [4 points]
 * 
 * Homecell pile tests:
 * Homecell pile in Ace's Up initially holds 0 cards [2 points]
 * Ace's Up homecell pile correctly determines if adding a specific card is legal or illegal (e.g., always legal) [2 points]
 * Ace's Up homecell pile correctly returns if removing top card is legal or illegal (e.g., always illegal) [2 points]
 * Adding card to Ace's Up homecell pile increases its number of cards and results in that card being the homecell pile's new top card [2 points]

 * Stock pile tests:
 * Ace's Up stock pile initially holds 48 cards [2 points]
 * Ace's Up stock pile correctly returns if adding a specific card is legal or illegal (e.g., always illegal) [2 points]
 * Ace's Up stock pile correctly returns if removing a specific card is legal or illegal (e.g., legal if the stock pile is not empty) [2 points]
 * Dealing cards from Ace's Up stock pile removes the top 4 cards, adds the removed cards to the tableau piles, and results in the 5th card being the new top card [2 points]
 */

public class AcesUpTest {
	AcesUp game = new AcesUp();
	
	// Tableau piles in Ace's Up initially hold 1 card 
	@Test
	public void testInitialTabSize() {
		game.initialSetUp();				// sets up the game
		
		for(int i = 0; i < game.getTableauPiles().size(); i++) {
			assertEquals("Tableau piles in Ace's Up should initially hold 1 card", 1, game.getTableauPiles().get(i).getTableauSize());
		}
	}
	
	// Ace's Up tableau pile method correctly determines if adding a specific card is legal or illegal.
	// Adding card to Ace's Up tableau pile increases its number of cards and results in that card being the tableau pile's new top card 
	@Test
	public void testAddLegal() {
		Card testee = new Card(1, 2);	// 2 of Diamonds
		Card testee2 = new Card(1, 3);	// 3 of Diamonds
		
		// adds 2 of Diamonds to an empty tableau pile
		assertTrue("Adding a card on an empty tableau pile is always legal", game.addToTableau(0, testee));
		
		// attempts to add another card to the same tableau pile (not empty now)
		assertFalse("Cannot add a card to a non-empty tableau pile", game.addToTableau(0, testee2));
		
		// tests that card was successfully added after stock pile got clicked
		game.initialSetUp();
		Card testee3 = game.getStockPile().getNextCard();	// next card in stock pile to be dealt
		game.stockGotClicked();
		game.stockClicked();
		assertEquals("Clicking on the stock pile always adds one card to each tableau pile", testee3, game.getTableauTopCard(0));
		assertEquals("Adding a card to tableau pile increases its number of cards", 2, game.getIndivTableauPile(1).size());
	}
	
	// Ace's Up tableau pile method correctly determines if removing a specific card is legal or illegal.
	// Removing card from Ace's Up tableau pile decreases its number of cards and results in following card being the new top card 
	@Test
	public void testRemoveLegal() {
		Card AcesDiamond = new Card(1,14);
		Card TwoHearts = new Card(3, 2);
		Card ThreeHearts = new Card(3, 3);
		Card FourSpades = new Card(4,4);
		
		game.addToTableau(0, AcesDiamond);		
		game.addToTableau(1, TwoHearts);
		game.addToTableau(2, ThreeHearts);
		game.addToTableau(3, FourSpades);
		assertFalse("You cannot remove a card from a tableau pile if you did not click on an empty tableau pile or the homecell" ,game.removeTopCard(0));
		game.tableauGotClicked();
		game.homeGotClicked();
		assertTrue("You should be able to remove a card if there is another card with the same suit, but higher rank on a different tableau pile", game.removeTopCard(1));
		assertEquals("The size of the tableau should decrement after removal", 0, game.getIndivTableauPile(1).size());
		assertEquals("After remove, the new top card should be updated", null, game.getTableauTopCard(1));
	}

	// Homecell pile in Ace's Up initially holds 0 cards [2 points]
	@Test
	public void testHomecellInitialSize() {
		game.initialSetUp();
		assertEquals("The homecell pile should initially hold 0 cards", 0, game.getHomecellPile().getHomecellSize());
	}
	
	// Ace's Up homecell pile correctly determines if adding a specific card is legal or illegal (e.g., always legal)
	@Test
	public void testHomecellAddLegal() {
		game.initialSetUp();
		assertTrue("You should always be able to add to the homecell pile", game.addToHomecell(new Card(3,2)));
	}
	
	// Ace's Up homecell pile correctly returns if removing top card is legal or illegal (e.g., always illegal)
	@Test
	public void testHomecellRemove() {
		game.initialSetUp();
		assertFalse("You should not be able to remove from the homecell pile ever", game.removeFromHomecell());
	}
	
	// Adding card to Ace's Up homecell pile increases its number of cards and results in that card being the homecell pile's new top card
	@Test
	public void testHomecellAdd() {
		Card topCard = new Card(1, 2);		// card to the added to the homecell pile
		game.addToHomecell(topCard);
		assertEquals("Homecell should update its top card every time a new card is added", topCard, game.getHomeTopCard());
	}

	// Ace's Up stock pile initially holds 48 cards 
	@Test
	public void testStockInitialSize() {
		game.initialSetUp();
		assertEquals("The stock pile should initially hold 48 cards", 48, game.getStockPile().getDeckSize());
	}
	
	// Ace's Up stock pile correctly returns if adding a specific card is legal or illegal (e.g., always illegal)
	@Test
	public void testAddStock() {
		assertFalse("You cannot add a card to the stock pile", game.addToStock());
	}
	
	// Ace's Up stock pile correctly returns if removing a specific card is legal or illegal (e.g., legal if the stock pile is not empty)
	@Test
	public void testRemoveStock() {
		game.initialSetUp(); 		// set up the game
		game.stockGotClicked();
		assertTrue(game.stockClicked());
		
		for(int i = 0; i < 12; i++) {
			game.stockClicked();
		}
		
		assertFalse(game.stockClicked());
	}
	
	// Dealing cards from Ace's Up stock pile removes the top 4 cards, adds the removed cards to the tableau piles, and results in the 5th card being the new top card
	@Test
	public void testStockClicked() {
		game.initialSetUp();  	
		Card[] nextCards = new Card[5];
		int cardIndex = 47;
		for(int i = 0; i < 5; i++) {
			nextCards[i] = game.getStockPile().getDeck().get(cardIndex);
			cardIndex--;
		}
		game.stockGotClicked();
		game.stockClicked();			// deals cards from stock to tableau piles
		
		for(int i = 0; i < 4; i++) {
			assertEquals("Tableau piles should add the removed cards from the stock onto its respective piles", nextCards[i], game.getTableauTopCard(i));
		}
		
		assertEquals("Clicking on the stock should remove the top 4 cards", 44, game.getStockPile().getDeckSize());
		assertEquals("Stock pile's top card should be updated after it's been clicked", nextCards[4], game.getStockTopCard());
	}
}
