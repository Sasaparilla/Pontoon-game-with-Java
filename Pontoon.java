/**
    Pontoon
    @author: Bo Lin
    @version: 1.0
    purpose: KXC151 assignment 2, 2012
	Student identity number (ZUT): 2011********
	Student identity number (UTAS): *****
	Date: December 15th, 2012
	source code that I have implemented stage 4.
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;

public class Pontoon
{
	//final instance variables
	private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;	    //get width of screen
	private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;	//get height of screen
	private final int CLICK_YES = 0;		//dialog will return 0 when click yes
	private final int CLICK_NO = 1;			//dialog will return 1 when click no
	private final int MULTIPLIER = 150;		//horizontal gap for each card in a line is 150 pixels
	private final int NO_ONE = 0;			//the game is still in progress
	private final int PLAYER = 1;			//the game is over, the player has won
	private final int COMPUTER = 2;			//the game is over, the player has lost
	private final int FIVE_AND_UNDER = 5;	//the number of cards which beat 21
	private final int MAXIMUM_CARDS = 5;	//the max number of cards could player have
	private final Color CORNFLOWERBLUE = new Color(100, 149, 237);	//the color used for label
	private final Color DEEPPINK_4 = new Color(139, 10, 80);		//the color used for label

	//non-final instance variables
	private int playersNumberOfCards;		//used to hold current player's cards
	private int computersNumberOfCards;		//used to hold current computer's cards
	private int playersTotalOfCards;		//used to hold total value of player's cards
	private int computersTotalOfCards;		//used to hold total value of computer's cards
	private int numberOfWin;				//used to hold how many game user win
	private int numberOfLose;				//used to hold how many game user lose
	private int numberOfDraw;				//used to hold how many game user draw
	private int numberOfTotal;				//used to hold how many game user play
	private int wantsToPlay;				//used to get the selection from user(yes/no)
	private Decks deck;						//used to call methods in Decks
	private JFrame frame;					//used to design GUI

	//used to add components
	private JPanel panelNorth;
	private JPanel panelSouth;
	private JPanel panelEast;
	private JPanel panelWest;
	private JPanel panelCenter;
	private JPanel panelBackground;

	//used to represent user's behavior
	private JButton buttonStart;
	private JButton buttonHit;
	private JButton buttonStand;

	private JLabel labelPlayers;			//used to show player is user
	private JLabel labelComputers;			//used to show player is computer
	private JLabel labelPlayersScores;		//used to show scores of user
	private JLabel labelComputersScores;	//used to show scores of computer
	private JLabel labelWin;				//used to show item win
	private JLabel labelNumWin;				//used to show the game user win
	private JLabel labelLose;				//used to show item lose
	private JLabel labelNumLose;			//used to show the game user lose
	private JLabel labelDraw;				//used to show the item draw
	private JLabel labelNumDraw;			//used to show the game user draw
	private JLabel labelTotal;				//used to show item total
	private JLabel labelNumTotal;			//used to show the game user play
	private JLabel labelBackground;			//used to add ImageIcon
	private JLabel[] labelPlayersCards;		//used to show player's cards
	private JLabel[] labelComputersCards;	//used to show computer's cards
	private ImageIcon iconBackground;		//used to store background
	private Container container;			//used to get content pane from frame
	private boolean tracing;				//used to switch trace messages on and off
	/** constructor
		@param none
		@return	none
	*/
	public Pontoon()
	{
		deck = new Decks(3);

		playersNumberOfCards = 0;
		computersNumberOfCards = 0;
		playersTotalOfCards = 0;
		computersTotalOfCards = 0;
		numberOfWin = 0;
		numberOfLose = 0;
		numberOfDraw = 0;
		numberOfTotal = 0;
		tracing = false;
		deck.setTracing(false);

		frame = new JFrame("Pontoon");
		iconBackground = new ImageIcon("material/bg.jpg");
		panelNorth = new JPanel();
		panelSouth = new JPanel();
		panelEast = new JPanel();
		panelWest = new JPanel();
		panelCenter = new JPanel();
		buttonStart = new JButton("Start");
		buttonHit = new JButton("Hit");
		buttonStand = new JButton("Stand");
		labelPlayers = new JLabel("Players : ");
		labelComputers = new JLabel("Computers : ");
		labelPlayersScores = new JLabel("Scores : 0     ");
		labelComputersScores = new JLabel("Scores : 0     ");
		labelWin = new JLabel("     WIN :    ");
		labelLose = new JLabel("     LOSE :    ");
		labelDraw = new JLabel("     DRAW :     ");
		labelTotal = new JLabel("     TOTAL :     ");
		labelNumWin = new JLabel("0");
		labelNumLose = new JLabel("0");
		labelNumDraw = new JLabel("0");
		labelNumTotal = new JLabel("0");
		labelBackground = new JLabel(iconBackground);
		labelPlayersCards = new JLabel[MAXIMUM_CARDS];
		labelComputersCards = new JLabel[MAXIMUM_CARDS];

		container = frame.getContentPane();

		//painting label and button
		labelPlayers.setForeground(CORNFLOWERBLUE);
		labelComputers.setForeground(CORNFLOWERBLUE);
		labelWin.setForeground(CORNFLOWERBLUE);
		labelLose.setForeground(CORNFLOWERBLUE);
		labelDraw.setForeground(CORNFLOWERBLUE);
		labelTotal.setForeground(CORNFLOWERBLUE);
		labelPlayersScores.setForeground(CORNFLOWERBLUE);
		labelComputersScores.setForeground(CORNFLOWERBLUE);
		labelNumWin.setForeground(DEEPPINK_4);
		labelNumLose.setForeground(DEEPPINK_4);
		labelNumDraw.setForeground(DEEPPINK_4);
		labelNumTotal.setForeground(DEEPPINK_4);
		buttonStart.setForeground(Color.RED);
		buttonHit.setForeground(Color.RED);
		buttonStand.setForeground(Color.RED);

		buttonStart.setContentAreaFilled(false);
		buttonHit.setContentAreaFilled(false);
		buttonStand.setContentAreaFilled(false);
		buttonHit.setToolTipText("Click for another card");
		buttonStand.setToolTipText("Click to stop");

		panelSouth.add(buttonStart);
		panelSouth.add(buttonHit);
		panelSouth.add(buttonStand);
		panelWest.add(labelComputers);
		panelWest.add(labelPlayers);
		panelEast.add(labelComputersScores);
		panelEast.add(labelPlayersScores);
		panelNorth.add(labelWin);
		panelNorth.add(labelNumWin);
		panelNorth.add(labelLose);
		panelNorth.add(labelNumLose);
		panelNorth.add(labelDraw);
		panelNorth.add(labelNumDraw);
		panelNorth.add(labelTotal);
		panelNorth.add(labelNumTotal);
		panelCenter.setLayout(null);

		//place the label position and painting border
		for(int i = 0; i < MAXIMUM_CARDS; i++)
		{
			labelComputersCards[i] = new JLabel();
			labelComputersCards[i].setBounds(50 + i * MULTIPLIER, 50, 138, 202);
			labelComputersCards[i].setHorizontalAlignment(JLabel.CENTER);
			labelComputersCards[i].setVerticalAlignment(JLabel.CENTER);
			labelComputersCards[i].setBorder(BorderFactory.createMatteBorder (2, 2, 2, 2, new Color(255, 250, 205)));
			panelCenter.add(labelComputersCards[i]);
		}
		for(int i = 0; i < MAXIMUM_CARDS; i++)
		{
			labelPlayersCards[i] = new JLabel();
			labelPlayersCards[i].setBounds(50 + i * MULTIPLIER, 300, 138, 202);
			labelPlayersCards[i].setHorizontalAlignment(JLabel.CENTER);
			labelPlayersCards[i].setVerticalAlignment(JLabel.CENTER);
			labelPlayersCards[i].setBorder(BorderFactory.createMatteBorder (2, 2, 2, 2, new Color(255, 250, 205)));
			panelCenter.add(labelPlayersCards[i]);
		}
		panelWest.setLayout(new GridLayout(2, 1, 0, 0));
		panelEast.setLayout(new GridLayout(2, 1, 0, 0));

		panelNorth.setOpaque(false);
		panelSouth.setOpaque(false);
		panelWest.setOpaque(false);
		panelEast.setOpaque(false);
		panelCenter.setOpaque(false);

		container.add(panelSouth, BorderLayout.SOUTH);
		container.add(panelWest, BorderLayout.WEST);
		container.add(panelEast, BorderLayout.EAST);
		container.add(panelNorth, BorderLayout.NORTH);
		container.add(panelCenter, BorderLayout.CENTER);

		//set the background
		labelBackground.setBounds(0, 0, iconBackground.getIconWidth(), iconBackground.getIconHeight());
		panelBackground = (JPanel)frame.getContentPane();
		panelBackground.setOpaque(false);
		frame.getLayeredPane().setLayout(null);
		frame.getLayeredPane().add(labelBackground, new Integer(Integer.MIN_VALUE));
		frame.setSize(iconBackground.getIconWidth(), iconBackground.getIconHeight());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((SCREEN_WIDTH - iconBackground.getIconWidth()) / 2, (SCREEN_HEIGHT - iconBackground.getIconHeight()) / 2);
		frame.setResizable(false);
		frame.setVisible(true);

		buttonStartMonitor();
		buttonHitMonitor();
		buttonStandMonitor();
		wantsToStart();
		wantsToPlayPerformance();
	}

	/** play -- play a game of Pontoon
		@param none
		@return	none
	*/
	public void play()
	{
		trace("play: begins");

		trace("play: ends");
	}

	/** oneGame - used to execute the game once in the console
		@param none
		@return none
	*/
	public void oneGame()
	{
		trace("oneGame: begins");

		trace("oneGame: ends");
	}

	/**	buttonStartMonitor - add an actionlistener to button start
		@param none
		@return none
	*/
	public void buttonStartMonitor()
	{
		trace("buttonStartMonitor: begins");

		buttonHit.setEnabled(false);
		buttonStand.setEnabled(false);
		buttonStart.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					startPerformance();
				}
			}
		);

		trace("buttonStartMonitor: ends");
	}

	/**	buttonHitMonitor - add an actionlistener to button hit
		@param none
		@return none
	*/
	public void buttonHitMonitor()
	{
		trace("buttonHitMonitor: begins");

		buttonHit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					deck.drawCard(deck.PLAYERS_TURN);
					playersTotalOfCards = deck.getTotalOfCards(deck.PLAYERS_TURN);
					labelPlayersScores.setText("Scores : " + playersTotalOfCards + "     ");
					playersNumberOfCards = deck.getNumberOfCards(deck.PLAYERS_TURN);
					labelPlayersCards[playersNumberOfCards - 1].setIcon(new ImageIcon
							("material/" + deck.cardToString(deck.PLAYERS_TURN, playersNumberOfCards) + ".jpg"));
					gameOverPerformance();
				}
			}
		);

		trace("buttonHitMonitor: ens");
	}

	/**	buttonStandMonitor - add an actionlistener to button stand
		@param none
		@return none
	*/
	public void buttonStandMonitor()
	{
		trace("buttonStandMonitor: begins");

		buttonStand.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonHit.setEnabled(false);
					deck.refreshComputersTotal();
					while(! deck.gameOver())
					{
						deck.computersTurn();
						deck.refreshComputersTotal();
					}
					gameOverPerformance();
				}
			}
		);

		trace("buttonStandMonitor: ends");
	}

	/**	startPerformance - performance when the game start
		@param none
		@return none
	*/
	public void startPerformance()
	{
		trace("startPerformance: begins");

		buttonStart.setEnabled(false);
		buttonHit.setEnabled(true);
		buttonStand.setEnabled(true);
		for(int i = 0; i < MAXIMUM_CARDS; i++)
		{
			labelPlayersCards[i].setIcon(null);
			labelComputersCards[i].setIcon(null);
		}
		deck.deal();
		playersTotalOfCards = deck.getTotalOfCards(deck.PLAYERS_TURN);
		labelPlayersScores.setText("Scores : " + playersTotalOfCards + "     ");
		computersTotalOfCards = deck.getTotalOfCards(! deck.PLAYERS_TURN);
		labelComputersScores.setText("Scores : " + computersTotalOfCards + "     ");
		playersNumberOfCards = deck.getNumberOfCards(deck.PLAYERS_TURN);
		for(int i = 0; i < playersNumberOfCards; i++)
		{
			labelPlayersCards[i].setIcon(new ImageIcon("material/" + deck.cardToString(deck.PLAYERS_TURN, i + 1) + ".jpg"));
		}
		labelComputersCards[0].setIcon(new ImageIcon("material/reverse side.jpg"));
		labelComputersCards[1].setIcon(new ImageIcon("material/" + deck.cardToString(! deck.PLAYERS_TURN, 2) + ".jpg"));
		gameOverPerformance();

		trace("startPerformance: ends");
	}

	/**	gameOverPerformance - performance when the game is over
		@param none
		@return none
	*/
	public void gameOverPerformance()
	{
		trace("gameOverPerformance: begins");

		if(deck.gameOver())
		{
			buttonStart.setEnabled(true);
			buttonHit.setEnabled(false);
			buttonStand.setEnabled(false);
			deck.refreshComputersTotal();
			computersTotalOfCards = deck.getTotalOfCards(! deck.PLAYERS_TURN);
			labelComputersScores.setText("Scores : " + computersTotalOfCards + "     ");
			computersNumberOfCards = deck.getNumberOfCards(! deck.PLAYERS_TURN);
			for(int i = 0; i < computersNumberOfCards; i++)
			{
				labelComputersCards[i].setIcon(new ImageIcon("material/" + deck.cardToString(! deck.PLAYERS_TURN, i + 1) + ".jpg"));
			}
			scoresCalculation();
			wantsToPlay = JOptionPane.showConfirmDialog(frame, "Player" + endingSituation(deck.PLAYERS_TURN) + "\n\nComputer" + endingSituation(
								! deck.PLAYERS_TURN) + "\n\nDo you want to play again ?", "Some messages", JOptionPane.YES_NO_OPTION);
			wantsToPlayPerformance();
		}

		trace("gameOverPerformance: ends");
	}
	/**	wantsToPlayPerformance - different performance when users click yes/no
		@param none
		@return none
	*/
	public void wantsToPlayPerformance()
	{
		trace("wantsToPlayPerformance: begins");

		if(wantsToPlay == CLICK_YES)
		{
			startPerformance();
		}
		else if (wantsToPlay == CLICK_NO)
		{
			JOptionPane.showMessageDialog(frame, "You have played " + labelNumTotal.getText() + " games\nYou win : " + labelNumWin.getText() +
							"\nYou lose : " + labelNumLose.getText() + "\nYou draw : " + labelNumDraw.getText() + "\nThanks for playing!",
							"Final messages", JOptionPane.INFORMATION_MESSAGE);
		}

		trace("wantsToPlayPerformance: ends");
	}

	/** scoresCalculation - statistics game results(win, lose, draw, total number of games)
		@param none
		@return none
	*/
	public void scoresCalculation()
	{
		trace("scoresCalculation: begins");

		switch(deck.whoWon())
		{
			case NO_ONE:
				numberOfDraw++;
				labelNumDraw.setText("" + numberOfDraw);
				break;
			case PLAYER:
				numberOfWin++;
				labelNumWin.setText("" + numberOfWin);
				break;
			case COMPUTER:
				numberOfLose++;
				labelNumLose.setText("" + numberOfLose);
				break;
		}
		numberOfTotal = numberOfDraw + numberOfWin + numberOfLose;
		labelNumTotal.setText("" + numberOfTotal);

		trace("scoresCalCulation: ends");
	}

	/** endingSituation - indicating situation at end of turn(bust, 5 and under, sit)
		@param boolean -- checking player or computer
		@return String -- detailed situation at end of turn(bust, 5 and under, sit)
	*/
	public String endingSituation(boolean playerOrComputer)
	{
		String situation;

		trace("endingSituation: begins");

		if(deck.getNumberOfCards(playerOrComputer) == FIVE_AND_UNDER)
		{
			situation = " have five cards and under 21";
		}
		else if(deck.isBust(playerOrComputer))
		{
			situation = " is bust";
		}
		else
		{
			situation = " have " + deck.getNumberOfCards(playerOrComputer) + " cards and sit on " + deck.getTotalOfCards(playerOrComputer);
		}

		trace("endingSituation: ends");
		return situation;
	}

	/** wantsToStart - create a dialog with a general description of the game and ask users whether want to start the game
		@param none
		@return none
	*/
	public int wantsToStart()
	{
		trace("wantsToStart: begins");

		wantsToPlay = JOptionPane.showConfirmDialog(frame, "Welcome to play Pontoon !\n\nYou can click \'Hit\' to get another card, or click 		\'Stand\' to stop.\n\nThe object of the game is to get as close to 21 as possible or to have 5 cards with a total under 21.\n\n Would you like to start now?", "TIPS", JOptionPane.YES_NO_OPTION);

		trace("wantsToStart: ends");
		return wantsToPlay;
	}


	/** explain -- give information on the game
		@param none
		@return	none
	*/
	public void explain()
	{
		trace("explain: begins");

		trace("explain: ends");
	}

	/** setTracing - used to turn tracing messages on or off
		@param boolean -- indicates the required state of messages (true on, false off)
		@return none
	*/
	public void setTracing(boolean traceState)
	{
		tracing = traceState;
	}

	/** trace - displays tracing messages
		@param String -- the message to be displayed if instance variable tracing is true
		@return none
	*/
	public void trace(String message)
	{
		if(tracing)
		{
			System.out.println("Pontoon: " + message);
		}
	}
}

