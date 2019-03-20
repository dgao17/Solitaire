
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class GUI implements Runnable, MouseListener {
	/** constant of invisible border when card is "unselected" */
	private static final Border UNSELECTED_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	
	/** constant of border when card is "selected" */
    private static final Border SELECTED_BORDER = BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW);
	
    /** Width of the card image */
	private static final int CARD_WIDTH = 83;
	
	/** Length of the card image */
	private static final int CARD_LENGTH = 110;
	
	/** Instance of Baker's Dozen game */
	private BakersDozen bGame;
	
	/** Instance of Freecell game */
	private Freecell fGame;
	
	/** Instance of Ace's Up game */
	private AcesUp aGame;
	
	//------------------------------------------------------------------
	/** Array of the JLayeredPanes to hold Freecell's individual tableau piles on the screen */
	private JLayeredPane[] FreecellTableauPiles = new JLayeredPane[8];
	
	/** Array of JLayeredPanes to hold Baker's Dozen's individual tableau piles on the screen */
	private JLayeredPane[] BDTableauPiles = new JLayeredPane[13];
	
	/** Array of JLayeredPanes to hold Ace's Up's individual tableau piles on the screen */
	private JLayeredPane[] AcesUpTableauPiles = new JLayeredPane[4];
	
	/** Array of an ArrayList of Cards of Baker's Dozen's tableau piles */
	@SuppressWarnings("unchecked")
	private ArrayList<Card>[] BakersTableauPiles = new ArrayList[13];
	
	/** A stack to hold the indices of relative piles when clicked upon during the game */
	private Stack<Integer> pileIndices = new Stack<Integer>();
	//------------------------------------------------------------------
	
	/** Integer for when a tableau pile is clicked. -1 means not selected, while 1 means selected */
	private int clicked = -1;
	
	/** Integer for when a homecell pile is clicked. -1 means not selected, while 1 means selected */
	private int homecellClicked = -1;
	
	/** Integer for when a freecell pile is clicked. -1 means not selected, while 1 means selected */
	private int freecellClicked = -1;
	
	/** Counter for the number of times an illegal move has been made / the number of times an error message has popped up */
	private int messageDialogCounter = 0;
	
	/** ArrayList of MouseEvents to keep track of what has been clicked throughout the game */
	private ArrayList<MouseEvent> events = new ArrayList<>();
	
	/** ArrayList of ActionEvents to keep track of all instances of games created */
	private ArrayList<ActionEvent> game = new ArrayList<>();
	
	/** JMenuBar object to create the menubar for the game*/
	private JMenuBar menuBar;								
	
	/** The outermost window for the game. Parent container essentially */
	private JFrame frame;
	
	private JPanel content;
	
	/** JPanel for the part of the game that displays the tableau piles */
	private JPanel gamePortion;
	
	/** JPanel for the part of the game that displays the homecell and/or freecell piles depending on which game is running */
	private JPanel topPortion;

	private JLabel background = loadAndSetImage("/Cards/SolitaireStart.png");
	
	private AudioPlayer sfx;
	
	/** Creates the frame that opens on the screen when the game starts */
	public void run() {
		frame = new JFrame();
		frame.setTitle("Solitaire");					
		frame.setSize(new Dimension(1200,650));						// sets size of the window
		frame.setLocationRelativeTo(null);							// centers the window 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// quits when user clicks the x button
		createMenu();
		
		frame.setContentPane(background);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void displayError() {
		sfx = new AudioPlayer("/Cards/doh1.wav");
		if(messageDialogCounter == 5) {
			JLabel label = loadAndSetImage("/Cards/stop.jpg");
			JOptionPane.showMessageDialog(frame, label,null, JOptionPane.PLAIN_MESSAGE);
			messageDialogCounter++;
		} else {
			sfx.play();
			JOptionPane.showMessageDialog(frame, "Nope. Can't do that, homie.");
			messageDialogCounter++;
		}
	}
	/**
	 * Sets up the entire menubar & its options for the frame
	 */
	public void createMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("New Game");	
		
		// JMenuItems under New Game
		JMenuItem BDCommand = new JMenuItem("Baker's Dozen");	
		JMenuItem FreeCommand = new JMenuItem("Freecell");
		JMenuItem AcesCommand = new JMenuItem("Ace's Up");
		JMenuItem exit = new JMenuItem("Exit");
		
		// adds the 3 options under New Game in Menu Bar
		menu.add(BDCommand);
		menu.add(FreeCommand);
		menu.add(AcesCommand);
		menu.add(exit);
				
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
				
		// when user clicks exit, the application closes
		exit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) { close(); } });
				
		// when user clicks Baker's Dozen, a new Baker's Dozen game starts
		BDCommand.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) { 
			frame.setVisible(false);
			events.clear();
			pileIndices.clear();
			game.add(e);
			runBakersDozen();
			frame.setVisible(true);
			}	});
				
		// when user clicks Freecell, a new Freecell game starts
		FreeCommand.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) { 
			frame.setVisible(false);
			events.clear();
			pileIndices.clear();
			game.add(e);
			runFreecell();
			frame.setVisible(true);
		 } });
		
		AcesCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.setSize(new Dimension(900,650));						// sets size of the window
				background = loadAndSetImage("/Cards/SolitaireStartHomer.png");
				frame.setContentPane(background);
				events.clear();
				pileIndices.clear();
				game.add(e);
				runAcesUp();
				frame.setVisible(true);
			}});
		
	}
	
	/**
	 * Displays the current homecell piles for Baker's Dozen
	 */
	public void drawHomecell() {
		topPortion.removeAll();
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		topPortion.setOpaque(false);
		
		/** Display 4 homecell piles */
		for(int i = 0; i < 4; i++) {
			if(bGame.getIndivHomecellSize(i) == 0) {
				JLabel hpile = loadAndSetImage("/Cards/homecell"+(i+1)+".png");
				hpile.setName("Homecell");
				hpile.addMouseListener(this);
				topPortion.add(hpile);
			} else {
				JLabel hpile = loadAndSetImage("/PlayingCards/"+bGame.getHomecellTopCard(i).toString());
				hpile.setName("Homecell");
				hpile.addMouseListener(this);
				topPortion.add(hpile);
			}
		}
		
		frame.add(topPortion, BorderLayout.NORTH);
	}
	
	/**
	 * Correctly displays the most current tableau piles for Baker's Dozen on the screen
	 */
	public void drawBakersTableauPiles() {
		Point origin = new Point(10, 20);
		int offset = 20;
		gamePortion.removeAll();
		gamePortion = new JPanel();	
		gamePortion.setLayout(new FlowLayout(FlowLayout.CENTER));
		gamePortion.setOpaque(false);
		
		for(int i = 0; i < 13; i++) {
			BakersTableauPiles[i] = bGame.getTableauPile(i);
			BDTableauPiles[i] = new JLayeredPane();
			BDTableauPiles[i].setPreferredSize(new Dimension(CARD_WIDTH+11, 800));
			gamePortion.add(BDTableauPiles[i]);
			int layer = 0;
			
				for(int j = 0; j < bGame.getIndivTableauSize(i); j++) {
					JLabel card = loadAndSetImage("/PlayingCards/"+bGame.getTableauPile(i).get(j).toString());
					card.setBounds(origin.x, origin.y, CARD_WIDTH, CARD_LENGTH);
					card.setName("Card");
					card.addMouseListener(this);
					BDTableauPiles[i].add(card, new Integer(layer));
					
					origin.y += offset;
					layer++;
				}
				
				origin.y = 20;
		}
		frame.add(gamePortion, BorderLayout.CENTER);

	}
	
	/**
	 * Displays the homecell and freecell piles for Freecell game
	 */
	public void drawFreecellTopPortion() {
		topPortion.removeAll();
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		topPortion.setOpaque(false);
		
		JPanel home = new JPanel(new FlowLayout());
		home.setBorder(new EmptyBorder(0, 0, 0, 40));
		home.setOpaque(false);
		
		JPanel free = new JPanel(new FlowLayout());
		free.setBorder(new EmptyBorder(0, 40, 0, 0));
		free.setOpaque(false);
		/** Display 4 homecell piles */
		for(int i = 0; i < 4; i++) {
			if(fGame.getIndivHomecellSize(i) == 0) {
				JLabel hpile = loadAndSetImage("/Cards/homecell"+(i+1)+".png");
				hpile.setName("Homecell");
				hpile.addMouseListener(this);
				home.add(hpile);
			} else {
				JLabel hpile = loadAndSetImage("/PlayingCards/"+fGame.getHomecellTopCard(i).toString());
				hpile.setName("Homecell");
				hpile.addMouseListener(this);
				home.add(hpile);
			}
		}
		
		/** Display 4 freecell piles */
		for(int j = 0; j < 4; j++) {
			if(fGame.getIndivFreecellSize(j) == 0) {
				JLabel fpile = loadAndSetImage("/Cards/freecell.png");
				// "/Cards/freecell.png"
				fpile.setName("Empty Freecell");
				fpile.addMouseListener(this);
				free.add(fpile);
			} else {
				JLabel fpile = loadAndSetImage("/PlayingCards/"+fGame.getFreecellTopCard(j).toString());
				fpile.setName("Full Freecell");
				fpile.addMouseListener(this);
				free.add(fpile);
			}
		}
		
		topPortion.add(home);
		topPortion.add(free);
		frame.add(topPortion, BorderLayout.NORTH);
	}
	
	/**
	 * Correctly displays the most current tableau piles in Freecell
	 */
	public void drawFreecellTableauPiles() {
		Point origin = new Point(10, 20);
		int offset = 20;
		gamePortion.removeAll();
		gamePortion = new JPanel();	
		gamePortion.setLayout(new FlowLayout(FlowLayout.CENTER));
		gamePortion.setOpaque(false);
		
		for(int i = 0; i < 8; i++) {
			FreecellTableauPiles[i] = new JLayeredPane();
			FreecellTableauPiles[i].setPreferredSize(new Dimension(CARD_WIDTH+11, 800));
			gamePortion.add(FreecellTableauPiles[i]);
		
		if(i < 4) {
			for(int j = 0; j < fGame.getIndivTableauSize(i); j++) {
				JLabel card = loadAndSetImage("/PlayingCards/"+fGame.getTableauPile(i).get(j).toString());
				card.setBounds(origin.x, origin.y, CARD_WIDTH, CARD_LENGTH);
				card.setName("Card");
				card.addMouseListener(this);
				FreecellTableauPiles[i].add(card, new Integer(j));

				origin.y += offset;
			}
			
			origin.y = 20;
		} else {
			for(int k = 0; k < fGame.getIndivTableauSize(i); k++) {
				JLabel card = loadAndSetImage("/PlayingCards/"+fGame.getTableauPile(i).get(k).toString());
				card.setBounds(origin.x, origin.y, CARD_WIDTH, CARD_LENGTH);
				card.setName("Card");
				card.addMouseListener(this);
				FreecellTableauPiles[i].add(card, new Integer(k));
	
				origin.y += offset;
			}
			origin.y = 20;
		}
	}
		frame.add(gamePortion, BorderLayout.CENTER);
	}
	

	
	/**
	 * Creates a JLabel image to use for the game display
	 * @param fileNameRelativeToClassFile
	 * 			the name of the file needed to create the JLabel image
	 * @return the JLabel that was created from the file located
	 */
	private JLabel loadAndSetImage(String fileNameRelativeToClassFile) {
	    ImageIcon cardImage;
	    java.net.URL imgURL = this.getClass().getResource(fileNameRelativeToClassFile);
	    if (imgURL != null) {
	      cardImage = new ImageIcon(imgURL);
	    } else {
	     System.err.println("Couldn't find file: " + fileNameRelativeToClassFile);
	      cardImage = null;
	    }

	    JLabel label = new JLabel(cardImage);
	    Dimension d = new Dimension(cardImage.getIconWidth(), cardImage.getIconHeight());
	    label.setSize(d);
	    label.setPreferredSize(d);
	    label.setMaximumSize(d);
	    label.setMinimumSize(d);
	    
	    return label;
	  }

	/**
	 * Draws a border around the card that has been clicked on.
	 * @param component
	 * 			the JLabel that has been clicked on
	 */
    public void select(JLabel component) {
    	int y = -1;
    	
    	if(wasFreecellSelected()) {
    		y = 5;   
    		if(component.getY() == y) {
        		component.setBorder(SELECTED_BORDER);
        		component.repaint();
        	} 
    	} else {
    		y = component.getParent().getComponentCount()*20;
    	
    		if(component.getY() == y) {
    			component.setBorder(SELECTED_BORDER);
    			component.repaint();
    		} 
    	}
    }

    /**
     * Draws an invisible border around the card that was clicked on.
     * @param card
     * 			the JLabel that was clicked on.
     */
    public void unselect(JLabel card) {
    	card.setBorder(UNSELECTED_BORDER);
    	card.repaint();
    }
	
	/** close method for exiting application */
	public void close() { System.exit(0); }
	
	/**
	 * Sets up the initial display for when a new Baker's Dozen game begins
	 */
	public void runBakersDozen() {
		bGame = new BakersDozen();
		
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
	
		gamePortion = new JPanel();	
		gamePortion.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.add(gamePortion, BorderLayout.CENTER);		// add gamePortion panel to center area
		
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		frame.add(topPortion, BorderLayout.NORTH);
		
		drawHomecell();
		drawBakersTableauPiles();
		
	}
	
	/**
	 * Sets up the initial display for when a new Freecell game begins
	 */
	public void runFreecell() {
		fGame = new Freecell();
		fGame.startGame();
		
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
	
		gamePortion = new JPanel();	
		gamePortion.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.add(gamePortion, BorderLayout.CENTER);		// add gamePortion panel to center area
		
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		frame.add(topPortion, BorderLayout.NORTH);
		
		drawFreecellTableauPiles();
		drawFreecellTopPortion();
		
		
	}
	
	public void runAcesUp() {
		aGame = new AcesUp();
		aGame.initialSetUp();
		
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
		
		gamePortion = new JPanel();
		frame.add(gamePortion, BorderLayout.CENTER);
		
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		frame.add(topPortion, BorderLayout.NORTH);
		
		drawAcesUpTableauPiles();
		drawAcesUpTopPortion();
		//drawHomer();
	}
	
	public void drawAcesUpTableauPiles() {
		Point origin = new Point(0, 20);
		int offset = 20;
		gamePortion.removeAll();
		gamePortion = new JPanel();	
		gamePortion.setLayout(new FlowLayout(FlowLayout.CENTER));
		gamePortion.setOpaque(false);
		
		for(int i = 0; i < 4; i++) {
			AcesUpTableauPiles[i] = new JLayeredPane();
			AcesUpTableauPiles[i].setPreferredSize(new Dimension(CARD_WIDTH+11, 800));
			gamePortion.add(AcesUpTableauPiles[i]);
			
			for(int j = 0; j < aGame.getIndivTableauSize(i); j++) {
				JLabel card = loadAndSetImage("/PlayingCards/"+aGame.getIndivTableauPile(i).get(j).toString());
				card.setBounds(origin.x, origin.y, CARD_WIDTH, CARD_LENGTH);
				card.setName("Card");
				card.addMouseListener(this);
				AcesUpTableauPiles[i].add(card, new Integer(j));
				
				origin.y += offset;
			}
			if(aGame.getIndivTableauSize(i) == 0) {
				JLabel card = loadAndSetImage("/Cards/emptycell.png");
				card.setBounds(origin.x, origin.y, CARD_WIDTH, CARD_LENGTH);
				card.setName("Empty Tableau");
				card.addMouseListener(this);
				AcesUpTableauPiles[i].add(card, new Integer(0));
			}
			origin.y = 20;
		}
	frame.add(gamePortion, BorderLayout.CENTER);
	}
	
	public void drawAcesUpTopPortion() {
		topPortion.removeAll();
		topPortion = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
		fl.setAlignOnBaseline(true);
		topPortion.setLayout(fl);
		topPortion.setOpaque(false);
		
		Point origin = new Point(10, 6);
		int offset = 1;
		int cutdeck = 7;
		JLayeredPane deck = new JLayeredPane();
		deck.setPreferredSize(new Dimension(100, 120));
		topPortion.add(deck);
		
		if(!aGame.getStockPile().isEmpty()) {
		for(int j = 0; j < cutdeck; j++) {
			JLabel card = loadAndSetImage("/Cards/BackofCard.png");
			card.setBounds(origin.x, origin.y, 83, 107);
			card.setName("Stock");
			card.addMouseListener(this);
			deck.add(card, new Integer(j));
			
			origin.x -= offset;
			origin.y -= offset;
			
			if(aGame.getStockPile().getDeckSize() < 44)
				cutdeck--;
			
		}
		}
		
		if(aGame.getHomecellPile().getHomecellSize() == 0) {
			JLabel hpile = loadAndSetImage("/Cards/homecellaces.png");
			hpile.setName("Homecell");
			hpile.addMouseListener(this);
			topPortion.add(hpile);
		} else {
			JLabel hpile = loadAndSetImage("/PlayingCards/"+ aGame.getHomecellPile().getTopCard().toString());
			hpile.setName("Homecell");
			hpile.addMouseListener(this);
			topPortion.add(hpile);
		}
		

		
		frame.add(topPortion, BorderLayout.NORTH);
	}
	
	public void drawHomer() {
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setOpaque(false);
		JLabel donut = loadAndSetImage("/Cards/donut.png");
		bottom.add(donut, BorderLayout.SOUTH);

		frame.add(bottom, BorderLayout.WEST);
		
	}
	
	/**
	 * Method for seeing if a tableau pile was clicked
	 * @return
	 * 	true if pile was clicked
	 */
	public boolean wasPileSelected() {
		if(clicked == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Method for seeing if a homecell pile was clicked
	 * @return
	 * 	true if pile was clicked
	 */
	public boolean wasHomecellSelected() {
		if(homecellClicked == 1) 
			return true;
		else
			return false;
	}
	
	/**
	 * Method for seeing if a freecell pile was clicked
	 * @return
	 * 	true if pile was clicked
	 */
	public boolean wasFreecellSelected() {
		if(freecellClicked == 1)
			return true;
		else
			return false;
	}
// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------//	
	/** 
	 * Gets the index of the tableau pile clicked on for Baker's Dozen
	 * @return
	 * 		the index of the tableau pile that was clicked
	 */
	public int selectedPileIndex() {
		// index of tableau pile
		int selectedCol = -1;
		clicked = 1;
		int lastClickedEventIndex = events.size()-1;
		if(wasPileSelected()) {
			for(int i = 0; i < BDTableauPiles.length; i++) {														// loop through all the JLayeredPane piles
				if(events.get(lastClickedEventIndex).getComponent().getParent().equals(BDTableauPiles[i])) {		// compare last clicked pile (JLayeredPane) to all the JLayeredPanes
						selectedCol = i;	
						return selectedCol;
				}
					
			}
		}
		System.out.println("Tableau pile not clicked.");
		return selectedCol;
	}
	
	/** 
	 * Gets the index of the tableau pile clicked on for Ace's Up
	 * @return
	 * 		the index of the tableau pile that was clicked
	 */
	public int selectedAPileIndex() {
		// index of tableau pile
		int selectedCol = -1;
		clicked = 1;
		int lastClickedEventIndex = events.size()-1;
		if(wasPileSelected()) {
			for(int i = 0; i < AcesUpTableauPiles.length; i++) {														// loop through all the JLayeredPane piles
				if(events.get(lastClickedEventIndex).getComponent().getParent().equals(AcesUpTableauPiles[i])) {		// compare last clicked pile (JLayeredPane) to all the JLayeredPanes
					System.out.println("Clicked on tableau pile " + i);
						selectedCol = i;	
						return selectedCol;
				}
					
			}
		}
		System.out.println("Tableau pile not clicked.");
		return selectedCol;
	}
	
	/**
	 * Gets the index of the tableau pile clicked on for Freecell
	 * @return
	 * 		the index of the tableau pile
	 */
	public int selectedFPileIndex() {
		// index of tableau pile
		int selectedCol = -1;
		int lastClickedEventIndex = events.size()-1;
		if(wasPileSelected()) {
			for(int i = 0; i < FreecellTableauPiles.length; i++) {														// loop through all the JLayeredPane piles
				if(events.get(lastClickedEventIndex).getComponent().getParent().equals(FreecellTableauPiles[i])) {		// compare last clicked pile (JLayeredPane) to all the JLayeredPanes
						selectedCol = i;	
						return selectedCol;
				}
					
			}
		}
		System.out.println("Tableau pile not clicked.");
		return selectedCol;
	}
	
	/**
	 * Gets the index of the freecell pile that was clicked
	 * @return
	 * 		integer; index of freecell pile
	 */	
	private Integer selectedFreecellPileIndex() {
		int freePileNum = 0;
		
		if(events.get(events.size()-1).getComponent().getX() == 133) 
			freePileNum = 1;
		if(events.get(events.size()-1).getComponent().getX() == 221) 
			freePileNum = 2;
		if(events.get(events.size()-1).getComponent().getX() == 309) 
			freePileNum = 3;
		return freePileNum;
	}
// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------//	
	/**
	 * Called if a pile was clicked in Baker's Dozen
	 * @param index
	 * 		the index of the pile relative to tableau or homecell
	 */
	public void pileClicked(int index) {
		if(wasPileSelected()) {
			int oldPile = pileIndices.peek();										// index of last pile clicked on
			Card temp = bGame.getTableauTopCard(oldPile);							// get top card of the tableau pile you last clicked on
			Component oldCardPic = events.get(events.size()-2).getComponent();		// JLabel of first pile you clicked on	
			Component newCardPic = events.get(events.size()-1).getComponent();		// JLabel of pile you want to move the card to

				if(oldCardPic.getParent() != newCardPic.getParent()) {				// if you clicked on two different piles,		
					if(bGame.addtoTableauPile(temp, index)) {						// if you can legally add the card to the selected pile,
						oldCardPic.revalidate();
						oldCardPic.getParent().repaint();
						bGame.remove(oldPile);										// remove card from tableau pile previously clicked
						unselect((JLabel) oldCardPic);
						pileIndices.pop();
						clicked = -1;												// set clicked = -1 again, meaning no piles are selected
						drawBakersTableauPiles();
					} else {														// if illegal, display error message
						displayError();
						clicked = -1;
						unselect((JLabel) newCardPic);
						drawBakersTableauPiles();
						pileIndices.pop();
					}
				} else {
					unselect((JLabel) newCardPic);
					unselect((JLabel) oldCardPic);
					drawBakersTableauPiles();
					pileIndices.pop();
				}
			}
		
	}
	
	/**
	 * Called if a pile was clicked in Freecell
	 * @param index
	 * 		the index of the pile relative to homecell, freecell, or tableau 
	 */
	public void pileFClicked(int index) {
			int oldPile = pileIndices.peek();										// index of last pile clicked on
			
			Component oldCardPic = events.get(events.size()-2).getComponent();		// JLabel of first pile you clicked on	
			Component newCardPic = events.get(events.size()-1).getComponent();		// JLabel of pile you want to move the card to

			if(oldCardPic.getName() == "Card") {
				Card temp = fGame.getTableauTopCard(oldPile);							// get top card of the tableau pile you last clicked on
				if(oldCardPic.getParent() != newCardPic.getParent()) {				// if you clicked on two different piles,		
					if(fGame.addtoTableauPile(temp, index)) {						// if you can legally add the card to the selected pile, add it
						oldCardPic.revalidate();
						oldCardPic.getParent().repaint();
						fGame.removeFromTableauPile(oldPile);						// remove card from old tableau pile
						drawFreecellTableauPiles();									// update screen to show new location of cards
						gamePortion.repaint();
						pileIndices.pop();
					} else {	
						pileIndices.pop();
						displayError();
						clicked = -1;											// set clicked back to -1, meaning no piles are clicked
					}														
				} else
					pileIndices.pop();

			} else if(oldCardPic.getName() == "Full Freecell") {							// if previous click was a full freecell pile
				Card temp = fGame.getFreecellTopCard(oldPile);							// get top card of the freecell pile you last clicked on
				if(oldCardPic != newCardPic) {										// if you clicked on different piles
					if(fGame.addtoTableauPile(temp, index)) {						// if legal to add to the tableau pile, add it
						fGame.removeFromFreecellPile(oldPile);						// remove card from freecell pile
						oldCardPic.revalidate();
						oldCardPic.repaint();
						drawFreecellTableauPiles();									// update screen to show new location of cards
						drawFreecellTopPortion();
						freecellClicked = -1;
						gamePortion.repaint();
						topPortion.repaint();
						pileIndices.pop();
					} else {
						displayError();
						pileIndices.pop();
					}
				}
			} 
	} // end pileFClicked
	
	/**
	 * called if a homecell pile is clicked in Baker's Dozen
	 * @param index
	 * 		index of the tableau pile
	 */
	public void homecellClicked(int index) {
		if(wasPileSelected()) {
			homecellClicked = 1;
			Component oldCardPic = events.get(events.size()-2).getComponent();
			Component newCardPic = events.get(events.size()-1).getComponent();
			if(events.size() >= 2) {												// if you clicked on more than 2 things,
				if(oldCardPic.getParent() != newCardPic.getParent()) {	
					if(bGame.addtoHomecellPile(bGame.getTableauTopCard(index))) {		// if legal to add card to homecell pile, add it
						oldCardPic.revalidate();
						oldCardPic.getParent().repaint();
						bGame.remove(index);											// remove the card from tableau pile
						drawHomecell();
						drawBakersTableauPiles();
						topPortion.repaint();
						gamePortion.repaint();
					}
					else {
						displayError();
						System.out.println("Cannot add this card to homecell pile");
						homecellClicked = -1;
					}
				}
				
			}
		}
			
	}
	
	/**
	 * called if a homecell pile is clicked in Freecell
	 * @param index
	 * 		index of the tableau pile
	 */
	public void homecellFClicked(int index) {																							
			Component oldCardPic = events.get(events.size()-2).getComponent();		// previous card
			Component newCardPic = events.get(events.size()-1).getComponent();		// chosen homecell pile
				
			if(oldCardPic.getName() == "Card") {								
					if(oldCardPic.getParent() != newCardPic.getParent()) {				// if the two piles are different
						if(fGame.addtoHomecellPile(fGame.getTableauTopCard(index))) {		// && if legal to add card to homecell pile, add it
							fGame.removeFromTableauPile(index);								// remove the card from tableau pile
							oldCardPic.revalidate();
							oldCardPic.getParent().repaint();
							drawFreecellTopPortion();										// update homecell & tableau piles on the screen to show new locations
							drawFreecellTableauPiles();			
							topPortion.repaint();
							gamePortion.repaint();
						} else {
							System.out.println("Cannot add this card to homecell pile");
							homecellClicked = -1;	
							
							displayError();
						}
					}
				} else if(oldCardPic.getName() == "Full Freecell") {
					if(fGame.addtoHomecellPile(fGame.getFreecellTopCard(index))) {				// if legal to add to homecell pile
						fGame.removeFromFreecellPile(index);
						oldCardPic.revalidate();
						newCardPic.revalidate();
						drawFreecellTopPortion();										// update homecell & tableau piles on the screen to show new locations
						drawFreecellTableauPiles();			
						topPortion.repaint();
						gamePortion.repaint();
					} else {
						displayError();
				}
			}
	}  // end homecellFClicked
			
	
	/**
	 * called if a freecell pile is clicked in Freecell
	 * @param index
	 * 		index of the pile related to when it was called
	 */
	public void FreecellClicked(int index) {
		Component oldCardPic = events.get(events.size()-2).getComponent();			// what you previously clicked on
		Component newCardPic = events.get(events.size()-1).getComponent();			// what you just clicked on
		int freePileNum = selectedFreecellPileIndex();								// get the freecell pile index that you clicked
		
		if(wasFreecellSelected()) {
		if(newCardPic.getName() == "Empty Freecell") {								// if an empty freecell pile was clicked
			if(oldCardPic.getName() == "Card") {									// && if the previous click was a tableau pile card
				fGame.addtoFreecellPile(freePileNum, fGame.getTableauTopCard(index));	// add the card from tableau to freecell
				fGame.removeFromTableauPile(index);										// remove the card from tableau pile
				oldCardPic.revalidate();
				oldCardPic.getParent().repaint();
				drawFreecellTopPortion();												// update homecell & tableau piles on the screen to show new locations
				drawFreecellTableauPiles();			
				topPortion.repaint();
				gamePortion.repaint();
				freecellClicked = -1;
			} 
		} else {
				pileIndices.push(freePileNum);											// else, push index of freecell onto stack
				freecellClicked = -1;
			}
		} else {
				displayError();
				System.out.println("Cannot add this card to freecell pile");
				messageDialogCounter++;
				freecellClicked = -1;
			}
				
	}  // end FreecellClicked
	
	public void PileAClicked(int index) {
			int oldPile = pileIndices.peek();										// index of last pile clicked on
			Component oldCardPic = events.get(events.size()-2).getComponent();		// JLabel of first pile you clicked on	
			Card temp = aGame.getTableauTopCard(oldPile);							// get top card of the tableau pile you last clicked on
			Component newCardPic = events.get(events.size()-1).getComponent();		// JLabel of pile you want to move the card to
			if(newCardPic.getName() == "Empty Tableau") {							// if empty tableau is clicked
				if(aGame.removeTopCard(oldPile)) {									// if legal to remove
					aGame.addToTableau(index, temp);
					oldCardPic.revalidate();
					oldCardPic.getParent().repaint();							// remove card from old tableau pile
					drawAcesUpTableauPiles();									// update screen to show new location of cards
					gamePortion.repaint();
					pileIndices.pop();
					clicked = -1;
					unselect((JLabel) newCardPic);
					drawAcesUpTableauPiles();
				}
			} else {
				if(oldCardPic != newCardPic) {
					displayError();
				}
				unselect((JLabel) newCardPic);
				unselect((JLabel) oldCardPic);
				drawAcesUpTableauPiles();
				pileIndices.pop();
			}
		}
	
	public void displayAcesLoseMessage() {
		ArrayList<Card> topCards = new ArrayList<>();
		String[] options = {"OK", "Restart", "Exit"};
		int lost = 0;
		if(aGame.getStockPile().getDeckSize() == 0) {
			for(int i = 0; i < 4; i++) {
				if(aGame.getTableauTopCard(i) != null)
					topCards.add(aGame.getTableauTopCard(i));
			}
		
			
			for(int j = 0; j< topCards.size(); j++) {
				for(int k = j+1; k <  topCards.size(); k++) {
					if(topCards.get(j).getSuit() == topCards.get(k).getSuit() && aGame.getStockPile().getDeckSize() != 48) {
						lost++;
					}
				}
			}
		}
		if(lost == 0 && aGame.getStockPile().getDeckSize() == 0 && topCards.size() == 4) {
			int x = JOptionPane.showOptionDialog(null, "Game over.", null, JOptionPane.DEFAULT_OPTION
											, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			unselect((JLabel) events.get(events.size()-1).getComponent());
			pileIndices.clear();
			System.out.println("Game over.");
			
			if(x == 0) {
				System.out.println("OK clicked");
			} else if(x == 1) {
				System.out.println("Game restarted.");
				frame.setVisible(false);
				frame.setSize(new Dimension(900,650));						// sets size of the window
				background = loadAndSetImage("/Cards/SolitaireStartHomer.png");
				frame.setContentPane(background);
				events.clear();
				pileIndices.clear();
				runAcesUp();
				frame.setVisible(true);
			} else if(x == 2) {
				System.out.println("Application closed");
				close();
			}
				
			
		}
	}
	
	public void displayWin() {
		if(aGame.getHomecellPile().getHomecellSize() == 48) {
			JLabel win = loadAndSetImage("/Cards/gg.jpg");
			JOptionPane.showMessageDialog(frame, win ,null, JOptionPane.PLAIN_MESSAGE);
		}
	}

	/** 
	 * sets homecellClicked to 1
	 */
	public void selectHome() { homecellClicked = 1; }
	
	/**
	 * sets clicked = 1
	 */
	public void selectPile() { clicked = 1; }
	
	/** 
	 * sets freecellClicked = 1
	 */
	public void selectFree() { freecellClicked = 1; }
	
// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------//		
	
	/**
	 * main method to start the game
	 * @param args
	 */
	public static void main(String[] args) { new GUI().run(); }

// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------//	
	/**
	 * method that is called whenever a click occurs on the mouse
	 */
	@Override
	public void mousePressed(MouseEvent e) {
				events.add(e);																	// add MouseEvent to ArrayList
				System.out.println("Clicked on " + e.getComponent().getName());
				
				if(game.get(game.size()-1).getActionCommand() == "Baker's Dozen") {				// if you are playing Baker's Dozen
					if(e.getComponent().getName() == "Card" && pileIndices.size() == 0) {			// if a tableau pile card was clicked & nothing else was previously clicked
						select((JLabel) e.getComponent());											// draw border around selected card
						pileIndices.push(selectedPileIndex());										// push index of tableau pile clicked onto stack
					}
					else if(e.getComponent().getName() == "Homecell" && pileIndices.size() == 1) {								// && if you clicked on a homecell pile
						selectHome();															// set homecellClicked = 1, meaning it was clicked
						if(wasHomecellSelected()) {												// so if homecellSelected is true
							homecellClicked(pileIndices.pop());									// run homecellClicked method
							unselect((JLabel)events.get(events.size()-2).getComponent());		// get rid of the border of the previous card you clicked on
							topPortion.repaint();												// redraw homecell piles on the screen
						}
					} else if(e.getComponent().getName() == "Card" && pileIndices.size() == 1) {							// if you clicked on another tableau pile 
						if(wasPileSelected()) {													// && the previous click was a tableau pile as well
							pileClicked(selectedPileIndex());									// call pileClicked method
							unselect((JLabel)events.get(events.size()-2).getComponent());		// deselect the previous card you clicked on
							select((JLabel) e.getComponent());									// draw border around selected card
							gamePortion.repaint();												// redraw tableau piles on screen
						}
					}
					
				} else if(game.get(game.size()-1).getActionCommand() == "Freecell") {			// if you are playing Freecell
					if(e.getComponent().getName() == "Card" && pileIndices.size() == 0) {			// if a tableau pile card was clicked & nothing else was previously clicked
						selectPile();																// set clicked = 1, meaning it was clicked
						select((JLabel) e.getComponent());											// draw border around selected card
						pileIndices.push(selectedFPileIndex());										// push index of tableau pile clicked onto stack
					}
					else if(e.getComponent().getName() == "Homecell" && pileIndices.size() == 1) {								// && you clicked a homecell pile
						selectHome();
						if(wasHomecellSelected()) {												// so if homecellSelected is true
							homecellFClicked(pileIndices.pop());								// run homecellFClicked method
							unselect((JLabel)events.get(events.size()-2).getComponent());		// get rid of the border of the previous card you clicked on
						}
					} else if(e.getComponent().getName() == "Empty Freecell" && pileIndices.size() == 1) {		// if you click on a freecell pile,								
							selectFree();
							FreecellClicked(pileIndices.pop());									// run FreecellClicked method
					} else if(e.getComponent().getName() == "Full Freecell" ) {
							if(pileIndices.size() == 0) {
								selectFree();
								select((JLabel) e.getComponent());
								FreecellClicked(selectedFreecellPileIndex());	
							} else {
								if(messageDialogCounter == 5) {
									JLabel label = loadAndSetImage("/Cards/stop.jpg");
									JOptionPane.showMessageDialog(frame, label,null, JOptionPane.PLAIN_MESSAGE);
									messageDialogCounter++;
								} else {
									JOptionPane.showMessageDialog(frame, "C'mon, you can't move that there.");
									System.out.println("Cannot add this card to freecell pile");
									messageDialogCounter++;
								}
								unselect((JLabel)events.get(events.size()-2).getComponent());
								pileIndices.pop();
							}
					} else if(e.getComponent().getName() == "Card" && pileIndices.size() == 1) {							// if you clicked on another tableau pile
						if(wasPileSelected()) {													// && the previous click was a tableau pile as well
							pileFClicked(selectedFPileIndex());									// call pileFClicked method
							unselect((JLabel)events.get(events.size()-2).getComponent());		// deselect the previous card you clicked on
							gamePortion.repaint();												// redraw tableau piles on screen
						} else if(wasFreecellSelected()) {		
							pileIndices.push(selectedFreecellPileIndex());								// OR if previous click was a full freecell pile
							pileFClicked(pileIndices.peek());									// call pileFClicked method
						}
					} 
				} else if(game.get(game.size()-1).getActionCommand() == "Ace's Up") {
					if(e.getComponent().getName() == "Card" && pileIndices.size() == 0) {			// if a tableau pile card was clicked & nothing else was previously clicked
						selectPile();																// set clicked = 1, meaning it was clicked
						select((JLabel) e.getComponent());											// draw border around selected card
						pileIndices.push(selectedAPileIndex());										// push index of tableau pile clicked onto stack
					} else if(e.getComponent().getName() == "Card" && pileIndices.size() == 1) {
						aGame.tableauGotClicked();
						PileAClicked(selectedAPileIndex());
						unselect((JLabel)events.get(events.size()-2).getComponent());
					} else if(e.getComponent().getName() == "Empty Tableau" && pileIndices.size() == 1) {
						aGame.tableauGotClicked();
						PileAClicked(selectedAPileIndex());
						unselect((JLabel)events.get(events.size()-2).getComponent());
					} else if(e.getComponent().getName() == "Stock" && pileIndices.size() == 1) {
						stockClicked();
						pileIndices.pop();
						unselect((JLabel)events.get(events.size()-2).getComponent());
					} else if(e.getComponent().getName() == "Stock") {
						stockClicked();
					} else if(e.getComponent().getName() == "Homecell" && pileIndices.size() == 1) {
						aGame.homeGotClicked();
						homecellAClicked(pileIndices.pop());
						unselect((JLabel)events.get(events.size()-2).getComponent());
						displayWin();
					}
					
						displayAcesLoseMessage();
					
				}
	}
	

	public void stockClicked() {
			aGame.stockGotClicked();
			aGame.stockClicked();
			drawAcesUpTopPortion();
			drawAcesUpTableauPiles();
			gamePortion.revalidate();
			topPortion.revalidate();
			gamePortion.repaint();
			topPortion.repaint();
	}

	public void homecellAClicked(int index) {
		if(wasPileSelected()) {
			homecellClicked = 1;
			Component oldCardPic = events.get(events.size()-2).getComponent();
			Component newCardPic = events.get(events.size()-1).getComponent();
			Card temp = aGame.getTableauTopCard(index);
			aGame.homeGotClicked();
			if(events.size() >= 2) {												// if you clicked on more than 2 things,
				if(oldCardPic.getParent() != newCardPic.getParent()) {	
					if(aGame.removeTopCard(index)) {		
						aGame.addToHomecell(temp);	
						oldCardPic.revalidate();
						drawAcesUpTopPortion();
						drawAcesUpTableauPiles();
						topPortion.repaint();
						gamePortion.repaint();
					}
					else {
						if(messageDialogCounter == 5) {
							JLabel label = loadAndSetImage("/Cards/stop.jpg");
							JOptionPane.showMessageDialog(frame, label,null, JOptionPane.PLAIN_MESSAGE);
							messageDialogCounter++;
						} else {
							JOptionPane.showMessageDialog(frame, "C'mon, you can't move that there.");
							messageDialogCounter++;
						}
						System.out.print("Cannot add this card to homecell pile");
						homecellClicked = -1;
					}
				}
				
			}
		}
	}

	/**
	 * Un-used MouseListener methods
	 * Ignore them
	 */
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
//-------------------------------------------------------------------------------------
