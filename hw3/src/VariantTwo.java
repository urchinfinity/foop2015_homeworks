import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class VariantTwo extends OldMaid {

	public void initPlayers() {
        players[0] = new Player(13);
        players[1] = new Player(13);
        players[2] = new Player(13);
        players[3] = new Player(12);
	}

    public void initCards() {
		CARDS_NUM = 51;
		CARDS = new int[CARDS_NUM];
    	cardsDealer = new int[CARDS_NUM];

		joker = new Random().nextInt(CARDS_NUM + 1) + 2;

		for (int i = 0, cardface = 2; i < CARDS_NUM; i++, cardface++) {
			if (cardface == joker)
				i--;
			else
				CARDS[i] = cardface;
		}
    }
}