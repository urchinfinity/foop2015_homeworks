import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class HandCreator {

	static final int MAX_SUIT = 4;
	static final int MAX_FACE = 13;

	private ArrayList<Card> allCards;
	private ArrayList<Card> cards;
	private Shuffler shuffler;
    private Random rand;
	
	public HandCreator() {
		allCards = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		shuffler = new Shuffler(52);
		rand = new Random();

		for (int i = 0; i < 52; i++) {
			Card card = new Card();
			card.setValue(i);
			allCards.add(card);
		}
	}

	public ArrayList<Card> royalFlush() {
		cards.clear();

		int suit = rand.nextInt(MAX_SUIT);
		for (int i = 8; i < 13; i++) {
			cards.add(allCards.get(i * MAX_SUIT + suit));
		}
		return sortCards(cards);
	}

	public ArrayList<Card> straightFlush() {
		cards.clear();
		
		int suit = rand.nextInt(MAX_SUIT);
		int faceStart = rand.nextInt(MAX_FACE - 4);
		for (int i = faceStart; i < faceStart + 5; i++) {
			cards.add(allCards.get(i * MAX_SUIT + suit));
		}
		return sortCards(cards);
	}

	public ArrayList<Card> fourOfAKind() {
		cards.clear();
		
		int face1 = rand.nextInt(MAX_FACE);
		int face2 = rand.nextInt(MAX_FACE);
		while (face2 == face1)
			face2 = rand.nextInt(MAX_FACE);
		int suit = rand.nextInt(MAX_SUIT);

		cards.add(allCards.get(face1 * MAX_SUIT));
		cards.add(allCards.get(face1 * MAX_SUIT + 1));
		cards.add(allCards.get(face1 * MAX_SUIT + 2));
		cards.add(allCards.get(face1 * MAX_SUIT + 3));
		cards.add(allCards.get(face2 * MAX_SUIT + suit));
		
		return sortCards(cards);
	}

	public ArrayList<Card> fullHouse() {
		cards.clear();
		
		int face1 = rand.nextInt(MAX_FACE);
		int suit1 = rand.nextInt(MAX_SUIT);
		int face2 = rand.nextInt(MAX_FACE);
		while (face2 == face1)
			face2 = rand.nextInt(MAX_FACE);
		int suit21 = rand.nextInt(MAX_SUIT);
		int suit22 = rand.nextInt(MAX_SUIT);
		while (suit22 == suit21)
			suit22 = rand.nextInt(MAX_SUIT);

		for (int i = 0; i < MAX_SUIT; i++) {
			if (i != suit1)
				cards.add(allCards.get(face1 * MAX_SUIT + i));
		}
		cards.add(allCards.get(face2 * MAX_SUIT + suit21));
		cards.add(allCards.get(face2 * MAX_SUIT + suit22));
		
		return sortCards(cards);
	}

	public ArrayList<Card> flush() {
		cards.clear();
		
		int suit = rand.nextInt(MAX_SUIT);
		int[] subCards = new int[MAX_FACE];
		for (int i = 0; i < MAX_FACE; i++)
			subCards[i] = i * MAX_SUIT + suit;
		shuffler.shuffleGivenIndex(subCards);

		for (int i = 0; i < 5; i++)
			cards.add(allCards.get(shuffler.getNext()));

		return sortCards(cards);
	}

	public ArrayList<Card> straight() {
		cards.clear();
		
		int faceStart = rand.nextInt(MAX_FACE - 4);
		for (int i = faceStart; i < faceStart + 5; i++) {
			int suit = rand.nextInt(MAX_SUIT);
			cards.add(allCards.get(i * MAX_SUIT + suit));
		}
		return sortCards(cards);
	}

	public ArrayList<Card> threeOfAKind() {
		cards.clear();
		
		int face1 = rand.nextInt(MAX_FACE);
		int suit1 = rand.nextInt(MAX_SUIT);

		int[] subFaces = new int[MAX_FACE - 1];
		for (int i = 0; i < MAX_FACE; i++) {
			if (i != face1)
				subFaces[i < face1 ? i : i-1] = i;
		}
		shuffler.shuffleGivenIndex(subFaces);

		int suit21 = rand.nextInt(MAX_SUIT);
		int suit22 = rand.nextInt(MAX_SUIT);

		for (int i = 0; i < MAX_SUIT; i++) {
			if (i != suit1)
				cards.add(allCards.get(face1 * MAX_SUIT + i));
		}
		cards.add(allCards.get(shuffler.getNext() * MAX_SUIT + suit21));
		cards.add(allCards.get(shuffler.getNext() * MAX_SUIT + suit22));
		
		return sortCards(cards);
	}

	public ArrayList<Card> twoPair() {
		cards.clear();
		
		int[] faces = new int[MAX_FACE];
		for (int i = 0; i < MAX_FACE; i++)
			faces[i] = i;

		shuffler.shuffleGivenIndex(faces);
		int face1 = shuffler.getNext();
		int face2 = shuffler.getNext();
		int face3 = shuffler.getNext();

		int[] suits = new int[MAX_SUIT];
		for (int i = 0; i < MAX_SUIT; i++)
			suits[i] = i;

		shuffler.shuffleGivenIndex(suits);
		cards.add(allCards.get(face1 * MAX_SUIT + shuffler.getNext()));
		cards.add(allCards.get(face1 * MAX_SUIT + shuffler.getNext()));

		shuffler.shuffleGivenIndex(suits);
		cards.add(allCards.get(face2 * MAX_SUIT + shuffler.getNext()));
		cards.add(allCards.get(face2 * MAX_SUIT + shuffler.getNext()));

		int suit3 = rand.nextInt(MAX_SUIT);
		cards.add(allCards.get(face3 * MAX_SUIT + suit3));

		return sortCards(cards);
	}

	public ArrayList<Card> jacksOrBetter() {
		cards.clear();
		
		int[] suits = new int[MAX_SUIT];
		for (int i = 0; i < MAX_SUIT; i++)
			suits[i] = i;

		shuffler.shuffleGivenIndex(suits);
		int suit1 = shuffler.getNext();
		int suit2 = shuffler.getNext();

		int face = rand.nextInt(4) + 9;
		int[] subFaces = new int[MAX_FACE - 1];
		for (int i = 0; i < MAX_FACE; i++) {
			if (i != face)
				subFaces[i < face ? i : i-1] = i;
		}
		shuffler.shuffleGivenIndex(subFaces);

		cards.add(allCards.get(face * MAX_SUIT + suit1));
		cards.add(allCards.get(face * MAX_SUIT + suit2));
		cards.add(allCards.get(shuffler.getNext() * MAX_SUIT + rand.nextInt(MAX_SUIT)));
		cards.add(allCards.get(shuffler.getNext() * MAX_SUIT + rand.nextInt(MAX_SUIT)));
		cards.add(allCards.get(shuffler.getNext() * MAX_SUIT + rand.nextInt(MAX_SUIT)));

		return sortCards(cards);
	}

	private ArrayList<Card> sortCards(ArrayList<Card> cards) {
		Collections.sort(cards, Card.Comparators.ID);
		return cards;
	}
}