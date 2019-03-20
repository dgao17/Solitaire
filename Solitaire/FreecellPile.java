import java.util.ArrayList;

/**
 * A class representing a single freecell pile. Freecell pile is an ArrayList of cards. 
 * @author Group MyKneeHertz
 *
 */
public class FreecellPile {
	
	/** ArrayList of type Card for the Freecell pile*/
	private ArrayList<Card> freePile;
	
	/** Instantiate the ArrayList */
	public FreecellPile() {
		freePile = new ArrayList<>();
	}
	
	/**
	 * Removes a Card from the Freecell pile
	 * @return the Card that has been removed. If there is no card to remove, this method returns null. 
	 */
	public Card remove() {
		if(!isEmpty()) {
			Card removed = freePile.remove(0);
			return removed;
		} else {
			return null;
		}
	}
	
	/**
	 * Adds a card to a Freecell pile. The pile must be empty. 
	 * @param toBeAdded
	 * 		the Card that you want to add to the pile
	 * @return true if the add was successful. If not, the method returns false.
	 */
	public boolean addToFreePile(Card toBeAdded) {
		if(isEmpty()) {
			freePile.add(toBeAdded);
			return true;
		} else {
			return false;
		}
	}
	
	/** @return true if the ArrayList is empty */
	public boolean isEmpty() { return freePile.size() == 0; }

	/**
	 * Retrieves the number of cards on the pile (should be 0 or 1 only)
	 * @return the number of cards on the pile
	 */
	public int getFreecellSize() { return freePile.size();	}
	
	public Card getTopCard() { return freePile.get(0); }
	
}