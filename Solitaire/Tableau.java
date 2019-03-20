import java.util.ArrayList;
import java.util.Collections;

/**
 * A class representing the Tableau piles. Tableau pile is an ArrayList of cards.
 * @author Group MyKneeHertz
 *
 */

public class Tableau {
	/** ArrayList of Cards for the tableau piles */
	private ArrayList<Card> tableauPile;
	
	/** Index of the top card on a tableau pile*/
	private int topCardIndex; 	
	
	/**
	 * Instantiate the ArrayList of Cards for the tableau piles.
	 * Sets the index of the top card to the size of the tableau minus 1.
	 */
	public Tableau() {
		tableauPile = new ArrayList<>();
		topCardIndex = tableauPile.size() - 1;

	}
	
	/** 
	 * Adds a card to the bottom of the tableau pile (bottom = index 0) 
	 * Increments the topCardIndex by 1
	 */
	public void addtoTableauBottom(Card c) {
		tableauPile.add(0, c);
		topCardIndex++;
	}
	
	/**
	 * Adds card to the tableau pile
	 * @param c
	 * 		Card that is going to be added to the tableau pile
	 */
	public void add(Card c) {
		tableauPile.add(c);
		topCardIndex++;
	}
	
	/**
	 * Retrieves a specific card in the tableau pile
	 * @param index
	 * 		Index of the Card in the ArrayList
	 * @return the specified card in the tableau pile
	 */
	public Card getCard(int index) {
		return tableauPile.get(index);
	}
	
	/** @return Index of the top card of the tableau pile */
	public int getTopCardIndex() { return topCardIndex; }
	
	/**
	 * Retrieves the top card of the tableau pile (AKA last element in the ArrayList) as long as the tableau pile is not empty
	 * @return Top Card of the tableau pile
	 */
	public Card getTopCard() {
		if(topCardIndex != -1)
			return tableauPile.get(tableauPile.size()-1);
		else
			return null;
	}
	
	/** @return the number of cards on the tableau pile */
	public int getTableauSize() { return tableauPile.size(); }
	
	public ArrayList<Card> getTableau() { return tableauPile; }
	
	public void swap(int i, int j) {
		Collections.swap(tableauPile, i, j);
	}
	
	public boolean removeTopCard(int index) {
		if(!isEmpty()) {
			topCardIndex--;							// decrements topCard, so that when removal is complete, top card becomes the card behind the original top card
			tableauPile.remove(index);
			return true;
		}
		return false;
	}
	
	/** @return true if the ArrayList's size is 0 (AKA empty). If not, the method returns false */
	public boolean isEmpty() {
		return tableauPile.size() == 0;
	}
}
