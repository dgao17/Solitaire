import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class which implements a Card -- an object that has a suit and a rank
 * @author Group MyKneeHertz
 *
 */

public class Card {
	
	/** 
	 * constant integer values are assigned to each suit. These do not ever change.
	 */
	public final static int DIAMONDS = 1;
	public final static int CLUBS = 2;
	public final static int HEARTS = 3;
	public final static int SPADES = 4;
	
	/**
	 * Constant integer values are assigned to the non-integer ranks. They also never change.
	 */
	public final static int JACK = 11;
	public final static int QUEEN = 12;
	public final static int KING = 13;
	public final static int ACE = 1;
	
	/** Integer in which the card's suit is stored */
	private int suit;
	
	/** Integer in which the card's rank is stored */
	private int rank;
	
	/** Constructor creates a new Card with a valid suit and rank.
	 * IllegalArgumentException is thrown if a card's suit or rank is invalid. 
	 * @param integer values for suit and rank. 
	 */
	public Card(int suit, int rank) {
		if(suit < 0 && suit > 5)
			throw new IllegalArgumentException("Suit does not exist");
		if(rank < 0 && rank > 13)
			throw new IllegalArgumentException("Rank does not exist");
		
		this.suit = suit;
		this.rank = rank;
	}
	
	/** @return the integer value of the card's suit */
	public int getSuit() { return suit; }
	
	/** Assigns the suit of a card
	 * @param the integer value corresponding to the suit 
	 * */
	public void setSuit(int suit) {
		if(suit > 0 && suit < 5)
			this.suit = suit;
	}
	
	/** @return the integer value of the card's rank */
	public int getRank() { return rank; }
	
	/** Assigns the rank of a card
	 * @param the integer value corresponding to the rank 
	 * */
	public void setRank(int rank) {
			this.rank = rank;
	}
	
	public void setAceHighest() {
		if(this.rank == 1)
			this.rank = 14;
	}
	
	/** When displaying the rank/suit, it uses the String associated with the integer value.
	 *  @return A string of the card's suit and rank */
	public String suitToString() {
		switch(suit) {
			case 1:   	return  "D.png";
			case 2:   	return "C.png";
			case 3: 	return  "H.png";
			case 4:    	return "S.png";
			default: 	return null;	
		}	
	}
	
	public String rankToString() {
		switch(rank) {
		case 1: return "A";
		case 2: return "2";
		case 3: return "3";
		case 4: return "4";
		case 5: return "5";
		case 6: return "6";
		case 7: return "7";
		case 8: return "8";
		case 9: return "9";
		case 10: return "10";
		case 11: return "J";
		case 12: return "Q";
		case 13: return "K";
		case 14: return "A";
		default: return null;
		}
	}
	
	public String toString() {
		return rankToString() + suitToString();
	}
}
