import java.util.ArrayList;

/**
In Freecell, an Ace is the LOWEST valued card in each suit.
8 Tableau Piles
Initial setup
Half of the tableau piles will be dealt 7 cards and the other half of the tableau piles will be dealt 6 cards. No cards need special handling.
Removing a Card
Only the card which is currently at the top of the tableau pile can be removed. Once a card is removed, the card following it in the pile becomes the top card and can be removed.
Adding a Card
A card can be added to a tableau pile when its value is one less than the tableau's top card AND its suit is the opposite of the top card's suit. For example, it is legal to move a red Queen onto a black King, a black 6 onto a red 7, or an black Ace onto a red 2, but illegal to move a black 4 onto a red 6, a black Jack onto a red 10, or a red 3 onto a red 4. The added card becomes the tableau's new top card. ANY card CAN be added to an empty tableau.

4 Homecell Piles
Initial setup
When the game begins each homecell pile should be empty.
Removing a Card
Cards cannot be removed from a homecell pile
Adding a Card
A card can be added to a homecell pile if it has the identical suit and a value one more than the homecell's top card. For example, the Queen of Spades can only be added to a homecell with the Jack of Spades as its top card. The added card becomes the homecell's new top card. Only the Aces can be added to an empty homecell.

4 Freecell Piles
Initial setup
When the game begins each freecell pile should be empty.
Removing a Card
Cards can always be removed from a freecell pile
Adding a Card
Any card can be added to an EMPTY freecell pile. A card cannot be added to a freecell pile that already has a card.
 *
 *
 */

/** 
 * A class representing the solitaire game, Freecell, and all of its rules.
 * @author Group MyKneeHertz
 */
public class Freecell {
	/** Instance of a Deck of Cards */
	private Deck deck;
	
	/** ArrayList of tableau objects for the tableau piles */
	private ArrayList<Tableau> tableauPiles;
	
	/** ArrayList of homecell objects for the homecell piles */
	private ArrayList<Homecell> homecellPiles;
	
	/** ArrayList of FreecellPile objects for the freecell piles */
	private ArrayList<FreecellPile> freecellPiles;
	
	/**
	 * Creates a new deck and shuffles it.
	 * Instantiates the tableau, homecell, and freecell ArrayLists.
	 * Deals 7 cards to half the tableau piles and 6 to the other half.
	 * Creates 4 empty homecell piles.
	 */
	public Freecell() {

	} // end constructor
	
	public void startGame() {
		deck = new Deck();
		deck.shuffleDeck();
		tableauPiles = new ArrayList<>();								
		homecellPiles = new ArrayList<>();
		freecellPiles = new ArrayList<>();
		
		for(int pile = 0; pile < 8; pile++) {			// for loop for 8 tableau piles
			if(pile < 4) {								
				tableauPiles.add(new Tableau());		// deal 7 cards to first 4 piles
				for(int j = 0; j < 7; j++) {							
					tableauPiles.get(pile).add(deck.deal());				
				}
			} else {
				tableauPiles.add(new Tableau());	// deal 6 cards to the remaining 4 piles
				for(int k = 0; k < 6; k++) {							
					tableauPiles.get(pile).add(deck.deal());
				}
			}
		}
		
		for(int i = 0; i < 4; i++) { 										// creates 4 empty homecell piles
			homecellPiles.add(new Homecell());
			freecellPiles.add(new FreecellPile());							// creates 4 empty freecell piles
		}	
	}
	
	/** @return the number of tableau piles */
	public int getTableauSize() { return tableauPiles.size(); }
	
	/** @return the number of homecell piles */
	public int getHomecellSize() { return homecellPiles.size(); }
	
	/** @return the number of freecell piles */
	public int getFreecellSize() { return freecellPiles.size(); }
	
	public ArrayList<Tableau> getTableau() { return tableauPiles; }
	public ArrayList<Homecell> getHomecell() { return homecellPiles; }
	public ArrayList<Card> getIndivHomecell(int index) { return homecellPiles.get(index).getIndivHomecell(); }
	/** 
	 * Returns a reference to a specified tableau pile
	 * 
	 * @param pileNum
	 * 		Index of the tableau pile you want to reference
	 * @return reference to the specified tableau pile
	 */
	public ArrayList<Card> getTableauPile(int pileNum) { return tableauPiles.get(pileNum).getTableau(); }
	
	/** 
	 * @param index
	 * 		Index of the tableau pile 
	 * @return the number of cards in a tableau pile
	 */
	public int getIndivTableauSize(int index) { return tableauPiles.get(index).getTableauSize(); }
	
	/** 
	 * @param index
	 * 		Index of the homecell pile 
	 * @return the number of cards in a homecell pile
	 */
	public int getIndivHomecellSize(int index) { return homecellPiles.get(index).getHomecellSize(); }
	
	/** 
	 * Returns the number of cards in a freecell pile. This should always be 0 or 1. 
	 * @param index
	 * 		Index of the freecell pile 
	 * @return the number of cards in a freecell pile
	 */
	public int getIndivFreecellSize(int index) { return freecellPiles.get(index).getFreecellSize(); }
	
	/**
	 * Returns the deck
	 * @return the deck
	 */
	public Deck getDeck() { return deck; }
	
	/**
	 * Retrieves the top card from a specified tableau pile
	 * @param pileNum
	 * 		Index of the tableau pile
	 * @return Top Card from the specified tableau pile
	 */
	public Card getTableauTopCard(int pileNum) { return tableauPiles.get(pileNum).getTopCard(); }

	/**
	 * Retrieves the top card from a specified homecell pile
	 * @param pileNum
	 * 		Index of the homecell pile 
	 * @return the top card of the pile
	 */
	public Card getHomecellTopCard(int pileNum) { return homecellPiles.get(pileNum).getTopCard(); }
	
	// remove top card from a pile 
	// @param index of the tableau pile you want to remove the top card from
	public boolean removeFromTableauPile(int index) {
		if(!tableauPiles.get(index).isEmpty())
			return tableauPiles.get(index).removeTopCard(tableauPiles.get(index).getTopCardIndex());
		else
			return false;
	} // end remove
	
	/**
	 * Removes a card from the freecell pile. 
	 * @param pileNum
	 * 		Index of the freecell pile that you want to remove a card from.
	 * @return the Card that you removed from the freecell pile.
	 */
	public Card removeFromFreecellPile(int pileNum) {
		return freecellPiles.get(pileNum).remove();
	}
	
	/**
	 * Removes a card from the homecell pile. However, you should not be able to remove the card.
	 * Method will always return false in this case.
	 * @param c
	 * 		Card that you want to remove
	 * @param pileNum
	 * 		Index of the homecell pile you want to remove from
	 * @return always returns false because you cannot remove a card from the homecell pile
	 */
	public boolean removeHomecellCard(Card c, int pileNum) {
		return homecellPiles.get(pileNum).remove(c, pileNum);
	}
	
	/**
	 * Adds a card to a specified tableau pile. 
	 * Only cards that have a rank one less than the top card on that pile can be added.
	 * @param c
	 * 		Card to be added to the tableau pile.
	 * @param index
	 * 		Index of the tableau pile that you want to add the card to
	 * @return true if the add was successful. If not, the method returns false.
	 */
	public boolean addtoTableauPile(Card c, int index) {
		if(!tableauPiles.get(index).isEmpty() && c.getRank() == tableauPiles.get(index).getTopCard().getRank() - 1) {
			if(c.getSuit() % 2 == 0 && tableauPiles.get(index).getTopCard().getSuit() % 2 != 0) {
				tableauPiles.get(index).add(c);
				return true;
			} else if(c.getSuit() % 2 != 0 && tableauPiles.get(index).getTopCard().getSuit() % 2 == 0) {
				tableauPiles.get(index).add(c);
				return true;
			} 
		} 
		
		return false;
	} // end add
	
	/**
	 * Adds a card to the correct homecell pile depending on its suit. Cards must be added by rank order.
	 * @param c
	 * 		Card to be added to the homecell pile.
	 * @param index
	 * 		Index of the homecell pile you want to add to
	 * @return true if the add was successful. If not, the method returns false.
	 */
	public boolean addtoHomecellPile(Card c) {
		boolean added = false;
		for(int suitPile = 1; suitPile <= 4; suitPile++) {
			added = homecellPiles.get(suitPile-1).add(c, suitPile);
			if(added)
				return added;
		}
		return added;
	}
	
	/**
	 * Adds a card to an empty freecell pile.
	 * @param index
	 * 		index of freecell pile that you want to add the card to
	 * @param toBeAdded
	 * 		Card that you are going to add to the specified freecell pile
	 * @return true if the add was successful. If not, the method returns false.
	 */
	public boolean addtoFreecellPile(int index,Card toBeAdded) { return freecellPiles.get(index).addToFreePile(toBeAdded); }
	
	public Card getFreecellTopCard(int pileNum) { return freecellPiles.get(pileNum).getTopCard(); }
}
