import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;


public class Player {

	int cardNum;
	ArrayList<Card> cards;
	ArrayList<Card> droppedCards;

	public Player(int num) {
		cardNum = num;
		cards = new ArrayList<Card>();
		droppedCards = new ArrayList<Card>();
	}

	public void dropCards() {
		for (int i = 0; i < cardNum - 1; i++) {
			if (isPairedCards(cards.get(i), cards.get(i+1))) {
				dropPairedCards(cards.get(i), cards.get(i+1));
				i--;
			}
			else if (i < cardNum - 2 && isPairedCards(cards.get(i), cards.get(i+2))) {
				dropPairedCards(cards.get(i), cards.get(i+2));
				i--;
			}
			else if (i < cardNum - 3 && isPairedCards(cards.get(i), cards.get(i+3))) {
				dropPairedCards(cards.get(i), cards.get(i+3));
				i--;
			}
		}
	}

	public void dropCard(Card card) {
		cards.remove(card);
		cardNum--;
	}

	public void insertnDropCard(Card card) {
		droppedCards.clear();

		for (int i = 0; i < cardNum + 1; i++) {
			//find the place to insert card
			if (i == cardNum || card.id < cards.get(i).id) {
				//insert card
				cards.add(i, card);
				cardNum++;

				//drop paired cards

				//check if there exists smaller paired card
				if (i > 0 && isPairedCards(card, cards.get(i-1))) {
					dropPairedCards(card, cards.get(i-1));
					return;
				}
				if (i > 1 && isPairedCards(card, cards.get(i-2))) {
					dropPairedCards(card, cards.get(i-2));
					return;
				}
				if (i > 2 && isPairedCards(card, cards.get(i-3))) {
					dropPairedCards(card, cards.get(i-3));
					return;
				}

				//check if there exists bigger paired card
				if (i < cardNum - 1 && isPairedCards(card, cards.get(i+1))) {
					dropPairedCards(card, cards.get(i+1));
					return;
				}
				if (i < cardNum - 2 && isPairedCards(card, cards.get(i+2))) {
					dropPairedCards(card, cards.get(i+2));
					return;
				}
				if (i < cardNum - 3 && isPairedCards(card, cards.get(i+2))) {
					dropPairedCards(card, cards.get(i+2));
					return;
				}

				//finish all actions
				return; 
			}
		}
	}

	public boolean hasNoCards() {
		return cardNum == 0;
	}

	public boolean isPairedCards(Card card1, Card card2) {
		return card1.rank == card2.rank;
	}

	private void dropPairedCards(Card card1, Card card2) {
		droppedCards.add(card1);
		droppedCards.add(card2);

		cards.remove(card1);
		cards.remove(card2);
		cardNum -= 2;
	}
}
