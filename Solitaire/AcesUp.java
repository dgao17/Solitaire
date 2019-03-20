import java.util.ArrayList;

/**
 * Ace's Up
In Ace's Up, an Ace is the HIGHEST valued card in each suit.

4 Tableau Piles
	Initial setup
		When the game begins each tableau pile should be dealt 1 card; more cards will be added as the game is played. No special handling is needed for any cards added to the tableau.

	Removing a Card
		The top card of a tableau can be removed and added to the homecell pile whenever another tableau's top card has the same suit, but with a higher rank. A top card can be removed and added to an empty tableau at any time.

	Adding a Card
		The system will add a card to the top of the tableau whenever the stock pile is clicked. A card can be added to an empty tableau pile at any time. Either way, the added card becomes the tableau's new top card.

	1 Homecell Pile
		Initial setup
			When the game begins the homecell pile should be empty.

	Removing a Card
		Cards cannot be removed from the homecell pile

	Adding a Card
		Assuming it was legally removed from a tableau pile, a card can be added to the homecell pile at any time.

	1 Stock Pile
		Initial setup
			When the game begins the stock pile will contain the entire deck except those cards dealt to the tableau piles.

	Removing Cards
		When the stock pile is selected, remove the 4 top cards and deal 1 card to each tableau pile.

	Adding a Card
		Cards cannot be added to the stock pile.

	0 Freecell Piles
		Ace's Up does not use any freecell piles.
 * @author dianagao
 *
 */
public class AcesUp {
	/**
	 * An arraylist of tableau objects that holds the 4 tableau piles. 
	 */
	private ArrayList<Tableau> tableauPiles;
	
	/**
	 * Instance of the one homecell pile.
	 */
	private Homecell homecellPile;
	
	/**
	 *  Instance of a Deck object for the stock pile.
	 */
	private Deck stockPile;
	
	int stockClicked = -1;
	int homeClicked = -1;
	int tableauClicked = -1;
	private boolean removed = false;
	
	/**
	 * Instantiates the instances of tableauPiles, homecellPile, and stockPile
	 */
	public AcesUp() {
		tableauPiles = new ArrayList<>();
		homecellPile = new Homecell();
		stockPile = new Deck();
		
		for(int i = 0; i < 4; i++) 
			tableauPiles.add(new Tableau());			// instantiate 4 separate tableau piles
	} 

	/** Getter method for the tableau piles */
	public ArrayList<Tableau> getTableauPiles() { return tableauPiles; }
	
	/** Getter method for an individual tableau piles */
	public ArrayList<Card> getIndivTableauPile(int index) { return tableauPiles.get(index).getTableau(); }

	/** Getter method for the homecell piles */
	public Homecell getHomecellPile() { return homecellPile; }

	/** Getter method for the stock pile */
	public Deck getStockPile() { return stockPile; }
	
	/**
	 * Initial set-up of Aces Up before user starts playing the game. 
	 * Simply shuffles the deck and deals 1 card to 4 tableau piles.
	 */
	public void initialSetUp() {
		stockPile.setAceHighest(); 						// changes all Aces to be the highest rank
		stockPile.shuffleDeck();						// shuffle the deck first
		
		for(int i = 0; i < 4; i++) 			
			tableauPiles.get(i).add(stockPile.deal());	// deals one card to each tableau pile
		
	}
	
	/**
	 * Retrieves the top card to a specified tableau pile. Does NOT remove the card.
	 * @param index
	 * 		the index of the tableau pile
	 * @return
	 * 		the top card on the pile
	 */			
	public Card getTableauTopCard(int index) { return tableauPiles.get(index).getTopCard(); }
	
	/**
	 * Retrieves the size of a specified tableau pile. 
	 * @param index
	 * 		the index of the tableau pile
	 * @return
	 * 		the size of the pile
	 */	
	public int getIndivTableauSize(int index) { return tableauPiles.get(index).getTableauSize(); }
	
	/**
	 * Retrieves the top card of the stock pile. Does NOT remove the card.
	 * @return
	 * 		the top card on the deck
	 */	
	public Card getStockTopCard() { return stockPile.getNextCard(); }
	
	/**
	 * Retrieves the top card of the homecell pile. Does NOT remove the card.
	 * @return
	 * 		the top card on the deck
	 */	
	public Card getHomeTopCard() { return homecellPile.getTopCard(); }
	
	/**
	 * 	Removes the top card from a specified tableau pile if it is legal.
	 * @param index
	 * 		the index of the tableau pile that the card is being removed from
	 * @return
	 * 		true if removal was successful, false otherwise
	 */
	public boolean removeTopCard(int index) { 
		Card[] topCards = new Card[4];
		int currentSuit = tableauPiles.get(index).getTopCard().getSuit(); 
		int currentRank = tableauPiles.get(index).getTopCard().getRank();
		
		for(int i = 0; i < 4; i++) {
			topCards[i] = tableauPiles.get(i).getTopCard(); 	// adds the top card from each pile to the array
		}	
		for(int i = 0; i < 4; i++) {
			if(wasTableauClicked()) {
				if(topCards[i] == null) {       
					removed = tableauPiles.get(index).removeTopCard(tableauPiles.get(index).getTopCardIndex());
					tableauClicked = -1;
					return removed;
				}
			}
			if(wasHomeClicked()) {
				if(topCards[i] == null) {
					//i++;
				} else if(currentSuit == topCards[i].getSuit() && currentRank < topCards[i].getRank())	{						
					tableauPiles.get(index).removeTopCard(tableauPiles.get(index).getTopCardIndex());
					removed = true;
					homeClicked = -1;
					return removed;
				}
			}
				
		}
		
		return removed = false;
		
	}
	
	/**
	 * If the tableau pile is empty, add the card to the pile. 
	 * @param index
	 * 		index of the tableau pile you want to add to.
	 * @param toBeAdded
	 * 		card that you want to add to the specified tableau pile.
	 * @return
	 * 		returns true if the add was legal, otherwise it returns false
	 */
	public boolean addToTableau(int index, Card toBeAdded) {
		if(tableauPiles.get(index).isEmpty()) {				// if tableau is empty, add the card
			tableauPiles.get(index).add(toBeAdded);
			tableauClicked = -1;
			return true;
		}
		Card[] topCards = new Card[4];
		for(int i = 0; i < 4; i++) {
			topCards[i] = tableauPiles.get(i).getTopCard();			// store top card of each pile into array
		}
		
		for(int i = 0; i < 4; i++) {
			if(wasTableauClicked()) {
				if(topCards[i] == null) {       
					tableauPiles.get(index).removeTopCard(tableauPiles.get(index).getTopCardIndex());
					tableauClicked = -1;
					return true;
				}
			}
			
			if(wasHomeClicked()) {
				if(topCards[i] == null) {
					// do nothing
				} else if(topCards[i].getSuit() == toBeAdded.getSuit() && topCards[i].getRank() > toBeAdded.getRank()) {
					homeClicked = -1;
					tableauPiles.get(index).add(toBeAdded);
					return true;
				}
			}
		}
		return false;

	}
	
	/**
	 * "Removes" a card from the homecell pile.
	 * @return
	 * 		Always returns false because you cannot remove cards from the homecell pile.
	 */
	public boolean removeFromHomecell() { return false; }
	
	/**
	 * Adds a card to homecell if removal was successful.
	 * @param toBeAdded
	 * 			card you want to add to the homecell/ card that you just removed
	 * @return
	 * 		true if card was successfully added, false otherwise
	 */
	public boolean addToHomecell(Card toBeAdded) {
			boolean added = false;
			added = homecellPile.addToAcesTableau(toBeAdded);
			homeClicked = -1;
			return added;
	}
	
	/** Sets stockClicked to 1 */
	public void stockGotClicked() {stockClicked = 1; }
	
	/** @return true if stockClicked is 1, otherwise false */
	public boolean wasStockClicked() { return stockClicked == 1; }
	
	/** sets homeClicked to 1 */
	public void homeGotClicked() {homeClicked = 1; }
	
	/** @return true if homeClicked is 1, otherwise false */
	public boolean wasHomeClicked() { return homeClicked == 1; }
	
	/** sets tableauClicked to 1 */
	public void tableauGotClicked() {tableauClicked = 1; }
	
	/** @return true if stockClicked is 1, otherwise false */
	public boolean wasTableauClicked() { return tableauClicked == 1; }
	
	/** If wasStockClicked is true, one card will be dealt to each tableau pile from the stock pile. */
	public boolean stockClicked() {
		if(wasStockClicked() && !stockPile.isEmpty()) {
			for(int i = 0; i < 4; i++) 
				tableauPiles.get(i).add(stockPile.deal());	// deals one card to each tableau pile
			stockClicked = -1;
			tableauClicked = -1;
			return true;
		} else
			return false;
	}
	
	/**
	 * "Adds" a card to the stock pile.
	 * @return
	 * 		Always returns false because you cannot add cards to the stock pile.
	 */
	public boolean addToStock() { return false; }
//	
}
