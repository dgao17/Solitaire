import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Defines class representing a Baker's Dozen game. Each game instance has 13 tableau piles, 4 homecell piles, and 0 freecell piles 
 */
public class BakersDozenTest {
	BakersDozen tester = new BakersDozen();
	
	@Test
	public void testBakersDozen() {
		assertEquals("There should be 13 tableau piles", 13, tester.getTableauPileSize());
		}
	
	@Test
	public void testBakersDozenTableauInitial() {
		assertEquals("There should be 4 homecell piles", 4, tester.getHomeCellPileSize());
	}
	
	@Test
	public void testBakersDozenZeroFreecell() {
		assertEquals("Baker's Dozen does not have any freecell piles", 0, tester.getFreecellPileSize());
	}
}
