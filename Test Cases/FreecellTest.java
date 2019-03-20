import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Defines class representing a Freecell game. Each game instance has 8 tableau piles, 4 homecell piles, and 4 freecell piles
*/

public class FreecellTest {
		Freecell tester = new Freecell();
	
		@Test
		public void testFreecellTableauInitial() {
			assertEquals("There should be 8 tableau piles", 8, tester.getTableauSize());
		}
		
		@Test
		public void testFreecellHomecellInitial() {
			assertEquals("There should be 4 homecell piles", 4, tester.getHomecellSize());
		}
		
		@Test
		public void testFreecellFreecellInitial() {
			assertEquals("There should be 4 freecell piles", 4, tester.getFreecellSize());
		}
	}

