import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class VariantOne extends OldMaid {

	public void initPlayers() {
		for (int i = 0; i < PLAYERS_NUM; i++) {
	        players[i] = new Player(i < 2 ? 14 : 13) {
	        	@Override
	        	public boolean isPairedCards(Card card1, Card card2) {
					return myIsPairedCards(card1, card2);
				}
	        };
	    }
	}

    public void initCards() {
		CARDS_NUM = 54;
		CARDS = new int[CARDS_NUM];
    	cardsDealer = new int[CARDS_NUM];

		for (int i = 0; i < CARDS_NUM; i++)
			CARDS[i] = i;
    }

    public boolean myIsPairedCards(Card card1, Card card2) {
		return card1.rank == card2.rank && card1.color == card2.color;
	}
}