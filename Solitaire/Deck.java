import java.util.ArrayList;
import java.util.Collections;

/**
 * Class which implements a Deck -- an arraylist of 52 unique Card objects
 * @author Group MyKneeHertz
 *
 */
public class Deck {
	/** constant value for the total number of cards in a deck */
	private final static int NUMBER_OF_CARDS = 52;
	
	/** the number of Cards left in the deck */
	private int numCardsLeft = 0;
	
	/** ArrayList in which cards are stored */
	private ArrayList<Card> deck;
	
	/** the following Card object that is going to be dealt next */
	private Card nextCard;

	/** Initializes an ArrayList of Card objects and assigns 52 unique card instances to each element.
	 *  numCardLeft variable is set to 52.
	 */
	public Deck() {
		deck = new ArrayList<Card>();	
		
			for(int rank = 1; rank <= 13; rank++) {
				for(int suit = 1; suit <= 4; suit++) {
					deck.add(new Card(suit, rank));
				}
			}
			numCardsLeft = 52;
	}
	
	/** @return the size of the deck (# of elements in the array) */
	public int getDeckSize() {
		return deck.size();
	}
	
	/** @return the next card to be dealt in the array (the last element) */ 
	public Card getNextCard() {
			return nextCard;
	}
	
	/** @return the reference to the deck */
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	/** Shuffles the deck (randomize the elements) */
	public void shuffleDeck() {
		for(int i = 0; i < NUMBER_OF_CARDS; i++) {
			Collections.shuffle(deck);
		}
		nextCard = deck.get(deck.size()-1);
	}
	
	/** 
	 * As long as there are still Cards in the deck, then the last Card in the array will be returned.
	 * @return the next Card in the deck (the last element) 
	 */
	public Card deal() {
		Card removed = null;
		if(!deck.isEmpty()) {
			 removed = deck.remove(deck.size()-1);
			numCardsLeft--;
		}
		
		if(deck.size() > 1)
			nextCard = deck.get(deck.size()-1);

		return removed;
		
	}
	
	/**
	 * When Ace's Up is played, Aces have to be changed to the highest rank. This needs to be called prior to any shuffling. 
	 */
	public void setAceHighest() {
		for(int i = 0; i < 4; i++) {
			deck.get(i).setAceHighest();
		}
		
	}
	
	public boolean isEmpty() {
		return deck.size() == 0;
	}
	
	
		
}

