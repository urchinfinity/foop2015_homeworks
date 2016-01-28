import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Player {
	static final int JOKER1 = 0;
	static final int JOKER2 = 1;

	int cardNum;
	List<Integer> cards;

	public Player(int num) {
		cardNum = num;
		cards = new ArrayList<>();
	}

	/** drop paired cards, start with C2(CARDS[2])
  	  * algorithm:
        if (card[i]-2)/4 == (card[i+1]-2)/4
      		drop card[i] and card[i+1]
  		note:
    		> skip dropped cards
    		> skip the last card
    */
	public void dropCards() {
		for (int i = 0; i < cardNum - 1; i++) {
			if (isPairedCards(cards.get(i), cards.get(i+1))) {
				dropPairedCards(cards.get(i), cards.get(i+1));
				i--;
			}
		}
	}

	public void dropCard(Integer card) {
		cards.remove(card);
		cardNum--;
	}

	public void insertnDropCard(Integer cardface) {
		for (int i = 0; i < cardNum; i++) {
			//find the place to insert card
			if (cardface < cards.get(i)) {
				//insert card
				cards.add(i, cardface);
				cardNum++;

				//drop paired cards

				//check if there exists smaller paired cardface
				if (i != 0 && isPairedCards(cardface, cards.get(i-1))) {
					dropPairedCards(cardface, cards.get(i-1));
					return;
				}

				//check if there exists bigger paired cardface
				if (i != cardNum - 1 && isPairedCards(cardface, cards.get(i+1))) {
					dropPairedCards(cardface, cards.get(i+1));
					return;
				}

				//finish all actions
				return; 
			}
		}
		//insert card at the end of all cards 
		cards.add(cardface);
		cardNum++;
		//check if the last two cards are pairs
		if (isPairedCards(cardface, cards.get(cardNum-2))) {
			dropPairedCards(cardface, cards.get(cardNum-2));
			return;
		}
	}

	public boolean hasNoCards() {
		return cardNum == 0;
	}

	private int alignedCardface(Integer cardface) {
		switch (cardface) {
			case JOKER1:
				return -2;
			case JOKER2:
				return -1;
			default:
				return (cardface - 2)/4;
		}
	}

	private boolean isPairedCards(Integer card1, Integer card2) {
		return alignedCardface(card1) == alignedCardface(card2);
	}

	private void dropPairedCards(Integer card1, Integer card2) {
		cards.remove(card1);
		cards.remove(card2);
		cardNum -= 2;
	}
}