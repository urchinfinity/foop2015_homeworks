import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;


public class Player {

	String name;

	int account;
	int curBet;
	ArrayList<Card> oldCards;
	ArrayList<Card> newCards;
	ArrayList<Character> discardCardsID;

	public Player(int money) {
		account = money;
		oldCards = new ArrayList<Card>();
		newCards = new ArrayList<Card>();
		discardCardsID = new ArrayList<Character>();
	}

	public void getUsername() {
		name = getUserInput();
	}

	public void getBet() {
		curBet = Integer.parseInt(getUserInput());
	}

	public void getKeepingCards() {
		resetDiscardCardsID();

		String keepingCardsStr = getUserInput();
		char[] keepingCardsArray = keepingCardsStr.toCharArray();

		for (int i = 0; i < keepingCardsStr.length(); i++) {
			switch (keepingCardsArray[i]) {
				case 'a':
					newCards.add(oldCards.get(0));
					break;
				case 'b':
					newCards.add(oldCards.get(1));
					break;
				case 'c':
					newCards.add(oldCards.get(2));
					break;
				case 'd':
					newCards.add(oldCards.get(3));
					break;
				case 'e':
					newCards.add(oldCards.get(4));
					break;
				default:
					System.out.printf("Invalid card ID (%c)\n", keepingCardsArray[i]);
					break;
			}
			discardCardsID.remove((Character)keepingCardsArray[i]);
		}
		oldCards.removeAll(newCards);
	}

	private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
	}

	private void resetDiscardCardsID() {
		discardCardsID.clear();
		newCards.clear();

		discardCardsID.add('a');
		discardCardsID.add('b');
		discardCardsID.add('c');
		discardCardsID.add('d');
		discardCardsID.add('e');
	}

}
