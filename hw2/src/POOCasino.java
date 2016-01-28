import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class POOCasino {

	static final String AUTHOR_ID 	= "b01902080";
	static final String AUTHOR_NAME = "Yu-Ching Wang";

	static Player player;
	static Computer computer;
//    static HandCreator handCreator;
//    static StatusChecker statusChecker;

	static int round;

	static Map<String, Integer> payoffTable;

	public static void main(String[] args) {

		player = new Player(1000);
		computer = new Computer();
//        handCreator = new HandCreator();
//        statusChecker = new StatusChecker();
		payoffTable = new HashMap<String, Integer>();

		round = 1;
		initPayoffTable();
		
		showCasinoInfo();
		getUsername();
		showWelcome();

		while(true) {
			showPlayerAccount();
			getUserBet();

			if (player.curBet == 0) {
                round--;
				showQuitGameMessage();
				break;
			}
			showCards();
			getKeepingCards();
			showDiscardCards();
			showNewCards();
			showHandAndPayoff();
		}
    }

    static void initPayoffTable() {
    	payoffTable.put(    "royal flush", 250);
    	payoffTable.put( "straight flush",  50);
    	payoffTable.put( "four of a kind",  25);
    	payoffTable.put(     "full House",   9);
    	payoffTable.put(          "flush",   6);
    	payoffTable.put(       "straight",   4);
    	payoffTable.put("three of a kind",   3);
    	payoffTable.put(       "two pair",   2);
    	payoffTable.put("Jacks or better",   1);
    	payoffTable.put(             "no",   0);
    }

    static void showCasinoInfo() {
    	System.out.printf("POOCasino Jacks or better, written by %s %s\n", AUTHOR_ID, AUTHOR_NAME);
    }

    static void getUsername() {
    	System.out.printf("Please enter your name: ");
    	player.getUsername();
    }

    static void showWelcome() {
    	System.out.printf("Welcome, %s.\n", player.name);
    }

    static void showPlayerAccount() {
    	System.out.printf("You have %d P-dollars now.\n", player.account);
    }

    static void getUserBet() {
    	System.out.printf("Please enter your P-dollar bet for round %d (1-5 or 0 for quitting the game): ", round);
    	player.getBet();
        while (player.curBet < 0 || player.curBet > 5) {
            System.out.println("Invalid bet.");
            System.out.printf("Please enter your P-dollar bet for round %d (1-5 or 0 for quitting the game): ", round);
            player.getBet();
        }
        player.account -= player.curBet;
    }

    static void showQuitGameMessage() {
    	System.out.printf("Good bye, %s. You played for %d round and have %d P-dollars now.\n", player.name, round, player.account);
    }

    static void showCards() {
    	player.oldCards = computer.dealCards();

    	System.out.printf("Your cards are");
    	for (int i = 0; i < player.oldCards.size(); i++) {
    		switch (i) {
    			case 0:
    				System.out.printf(" (a)");
    				break;
    			case 1:
    				System.out.printf(" (b)");
    				break;
    			case 2:
    				System.out.printf(" (c)");
    				break;
    			case 3:
    				System.out.printf(" (d)");
    				break;
    			case 4:
    				System.out.printf(" (e)");
    				break;
    			default:
    				System.out.printf("Invalid card length");
    				break;
    		}
    		System.out.printf(" %s", player.oldCards.get(i).toString());
    	}
    	System.out.printf("\n");
    }

    static void getKeepingCards() {
    	System.out.printf("Which cards do you want to keep? ");
    	player.getKeepingCards();
    }

    static void showDiscardCards() {
    	System.out.printf("Okay. I will discard");
    	for (int i = 0; i < player.discardCardsID.size(); i++) {
    		System.out.printf(" (");
    		System.out.printf("%c", player.discardCardsID.get(i));
    		System.out.printf(")");
    		System.out.printf(" %s", player.oldCards.get(i).toString());
    	}
    	System.out.printf(".\n");
    }

    static void showNewCards() {
    	player.newCards = computer.redealCards(player.newCards);

    	System.out.printf("Your new cards are");
    	for (int i = 0; i < player.newCards.size(); i++)
    		System.out.printf(" %s",player.newCards.get(i).toString());
    	System.out.printf(".\n");
    }

    static void showHandAndPayoff() {
    	String hand = computer.calculateHand(player.newCards);
    	int payoff = isRoyalFlushAnd5P(hand) ? 4000 : payoffTable.get(hand) * player.curBet;

//        statusChecker.pushStatus(player.account, player.curBet, hand, payoff);
    	player.account += payoff;
//        System.out.println(statusChecker.isCorrect(player.account));

    	round++;
    	System.out.printf("You get a %s hand. The payoff is %d.\n", hand, payoff);
    }

    static boolean isRoyalFlushAnd5P(String hand) {
        return hand == "royal flush" && player.curBet == 5;
    }

    /*
    //method used for correctness testing
    static void testHandCreator(int testSize) {
        ArrayList<Card> cs;

        System.out.printf("Royal Flush\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.royalFlush();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nStraight Flush\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.straightFlush();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nFour Of A Kind\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.fourOfAKind();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nFull House\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.fullHouse();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nFlush\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.flush();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nStraight\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.straight();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nThree Of A Kind\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.threeOfAKind();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nTwo Pair\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.twoPair();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }

        System.out.printf("\nJacks Or Better\n");  
        for (int i = 0; i < testSize; i++) {
            cs = handCreator.jacksOrBetter();
            for (int j = 0; j < cs.size(); j++)
                System.out.printf("%s ",cs.get(j).toString());
            System.out.printf(" %s\n", computer.calculateHand(cs));
        }
    }*/
}
