import java.util.ArrayList;

/**
 * In Baker's Dozen, an Ace is the LOWEST valued card in each suit.
		
	13 Tableau Piles
		
	Initial setup
	When the game begins each tableau pile should be dealt 4 cards. After dealing is completed, any Kings should be moved to the bottom of their tableau.

	Removing a Card
	Only the card which is currently at the top of the tableau pile can be removed. Once a card is removed, the card following it in the pile becomes the top card and can be removed.

	Adding a Card
	A card can be added to a tableau pile when its value is one less than the tableau's top card (suits do not matter for this). 
	For example, it is legal to move a Queen onto a King, a 6 onto a 7, or an Ace onto a 2, but illegal to move a 4 onto a 6 or a Jack onto a 10. 
	The added card becomes the tableau's new top card. Cards cannot be added to an empty tableau.

	4 Homecell Piles
	Initial setup
	When the game begins each homecell pile should be empty.

	Removing a Card
	Cards cannot be removed from a homecell pile

	Adding a Card
	A card can be added to a homecell pile if it has the identical suit and a value one more than the homecell's top card. For example, the Queen of Spades can only be added to a homecell with the Jack of Spades as its top card. The added card becomes the homecell's new top card. Only the Aces can be added to an empty homecell.

	0 Freecell Piles
	Baker's dozen does not use any freecell piles.
 **/

/** 
 * A class representing the solitaire game, Baker's Dozen, and all of its rules.
 * @author Group MyKneeHertz
 */
public class BakersDozen {
	/** constant value for the number of tableau piles in Baker's Dozen */
	private final static int NUMBER_OF_TABLEAU_PILES = 13;
	
	/** Instance of a Deck of Cards */
	private Deck deck;
	
	/** An arrayList of Tableau objects */
	private ArrayList<Tableau> tableauPiles;
	
	/** An arrayList of Homecell objects */
	private ArrayList<Homecell> homecellPiles;
	
	/** An arrayList of Freecell objects */
	private ArrayList<Freecell> freecellPiles; 
	
	/** 
	 * Instantiates a new deck & shuffles the deck. Also instantiates the 13 tableau piles, dealing 4 cards to each pile. 
	 * Instantiates 4 empty Homecell objects.
	 * organizeCards() is called to move Kings to the bottom of each tableau pile.
	 * 
	*/
	public BakersDozen() {
		deck = new Deck();												// create new deck
		deck.shuffleDeck();												// shuffles the deck
		tableauPiles = new ArrayList<>();								// create the ArrayList to hold each tableau pile
		homecellPiles = new ArrayList<>();
		freecellPiles = new ArrayList<>();
		
		for(int i = 0; i < NUMBER_OF_TABLEAU_PILES; i++) {
				tableauPiles.add(new Tableau()); 						// create 13 piles 
				for(int j = 0; j < 4; j++) 								// for loop to add 4 cards to each pile
					tableauPiles.get(i).add(deck.deal());				// add the card to the pile
		}
		for(int i = 0; i < 4; i++) 										// creates 4 empty homecell piles
			homecellPiles.add(new Homecell());
		
		organizeCards();												// move Kings to bottoms of all piles
	} // end constructor
	
	/** organizeCards places the Kings of each tableau pile to the bottom of their respective piles (bottom = index 0) */
	public void organizeCards() {
		for(int i = 0; i < NUMBER_OF_TABLEAU_PILES-1; i++) {				// for loop for each pile
			for(int j = 0; j < 4; j++) {									// for loop to go through the individual piles
				if(tableauPiles.get(i).getCard(j).getRank() == Card.KING) {
					tableauPiles.get(i).swap(0, j);
					if(tableauPiles.get(i).getCard(j).getRank() == tableauPiles.get(i).getCard(0).getRank()) {		// Is there already a King at the bottom?
						if(tableauPiles.get(i).getCard(j).getSuit() > tableauPiles.get(i).getCard(0).getSuit()) {	// yes: compare the suits
							tableauPiles.get(i).swap(0, 1);
							tableauPiles.get(i).swap(0, j);
						} else {
							tableauPiles.get(i).swap(1, j);
						}
					}
				}	
			}
		}			
	} // end organizeCards
	
	/** @return the instance of the Deck of Cards */
	public Deck getDeck() { return deck; }
	
	/** 
	 * @param index
	 * 		the index of the tableau pile you want to access
	 * @return the size of the specified tableau pile 
	 */
	public int getIndivTableauSize(int index) { return tableauPiles.get(index).getTableauSize(); }
	
	/** 
	 * @param index
	 * 		the index of the homecell pile you want to access
	 * @return the size of the specified homecell pile 
	 */
	public int getIndivHomecellSize(int index) { return homecellPiles.get(index).getHomecellSize(); }

	/** @return the number of tableau piles in total (always 13) */
	public int getTableauPileSize() { return tableauPiles.size(); }
	
	/** @return the number of homecell piles in total (always 4) */
	public int getHomeCellPileSize( ) { return homecellPiles.size();} 
	
	/** @return the number of freecell piles in total (always 0) */
	public int getFreecellPileSize() { return freecellPiles.size(); }
	
	/** 
	 * @param pileNum
	 * 		the index of the tableau pile you want to access
	 * @return the top Card (AKA the last element) at the specified tableau pile 
	 */
	public Card getTableauTopCard(int pileNum) { return tableauPiles.get(pileNum).getTopCard(); }
	
	/** 
	 * @param pileNum
	 * 		the index of the homecell pile you want to access
	 * @return the top Card (AKA the last element) at the specified homecell pile 
	 */
	public Card getHomecellTopCard(int pileNum) { return homecellPiles.get(pileNum).getTopCard(); }
	
	/** 
	 * @param pileNum
	 * 		the index of the tableau pile you want to access
	 * @return the ArrayList of Cards of a tableau pile   */
	public ArrayList<Card> getTableauPile(int pileNum) { return tableauPiles.get(pileNum).getTableau(); }
	
	/** Remove the top card from a specified tableau pile
	 * 
	 * @param index
	 * 		index of the tableau pile you want to remove the top card from
	 * @return the card that has been removed from the tableau pile
	 */
	public boolean remove(int index) {
		return tableauPiles.get(index).removeTopCard(tableauPiles.get(index).getTopCardIndex());
	} // end remove
	
	/** adds card to a pile if the pile is not empty & the card is one less than the top card of the pile 
	 * 
	 * @param c
	 * 		the Card that is going to be added to the tableau pile
	 * @param index
	 * 		the index of the tableau pile you want to add the card to
	 * @return returns true if the add was successful. If not, the method returns false
	 */
	public boolean addtoTableauPile(Card c, int index) {
		if(!tableauPiles.get(index).isEmpty() && c.getRank() == tableauPiles.get(index).getTopCard().getRank() - 1) {
			tableauPiles.get(index).add(c);
			return true;
		} else {
			return false;
		}
	} // end add
	
	/**
	 * Add a Card to the ArrayList for the homecell piles
	 * @param c
	 * 		the card that is going to be added to the correct homecell pile 
	 * @return returns true if the add was successful. If not, the method returns false
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
	 * Removes a card from the homecell pile. However, removal is not actually possible, so this method always returns false.
	 * @param c
	 * 		The card that you want to remove
	 * @param pileNum
	 * 		The index of the homecell pile that you want to remove a card from
	 * @return returns false because you cannot remove a card from the homecell pile.
	 */
	public boolean removeHomecellCard(Card c, int pileNum) {
		return homecellPiles.get(pileNum).remove(c, pileNum);
	}
	
} // end BakersDozen class
