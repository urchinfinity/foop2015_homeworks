import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;

public class Computer {
	Shuffler shuffler;

	ArrayList<Card> curCards;
	Card pair1;
	Card pair2;

	public Computer() {
		shuffler = new Shuffler(52);
	}

	public ArrayList<Card> dealCards() {
		ArrayList<Card> cards = new ArrayList<Card>();

		shuffler.shuffle();
		for (int i = 0; i < 5; i++) {
			Card card = new Card();
			card.setValue(shuffler.getNext());
			cards.add(card);
		}

		return sortCards(cards);
	}

	public ArrayList<Card> redealCards(ArrayList<Card> cards) {
		int length = cards.size();
		for (int i = 0; i < 5 - length; i++) {
			Card card = new Card();
			card.setValue(shuffler.getNext());
			cards.add(card);
		}

		return sortCards(cards);
	}

	public String calculateHand(ArrayList<Card> cards) {
		curCards = cards;

		if 		(isRoyalFlush())    return "royal flush";
		else if (isStraightFlush())	return "straight flush";
		else if (isStraight())		return "straight";
		else if (isFullHouse())		return "full House";
		else if (isFourOfAKind())	return "four of a kind";
		else if (isTwoPair())		return "two pair";
		else if (isThreeOfAKind())	return "three of a kind";
		else if (isJacksOrBetter())	return "Jacks or better";
		else if (isFlush())			return "flush";
		else						return "no";
	}

	private ArrayList<Card> sortCards(ArrayList<Card> cards) {
		Collections.sort(cards, Card.Comparators.ID);
		return cards;
	}

	private boolean isRoyalFlush() {
		return isSequential() && hasSameSuit() && isRoyal();
	}

	private boolean isStraightFlush() {
		return isSequential() && hasSameSuit();
	}

	private boolean isFourOfAKind() {
		return hasPair1() && hasPair2() && hasFourOfAKind();
	}

	private boolean isFullHouse() {
		return hasPair1() && hasPair2() && hasThreeOfAKind(true);
	}

	private boolean isFlush() {
		return hasSameSuit();
	}

	private boolean isStraight() {
		return isSequential();
	}

	private boolean isThreeOfAKind() {
		return hasPair1() && hasThreeOfAKind(false);
	}

	private boolean isTwoPair() {
		return hasPair1() && hasPair2();
	}

	private boolean isJacksOrBetter() {
		return hasPair1() && hasJacksOrBetter();
	}

	private boolean isSequential() {
		for (int i = 1; i < curCards.size(); i++) {
			if (curCards.get(i).face != curCards.get(i-1).face + 1)
				return false;
		}
		return true;
	}

	private boolean hasSameSuit() {
		for (int i = 1; i < curCards.size(); i++) {
			if (curCards.get(i).suit != curCards.get(i-1).suit)
				return false;
		}
		return true;
	}

	private boolean isRoyal() {
		return curCards.get(0).face == 8
		    && curCards.get(1).face == 9
		    && curCards.get(2).face == 10
		    && curCards.get(3).face == 11
		    && curCards.get(4).face == 12;
	}

	private boolean hasPair1() {
		for (int i = 1; i < curCards.size(); i++) {
			if (curCards.get(i).face == curCards.get(i-1).face) {
				pair1 = curCards.get(i);
				return true;
			}
		}
		return false;
	}

	private boolean hasPair2() {
		for (int i = 1; i < curCards.size(); i++) {
			if (curCards.get(i-1) != pair1 && curCards.get(i) != pair1 && curCards.get(i).face == curCards.get(i-1).face) {
				pair2 = curCards.get(i);
				return true;
			}
		}
		return false;
	}

	private boolean hasThreeOfAKind(boolean hasPair2) {
		int count1 = 0, count2 = 0;
		for (int i = 0; i < curCards.size(); i++) {
			if (curCards.get(i).face == pair1.face)
				count1++;
		}
		if (hasPair2) {
			for (int i = 0; i < curCards.size(); i++) {
				if (curCards.get(i).face == pair2.face)
					count2++;
			}
		}
		
		return (!hasPair2 && count1 == 3)
		    || (hasPair2 && (count1 == 3 || count2 == 3));
	}

	private boolean hasFourOfAKind() {
		return pair1.face == pair2.face;
	}

	private boolean hasJacksOrBetter() {
		return pair1.face == 9
		    || pair1.face == 10
		    || pair1.face == 11
		    || pair1.face == 12;
	}
}