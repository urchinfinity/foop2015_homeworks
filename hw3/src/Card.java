
public class Card {

	static final int MAX_SUIT = 4;
	static final int   JOKER1 = 0;
	static final int   JOKER2 = 1;
	static final int   	  RED = 0;
	static final int    BLACK = 1;
	static final String[] SUITS = {"R", "B", "C", "D", "H", "S"};
	static final String[] RANKS = {"0", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

	int id;

	int suit;
	int rank;
	int color;

	public Card(int index) {
		id = index;

		switch (id) {
			case JOKER1:
				suit = 0;
				rank = 0;
				color = RED;
				break;
			case JOKER2:
				suit = 1;
				rank = 0;
				color = BLACK;
				break;
			default:
				suit = (id - 2) % MAX_SUIT + 2;
				rank = (id - 2) / MAX_SUIT + 1;
				color = (suit == 2 || suit == 5) ? BLACK : RED;
				break;
		}
	}

	@Override
	public String toString() {
		return SUITS[suit] + RANKS[rank];
	}
}