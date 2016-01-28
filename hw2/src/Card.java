import java.util.Comparator;

public class Card implements Comparable<Card> {

	static final int MAX_SUIT = 4;
	static final String[] SUITS = {"C", "D", "H", "S"};
	static final String[] FACES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

	int id;

	int suit;
	int face;

	public void setValue(int index) {
		id = index;
		suit = index % MAX_SUIT;
		face = index / MAX_SUIT;
	}

	public void setSF(int s, int f) {
		id = s + f * MAX_SUIT;
		suit = s;
		face = f;
	}

	@Override
	public String toString() {
		return SUITS[suit] + FACES[face];
	}

	@Override
    public int compareTo(Card card) {
        return Comparators.ID.compare(this, card);
    }


    public static class Comparators {
        public static Comparator<Card> ID = new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.id - c2.id;
            }
        };
    }
}