import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tableau pile tests: [40 points]
 * Tableau piles in Freecell initially hold 6 or 7 cards [4 points]
 * Tableau piles in Baker's Dozen initially hold 4 cards [4 points]
 * Freecell tableau pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Baker's Dozen tableau pile correctly determines if adding a specific card is legal or illegal [4 points]
 * Freecell tableau pile correctly returns if removing top card is legal or illegal (e.g., if the tableau pile is NOT empty) [4 points]
 * Baker's Dozen tableau pile correctly returns if removing top card is legal or illegal (e.g., if the tableau pile is NOT empty) [4 points]
 * Adding card to Freecell tableau pile increases its number of cards and results in that card being the tableau pile's new top card [4 points]
 * Adding card to Baker's Dozen tableau pile increases its number of cards and results in that card being the tableau pile's new top card [4 points]
 * Removing card from Freecell tableau pile decreases its number of cards and results in following card being the new top card [4 points]
 * Removing card from Baker's Dozen tableau pile decreases its number of cards and results in following card being the new top card [4 points]
 * @author dianagao
 *
 */
public class TableauTest {
	
	// Tableau piles in Freecell initially hold 6 or 7 cards
	@Test
	public void testInitialFreecell() {
		Freecell game = new Freecell();
		for(int i = 0; i < 8; i++) {
			if(game.getIndivTableauSize(i) == 6)
				assertEquals("Tableau piles in Freecell should start with 6 or 7 cards", 6 , game.getIndivTableauSize(i));
			if(game.getIndivTableauSize(i) == 7)
				assertEquals("Tableau piles in Freecell should start with 6 or 7 cards", 7 , game.getIndivTableauSize(i));
		}
	}
	
	// Tableau piles in Baker's Dozen initially hold 4 cards
	@Test
	public void testInitialBakersDozen() {
		BakersDozen game = new BakersDozen();
		for(int index = 0; index < game.getTableauPileSize(); index++)
			assertEquals("Tableau piles in Baker's Dozen should start with 4", 4 , game.getIndivTableauSize(index));
	}
	
	// Freecell tableau pile correctly determines if adding a specific card is legal or illegal
	// Adding card to Freecell tableau pile increases its number of cards and results in that card being the tableau pile's new top card
	@Test
	public void testFreecellAdd() {
		Freecell game = new Freecell();
		Card topCard = game.getTableauTopCard(0);			// top card of pile 0 
		Card newCard = new Card(topCard.getSuit(), topCard.getRank());
		int topCardSuit = topCard.getSuit();
		if(topCardSuit != 4)					
			newCard.setSuit(topCardSuit+1);
		if(topCardSuit == 4)
			newCard.setSuit(topCardSuit-1);
		int topCardRank = topCard.getRank();
		if(topCardRank == 1) {
			topCard.setRank(2);
			newCard.setRank(topCard.getRank()-1);
		}
		
		newCard.setRank(topCardRank-1);
		assertTrue("Freecell tableau pile correctly determines if adding a specific card is legal or illegal", game.addtoTableauPile(newCard, 0));
		assertEquals("Adding card to Freecell tableau pile increases its number of cards by 1", 8, game.getIndivTableauSize(0));
		assertEquals("Adding card to Freecell tableau pile results in that card being the tableau pile's new top card",newCard, game.getTableauTopCard(0));
		
	}
	
	// Baker's Dozen tableau pile correctly determines if adding a specific card is legal or illegal
	// Adding card to Baker's Dozen tableau pile increases its number of cards and results in that card being the tableau pile's new top card
	@Test
	public void testBDAdd() {
		BakersDozen game = new BakersDozen();
		Card topCard = game.getTableauTopCard(0);
		Card newCard = new Card(1, topCard.getRank());
		if(topCard.getRank() == 1) {
			topCard.setRank(2);
			newCard.setRank(topCard.getRank()-1);
		}
		newCard.setRank(topCard.getRank()-1);
		assertTrue("Baker's Dozen tableau pile correctly determines if adding a specific card is legal or illegal",game.addtoTableauPile(newCard, 0));
		assertEquals("Size of tableau pile should increase by one after you add a card",5,game.getIndivTableauSize(0));
		assertEquals("Tableau pile should update the top card after adding a new card",newCard, game.getTableauTopCard(0));
	}
	
	
	// Freecell tableau pile correctly returns if removing top card is legal or illegal (e.g., if the tableau pile is NOT empty) 
	// Removing card from Freecell tableau pile decreases its number of cards and results in following card being the new top card
	@Test
	public void testFreecellRemove() {
		Freecell game = new Freecell();	
		ArrayList<Card> copy = new ArrayList<>();
		copy.addAll(game.getTableauPile(0));
		game.removeFromTableauPile(0);
		assertEquals("Number of cards in the tableau pile should have decreased by one after each removal",6,game.getIndivTableauSize(0));
		assertEquals("After removing the top card, the following card becomes the top card",copy.get(5),game.getTableauTopCard(0));
		
		for(int i = 0; i < 6; i++) 
			game.removeFromTableauPile(0);

		assertEquals("Remove should return null if there are no more cards on the tableau pile",null,game.getTableauTopCard(0));
	}
	
	// Baker's Dozen tableau pile correctly returns if removing top card is legal or illegal (e.g., if the tableau pile is NOT empty)
	// Removing card from Baker's Dozen tableau pile decreases its number of cards and results in following card being the new top card
	@Test
	public void testBDRemove() {
		BakersDozen game = new BakersDozen();
		ArrayList<Card> copy = new ArrayList<>();
		copy.addAll(game.getTableauPile(6));
		game.remove(6);
		assertEquals("Number of cards in the tableau pile should have decreased by one after each removal",3,game.getIndivTableauSize(6));
		assertEquals("After removing the top card, the following card becomes the top card",copy.get(2),game.getTableauTopCard(6));
		
		for(int i = 0; i < 3; i++) 
			game.remove(6);

		assertEquals("Remove should return null if there are no more cards on the tableau pile",null,game.getTableauTopCard(6));
	}
	
	
}

