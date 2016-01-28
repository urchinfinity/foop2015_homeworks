import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import foop.*;

public class PlayerB01902080 extends Player {

	static final int NA = -1;
	static final int HT = 0;
	static final int ST = 1;
	static final int DD = 2;
	static final int SU = 3;
	static final int SP = 4;

	static final int[] HARD_STRATEGY_TABLE = {
/*	     2   3   4   5   6   7   8   9   T   A   */
/*-----------------------------------------------*/
/*7*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*8*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*9*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*10*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*11*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*12*/	HT, HT, ST, ST, ST, HT, HT, HT, HT, HT,
/*13*/	ST, ST, ST, ST, ST, HT, HT, HT, HT, HT,
/*14*/	ST, ST, ST, ST, ST, HT, HT, HT, HT, HT,
/*15*/	ST, ST, ST, ST, ST, HT, HT, HT, HT, HT,
/*16*/	ST, ST, ST, ST, ST, HT, HT, HT, HT, HT,
/*17*/	ST, ST, ST, ST, ST, ST, ST, ST, ST, ST};

	static final int[] HARD_DOUBLE_DOWN_TABLE = {
/*	     2   3   4   5   6   7   8   9   T   A   */
/*-----------------------------------------------*/
/*7*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*8*/	NA, NA, NA, DD, DD, NA, NA, NA, NA, NA,
/*9*/	DD, DD, DD, DD, DD, NA, NA, NA, NA, NA,
/*10*/	DD, DD, DD, DD, DD, DD, DD, DD, NA, NA,
/*11*/	DD, DD, DD, DD, DD, DD, DD, DD, DD, DD,
/*12*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*13*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*14*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*15*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*16*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*17*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA};

	static final int[] SOFT_STRATEGY_TABLE = {
/*	     2   3   4   5   6   7   8   9   T   A   */
/*-----------------------------------------------*/
/*A5*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*A6*/	HT, HT, HT, HT, HT, HT, HT, HT, HT, HT,
/*A7*/	ST, ST, ST, ST, ST, ST, ST, HT, HT, HT,
/*A8*/	ST, ST, ST, ST, ST, ST, ST, ST, ST, ST,
/*A9+*/	ST, ST, ST, ST, ST, ST, ST, ST, ST, ST};

	static final int[] SOFT_DOUBLE_DOWN_TABLE = {
/*	     2   3   4   5   6   7   8   9   T   A   */
/*-----------------------------------------------*/
/*A5*/	NA, NA, DD, DD, DD, NA, NA, NA, NA, NA,
/*A6*/	DD, DD, DD, DD, DD, NA, NA, NA, NA, NA,
/*A7*/	NA, DD, DD, DD, DD, NA, NA, NA, NA, NA,
/*A8*/	NA, NA, NA, NA, DD, NA, NA, NA, NA, NA,
/*A9+*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA};

	static final int[] PAIR_TABLE = {
/*	     2   3   4   5   6   7   8   9   T   A   */
/*-----------------------------------------------*/
/*22*/	SP, SP, SP, SP, SP, SP, NA, NA, NA, NA,
/*33*/	SP, SP, SP, SP, SP, SP, SP, NA, NA, NA,
/*44*/	NA, NA, SP, SP, SP, NA, NA, NA, NA, NA,
/*55*/	DD, DD, DD, DD, DD, DD, DD, DD, NA, NA,
/*66*/	SP, SP, SP, SP, SP, SP, NA, NA, NA, NA,
/*77*/	SP, SP, SP, SP, SP, SP, SP, NA, NA, NA,
/*88*/	SP, SP, SP, SP, SP, SP, SP, SP, SP, SP,
/*99*/	SP, SP, SP, SP, SP, NA, SP, SP, NA, SP,
/*TT*/	NA, NA, NA, NA, NA, NA, NA, NA, NA, NA,
/*AA*/	SP, SP, SP, SP, SP, SP, SP, SP, SP, SP};

	private int remainingCardNum;
	private int[] remainingCards;

	private double lastChips;

	public PlayerB01902080(int chips) {
		super(chips);
		remainingCards = new int[Card.VALUE_UPPER-3];
		lastChips = chips;
	}

	// Ask whether the player wants to buy insurance.
	public boolean buy_insurance(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		calculateRemainingCards(my_open, dealer_open, current_table);

		//calculate the probability that  dealer gets a hole card with value equal to 10
		//p falls between 0.186 ~ 0.372 for general cases
		//if p >= 0.5, then buy insurance!

    	return calculateProbability(10) >= 0.28;
	}

	// Ask whether the player wants to double down.
	public boolean do_double(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		ArrayList<Byte> possibleHandValues = calculateHand(my_open);
		boolean hasSoftHand = hasAce(my_open);

		byte dealerOpenCardValue = dealer_open.getValue();
		byte hardHandValue = possibleHandValues.get(possibleHandValues.size()-1);
		byte softHandValue = 22;
		for (int i = 0; i < possibleHandValues.size() - 1 && softHandValue > 21; i++)
			softHandValue = possibleHandValues.get(i);

		if (isPair(my_open))
			if (PAIR_TABLE[getPairTableIndex(hardHandValue, dealerOpenCardValue)] == DD)
				return true;

		if (softHandValue <= 21) {
			return SOFT_DOUBLE_DOWN_TABLE[getSoftTableIndex(softHandValue, dealerOpenCardValue)] == DD;
		} else {
			return HARD_DOUBLE_DOWN_TABLE[getHardTableIndex(hardHandValue, dealerOpenCardValue)] == DD;
		}
	}

	// Ask whether the player wants to do split.
	public boolean do_split(ArrayList<Card> my_open, Card dealer_open, ArrayList<Hand> current_table) {
		byte dealerOpenCardValue = dealer_open.getValue();
		byte pairHandValue = (byte)(alignedValue(my_open.get(0).getValue())*2);

		return PAIR_TABLE[getPairTableIndex(pairHandValue, dealerOpenCardValue)] == SP;
	}

	// Ask whether the player wants to do surrender.
	public boolean do_surrender(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		if (dealer_open.getValue() == 10)
			return my_open.getValue() == 6;
		else if (dealer_open.getValue() == 1)
			return my_open.getValue() == 6 || my_open.getValue() == 7;
		return false;
	}

	// Ask whether the player wants to hit.
	public boolean hit_me(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		ArrayList<Byte> possibleHandValues = calculateHand(my_open);
		boolean hasSoftHand = hasAce(my_open);

		byte dealerOpenCardValue = dealer_open.getValue();
		byte hardHandValue = possibleHandValues.get(possibleHandValues.size()-1);
		byte softHandValue = 22;
		for (int i = 0; i < possibleHandValues.size() - 1 && softHandValue > 21; i++)
			softHandValue = possibleHandValues.get(i);

		if (softHandValue <= 21) {
			return SOFT_STRATEGY_TABLE[getSoftTableIndex(softHandValue, dealerOpenCardValue)] == HT;
		} else {
			return HARD_STRATEGY_TABLE[getHardTableIndex(hardHandValue, dealerOpenCardValue)] == HT;
		}
	}

	// Get the number of chips that the player wants to bet.
	public int make_bet(ArrayList<Hand> last_table, int total_player, int my_position) {
		Random rand = new Random();
		double multiplier;

		double bookie = get_chips() / 15;

		if (lastChips == get_chips()) {
			//player gets a push in last round :|
			//bookie falls between 0.75 ~ 1.5
			multiplier = (double)rand.nextInt(76);
			bookie *= (multiplier + 75) / 100;
		} else if (lastChips > get_chips()) {
			//player loses in last round :(
			//bookie falls between 0.5 ~ 1.0
			multiplier = (double)rand.nextInt(51);
			bookie *= (multiplier + 50) / 100;

		} else if (lastChips < get_chips()) {
			//player wins in last round :)
			//bookie falls between 1.0 ~ 2.0
			multiplier = (double)rand.nextInt(101);
			bookie *= (multiplier + 100) / 100;

		}
		lastChips = get_chips();

		return (int)bookie;
	}

	// Show the player's status.
	public java.lang.String toString() {
		return "" + get_chips() + " chip(s)";
	}

	private boolean isPair(Hand hand) {
		return hand.getCards().size() == 2 && hand.getCards().get(0).getValue() == hand.getCards().get(1).getValue();
	}

	private boolean hasAce(Hand hand) {
		for (int i = 0; i < hand.getCards().size(); i++) {
			if (hand.getCards().get(i).getValue() == Card.VALUE_LOWER)
				return true;
		}
		return false;
	}

	private ArrayList<Byte> calculateHand(Hand hand) {
        ArrayList<Card> cards = hand.getCards();
        ArrayList<Byte> handValues = new ArrayList<Byte>();
        byte handValue = 0;
        int aceNum = 0;
        for (int i = 0; i < cards.size(); i++) {
            byte value = cards.get(i).getValue();
            switch(value) {
                case Card.VALUE_LOWER:
                    handValue += 11;
                    aceNum++;
                    break;
                case Card.VALUE_UPPER:
                case Card.VALUE_UPPER-1:
                case Card.VALUE_UPPER-2:
                    handValue += 10;
                    break;
                default:
                    handValue += value;
            }
        }

        handValues.add(handValue);
        while (aceNum-- > 0) {
            handValue -= 10;
        	handValues.add(handValue);
        }

        return handValues;
    }

    private void calculateRemainingCards(Hand myOpen, Card dealerOpen, ArrayList<Hand> currentTable) {
    	//clear remainingCards value
    	for (int i = 0; i < Card.VALUE_UPPER - 4; i++)
    		remainingCards[i] = 4;
    	remainingCards[9] = 16;

    	//get list of total cards on current table
    	ArrayList<Card> currentTableCards = new ArrayList<Card>();
    	currentTableCards.addAll(myOpen.getCards());
    	currentTableCards.add(dealerOpen);
    	for (int i = 0; i < currentTable.size(); i++)
    		currentTableCards.addAll(currentTable.get(i).getCards());

  		//exclude dealer's hole card
    	remainingCardNum = 51;

    	//calculate remaining cards
    	for (int i = 0; i < currentTableCards.size(); i++) {
    		int index = currentTableCards.get(i).getValue() >= 10 ? 9 : currentTableCards.get(i).getValue()-1;
    		remainingCards[index]--;
    		remainingCardNum--;
    	}

    }

    private void calculateRemainingCards(Card myOpen, Card dealerOpen, ArrayList<Hand> currentTable) {
    	Hand myHand = new Hand(new ArrayList<Card>(Arrays.asList(myOpen)));
    	calculateRemainingCards(myHand, dealerOpen, currentTable);
    }

    private double calculateProbability(int expectedCardValue) {
    	return (double)remainingCards[expectedCardValue-1]/remainingCardNum;
    }

    private int getHardTableIndex(byte handValue, byte dealerOpenCardValue) {
    	byte dealerAlignedValue = alignedValue(dealerOpenCardValue);
    	int xIndex = ((int)dealerAlignedValue+8) % 10;
    	int yIndex;
    	
    	if (handValue <= 7) 	  yIndex = 0;
    	else if (handValue >= 17) yIndex = 10;
    	else 					  yIndex = handValue - 7;

    	return xIndex + yIndex * 10;
    }

    private int getSoftTableIndex(byte handValue, byte dealerOpenCardValue) {
    	byte dealerAlignedValue = alignedValue(dealerOpenCardValue);
    	int xIndex = ((int)dealerAlignedValue+8) % 10;
    	int yIndex;
    	
    	if (handValue <= 16) 	  yIndex = 0;
    	else if (handValue >= 20) yIndex = 4;
    	else 					  yIndex = handValue - 16;

    	return xIndex + yIndex * 10;
    }

    private int getPairTableIndex(byte handValue, byte dealerOpenCardValue) {
    	byte dealerAlignedValue = alignedValue(dealerOpenCardValue);
    	int xIndex = ((int)dealerAlignedValue+8) % 10;
    	int yIndex = (alignedValue((byte)(handValue/2))+8) % 10;

    	return xIndex + yIndex * 10;
    }

    private byte alignedValue(byte handValue) {
    	return handValue >= 10 ? 10 : handValue;
    }
}
