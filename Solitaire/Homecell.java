import java.util.ArrayList;

/**
 * A class representing the Homecell piles. Homecell pile is an ArrayList of cards.
 * @author Group MyKneeHertz
 *
 */
public class Homecell {
	/** constant value for the max number of cards a homecell pile can have */
	private final static int HOMECELL_PILE_SIZE = 13;	
	
	/** ArrayList of Cards for the homecell piles */
	private ArrayList<Card> homePiles;
	
	/** Index of the top card on a homecell pile*/
	private int topCardIndex;
	
	/**
	 * Instantiate the ArrayList of Cards for the homecell piles.
	 * Sets the index of the top card to -1.
	 */
	public Homecell() {
			homePiles = new ArrayList<>();
			topCardIndex = -1;
	}
	
	/**
	 * Adds a card to the homecell pile. The first card added to an empty homecell pile must be an Ace.
	 * Following cards must be of the same suit and exactly one rank higher. 
	 * @param c
	 * 		the Card to be added to the homecell pile
	 * @param suit
	 * 		integer value of the suit to figure out which homecell pile to add the card to
	 * @return true if the add was successful. If not, the method returns false
	 */
	public boolean add(Card c, int suit) {
		boolean added = false;
		while(homePiles.size() != HOMECELL_PILE_SIZE) {
			if(isEmpty() && c.getRank() == Card.ACE && c.getSuit() == suit) {		// if homecell pile is empty & the card is an Ace with the correct suit, add onto pile
				homePiles.add(c);
				topCardIndex++;
				return added = true;
			} else if(isEmpty()) {
				return added;
			} else if(c.getSuit() == suit && c.getRank() == homePiles.get(topCardIndex).getRank()+1) {
				homePiles.add(c);
				topCardIndex++;
				return added = true;
			} else {
				return added;
			}
		}
		return added;
	}
	
	/**
	 * Add method for Aces Up game.
	 * @param card
	 * @return
	 */
	public boolean addToAcesTableau(Card card) {
		if(homePiles.add(card)) {
			topCardIndex++;
			return true;
			
		} else 
			return false;
	}
	
	/** 
	 * Removes the card from the homecell pile. However, removal is not possible, so this method always returns false.
	 * @param c
	 * 		the Card that is going to be removed
	 * @param pileNum
	 * 		the index of the homecell pile you want to remove the card from
	 * @return always returns false since you cannot remove a card from the homecell pile
	 */
	public boolean remove(Card c, int pileNum) { return false; }
	
	/** @return the index of the top card on a homecell pile */
	public int getTopCardIndex() { return topCardIndex; }
	
	/**
	 * Retrieves the top Card of the homecell pile. The top card is the last element of the ArrayList
	 * @return the top Card (AKA last element) of a homecell pile
	 */
	public Card getTopCard() {
		if(topCardIndex != -1)
			return homePiles.get(getTopCardIndex());
		else
			return null;
	}
	
	/** @return true if the ArrayList's size is 0 (AKA empty). If not, the method returns false */
	public boolean isEmpty() { return homePiles.size() == 0; }
	
	/** @return the number of cards on the homecell pile */
	public int getHomecellSize() { return homePiles.size(); }
	
	public ArrayList<Card> getIndivHomecell() { return homePiles; }
}
