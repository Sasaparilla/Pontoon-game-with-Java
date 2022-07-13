/**
    Decks
    @author: Julian Dermoudy
    @version: 2012
    purpose: provides resources for playing a version of the game "Pontoon"
*/

import java.util.Random;

public class Decks
{
	//final instance variables
	public final int NO_ONE=0;			//the game is still in progress
	public final int PLAYER=1;			//the game is over, the player has won
	public final int COMPUTER=2;		//the game is over, the player has lost
	public final int COMPUTER_SITS=16;	//house rules: computer sits on 16 or more
	public final boolean PLAYERS_TURN=true;	//the player's turn (opposite is computer's turn)

	private final int NUM_SUITS = 4;	//the number of suits in a deck
	private final String[] SUITS = {"Hearts", "Spades", "Diamonds", "Clubs"};	//the suit names
	private final int NUM_CARDS = 13;	//the number of cards in a suit
	private final String[] CARD_FACES = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};	//the card names
	private final int[] CARD_VALUES = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};	//the card values
	private final int FIVE_AND_UNDER=5;	//the number of cards which beat 21
	private final int HAND_SIZE=5;		//the maximum number of cards allowed

	//non-final instance variables
	private String[][] cards; 			//representation of the decks
	private Random generator; 			//used for randomising card drawing
	private int[] playersCards; 		//used for holding player's cards
	private int[] computersCards; 		//used for holding computer's cards
	private int currentCard;			//used to hold most recently dealt card
	private int numPlayersCards;		//used to hold count of player's cards
	private int numComputersCards;		//used to hold count of computer's cards
	private int totalPlayersCards;		//used to hold total value of player's cards
	private int totalComputersCards;	//used to hold total value of computer's cards
	private boolean tracing;			//used to switch trace messages on and off


	/** constructor
		@param int -- how many decks of cards should be used
		@return	none
	*/
    public Decks(int numDecks)
    {

		generator = new Random();
		tracing = true;
		cards = new String[numDecks][NUM_CARDS * NUM_SUITS];

		for (int deck=0; deck<numDecks; deck++)
		{
			for (int suit=0; suit<NUM_SUITS; suit++)
			{
				for (int card=0; card<NUM_CARDS; card++)
				{
					cards[deck][(suit*NUM_CARDS)+card]=CARD_FACES[card]+ " of " + SUITS[suit];
				}
			}
		}

		playersCards=new int[HAND_SIZE];
		computersCards=new int[HAND_SIZE];
    }

	/** deal - (re-)start a game
		@param none
		@return none
		deal two cards to the players hiding the computer's first card.
	*/
	public void deal()
	{
		trace("deal: begins");

		//initialise hands to empty
		for (int i=0; i<HAND_SIZE; i++)
		{
			playersCards[i]=-1;
			computersCards[i]=-1;
		}

		currentCard=-1;
		numPlayersCards=0;
		totalPlayersCards=0;
		numComputersCards=0;
		totalComputersCards=0;

		//deal first four cards
		drawCard(true);
		drawCard(false);
		drawCard(true);
		drawCard(false);

		//reveal computer's second card
		totalComputersCards=CARD_VALUES[computersCards[1]%cards.length%NUM_SUITS];

		trace("draw: ends");
	}

	/** drawCard - draw the next card
		@param booean -- player's turn?
		@return none
		draw a fresh card for the nominated player
	*/
	public void drawCard(boolean playersTurn)
	{
		int deck;
		int card;
		int result;
		boolean duplicate;

		trace("drawCard: begins");

		do
		{
			//determine next card
			deck=generator.nextInt(cards.length);
			card=generator.nextInt(NUM_CARDS*NUM_SUITS);
			result=deck*NUM_CARDS+card;

			//check if already drawn
			duplicate=false;
			for (int i=0; i<HAND_SIZE; i++)
			{
				if ((playersCards[i]==result) || (computersCards[i]==result))
				{
					duplicate=true;
				}
			}
		}
		while (duplicate);

		//update player/computer's hand
		if (playersTurn)
		{
			playersCards[numPlayersCards]=result;
			numPlayersCards++;
			totalPlayersCards+=CARD_VALUES[card%NUM_CARDS];
		}
		else
		{
			computersCards[numComputersCards]=result;
			numComputersCards++;
			totalComputersCards+=CARD_VALUES[card%NUM_CARDS];
		}

		currentCard=result;

		trace("drawCard: ends");
	}

	/** computersTurn - complete computer's turn
		@param none
		@return none
		Determine whether computer should have a turn, and if so, draw the card
	*/
	public void computersTurn()
	{
		trace("computersTurn: begins");

		if (!((numComputersCards == FIVE_AND_UNDER) || (totalComputersCards >= COMPUTER_SITS)))
		{
			drawCard(! PLAYERS_TURN);
		}

		trace("computersTurn: ends");
	}

	/** isBust - determine if nominated player is bust
		@param boolean -- checking the player?
		@return boolean -- whether the nominated player is bust
		Determine whether the total of the nominated player exceeds 21
	*/
	public boolean isBust(boolean playersTurn)
	{
		trace("isBust: begins and ends");

		if (playersTurn == PLAYERS_TURN)
		{
			return totalPlayersCards > 21;
		}
		else
		{
			return totalComputersCards > 21;
		}
	}

	/** refreshComputersTotal - recalculate computer's total
		@param none
		@return none
		Reveal all of computers cards, recalculating the total
	*/
	public void refreshComputersTotal()
	{
		trace("refreshComputersTotal: begins");

		totalComputersCards=0;

		for (int i=0; i<numComputersCards; i++)
		{
			totalComputersCards+=CARD_VALUES[computersCards[i]%(NUM_CARDS*NUM_SUITS)%NUM_CARDS];
		}

		trace("refreshComputersTotal: ends");
	}

	/** gameOver - determine whether turns have/should end
		@param none
		@return boolean -- whether or not the game is over
		Determine whether the game is effectively concluded
	*/
	public boolean gameOver()
	{
		boolean ans;

		trace("gameOver: begins");

		ans=false;

		// check is someone exceeds 21
		if (isBust(PLAYERS_TURN) || isBust(!PLAYERS_TURN))
		{
			trace("gameOver: a player is bust");
			ans=true;
		}

		// check if player has "5 and under"
		if ((getNumberOfCards(PLAYERS_TURN) == FIVE_AND_UNDER) && (!isBust(PLAYERS_TURN)))
		{
			trace("gameOver: player has 5 and under");
			ans=true;
		}

		// check if computer sits
		if (getTotalOfCards(!PLAYERS_TURN) >= COMPUTER_SITS)
		{
			trace("gameOver: computer sits");
			ans=true;
		}

		trace("gameOver: ends with return value of " + ans);
		return ans;
	}

	/** whoWon - determine who (if anyone) has won
		@param none
		@return int -- a value indicating noone, player, or computer
		Determine who (if anyone) has won
	*/
	public int whoWon()
	{
		int winner;

		trace("whoWon: begins");

		winner=NO_ONE;

		if (isBust(!PLAYERS_TURN))
		{
			trace("whoWon: computer is bust");
			if (!isBust(PLAYERS_TURN))
			{
				trace("whoWon: player is not bust");
				winner=PLAYER;
			}
		}
		else
		{
			trace("whoWon: computer is not bust");
			if (isBust(PLAYERS_TURN))
			{
				trace("whoWon: player is bust");
				winner=COMPUTER;
			}
			else
			{
				trace("whoWon: player is not bust");
				if ((numPlayersCards == FIVE_AND_UNDER) || (totalPlayersCards > totalComputersCards))
				{
					trace("whoWon: player has 5 and under or beats the computer's total");
					winner=PLAYER;
				}
				else
				{
					if ((numComputersCards == FIVE_AND_UNDER) || (totalComputersCards > totalPlayersCards))
					{
						trace("whoWon: computer has 5 and under or beats the player's total");
						winner=COMPUTER;
					}
				}
			}
		}

		trace("whoWon: ends with return value " + winner);
		return winner;
	}

	/** getNumberOfCards - indicate the number of cards in the nominated player's hand
		@param boolean -- count player's hand?
		@return int -- how many cards are held
		Determine how many cards the player/computer holds
	*/
	public int getNumberOfCards(boolean playersTurn)
	{
		trace("getNumberOfCards: begins and ends");

		if (playersTurn == PLAYERS_TURN)
		{
			return numPlayersCards;
		}
		else
		{
			return numComputersCards;
		}
	}

	/** getTotalOfCards - indicate the total value of cards in the nominated player's hand
		@param boolean -- sum player's hand?
		@return int -- what value of cards are held
		Determine value of cards the player/computer holds
	*/
	public int getTotalOfCards(boolean playersTurn)
	{
		trace("getTotalOfCards: begins and ends");

		if (playersTurn == PLAYERS_TURN)
		{
			return totalPlayersCards;
		}
		else
		{
			return totalComputersCards;
		}
	}

	/** lastCardToString - get printable form of last card dealt
		@param none
		@return String -- printable form of last card dealt
		Obtain String formatted description of last card dealt
	*/
	public String lastCardToString()
	{
		int deck;
		int card;

		trace("lastCardToString: begins");

		deck=currentCard / (NUM_SUITS*NUM_CARDS);
		card=currentCard % (NUM_SUITS*NUM_CARDS);

		trace("lastCardToString: card is " + cards[deck][card] + " (#" + currentCard + ", deck " +
			deck + ", card " + card +") with a value of " + CARD_VALUES[card%NUM_CARDS]);

		trace("lastCardToString: ends");
		return cards[deck][card];
	}

	/** cardToString - get printable form of indicated card held by nominated player
		@param boolean -- check player's hand?
		@param int -- card number to investigate (numbered from 1)
		@return String -- printable form of requested card
		Obtain String formatted description of particular card held by player/computer
	*/
	public String cardToString(boolean playersTurn,int cardNumber)
	{
		int deck;
		int card;

		trace("cardToString: begins");

		if (playersTurn == PLAYERS_TURN)
		{
			// get player's card's detail
			deck=playersCards[cardNumber-1] / (NUM_SUITS*NUM_CARDS);
			card=playersCards[cardNumber-1] % (NUM_SUITS*NUM_CARDS);

			trace("cardToString: card is " + cards[deck][card] + " (#" + playersCards[cardNumber-1] +
				", deck " + deck + ", card " + card +") with a value of " + CARD_VALUES[card%NUM_CARDS]);
		}
		else
		{
			// get computer's card's detail
			deck=computersCards[cardNumber-1] / (NUM_SUITS*NUM_CARDS);
			card=computersCards[cardNumber-1] % (NUM_SUITS*NUM_CARDS);
			trace("cardToString: card is " + cards[deck][card] + " (#" + computersCards[cardNumber-1] +
				", deck " + deck + ", card " + card +") with a value of " + CARD_VALUES[card%NUM_CARDS]);
		}

		trace("cardToString: ends");
		return cards[deck][card];
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
			System.out.println("Decks: " + message);
		}
	}
}
