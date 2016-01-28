import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

class PlayGame {
	static final String[] CARDS = {
	"R0", "B0", "C2", "D2", "H2", "S2",
	"C3", "D3", "H3", "S3", "C4", "D4", "H4", "S4",
	"C5", "D5", "H5", "S5",	"C6", "D6", "H6", "S6",
	"C7", "D7", "H7", "S7",	"C8", "D8", "H8", "S8",
	"C9", "D9", "H9", "S9","C10","D10","H10","S10",
	"CJ", "DJ", "HJ", "SJ",	"CQ", "DQ", "HQ", "SQ",
	"CK", "DK", "HK", "SK",	"CA", "DA", "HA", "SA"};

	static final int CARDS_NUM = 54;

	static final int ID_TO = 0;
	static final int ID_FROM = 1;

	static int playerNum;
	static Player[] players;

	static int[] cardsDealer;
	static List<Integer> playersList;
	static List<Integer> winnersBuf;

	static boolean isBasicGameOver;

	public static void main(String[] args) {
        initPlayersData();
 		fillplayersID();
 		shuffleCards();
 		dealCards();
 		dropCards();
 		initPlayersList();
 		startGame();
    }

    static void initPlayersData() {
        //initialize assigned data
        playerNum = 4;
        players = new Player[playerNum];
        players[0] = new Player(14);
        players[1] = new Player(14);
        players[2] = new Player(13);
        players[3] = new Player(13);

    	cardsDealer = new int[CARDS_NUM];
    	playersList = new ArrayList<>();
    	winnersBuf = new ArrayList<>();

    	isBasicGameOver = false;
    }

	/** create an array cantaining repeated userID 
	  * [user1 ID, ..., user1 ID, user2 ID, ...]
	  */
    static void fillplayersID() {
    	int pos = 0;
    	for (int i = 0; i < playerNum; i++) {
    		for (int j = 0; j < players[i].cardNum; j++) {
    			cardsDealer[pos++] = i;
    		}
    	}
    }

	/** implement Fisher–Yates shuffle, 
	  * each user would abtain corresponding cards in ascending order
	  */
    static void shuffleCards() {
    	Random rand = new Random();
    	for (int i = CARDS_NUM - 1; i > 0; i--) {
      		int index = rand.nextInt(i + 1);
      		int a = cardsDealer[index];
      		cardsDealer[index] = cardsDealer[i];
      		cardsDealer[i] = a;
    	}
	}

	static void dealCards() {
		System.out.println("Deal cards");

		for (int i = 0; i < CARDS_NUM; i++)
			players[cardsDealer[i]].cards.add(i);

		for (int i = 0; i < playerNum; i++)
			printPlayerCards(i);
	}

	static void dropCards() {
		System.out.println("Drop cards");

		for (int i = 0; i < playerNum; i++)
			players[i].dropCards();

		for (int i = 0; i < playerNum; i++)
			printPlayerCards(i);
	}

	static void initPlayersList() {
		for (int i = 0; i < playerNum; i++) {
			//check if the player has no cards already before starting game
			if (players[i].cardNum == 0) {
				setWinnerStatus(i);
			} else {
				playersList.add(i);
			}
		}
	}

	static void startGame() {
		System.out.println("Game start");
		printWinner();

		//game stops when there remains only one player
		while (playersList.size() > 1) {

			int playerTo = playersList.get(ID_TO);
			int playerFrom = playersList.get(ID_FROM);

			//draw a card
	    	Random rand = new Random();
      		int drawCard = players[playerFrom].cards.get(rand.nextInt(players[playerFrom].cardNum));
      		players[playerTo].insertnDropCard(drawCard);
      		players[playerFrom].dropCard(drawCard);
      		System.out.println("Player" + playerTo + " draws a card from Player" + playerFrom + " " + CARDS[drawCard]);
  			printPlayerCards(playerTo);
  			printPlayerCards(playerFrom);


      		// check playerTo status
      		playersList.remove((Integer)playerTo);
      		if (players[playerTo].cardNum == 0) {
      			setWinnerStatus(playerTo);
      		} else {
      			playersList.add(playerTo);
      		}

      		//check playerFrom status
      		if (players[playerFrom].cardNum == 0) {
      			playersList.remove((Integer)playerFrom);
      			setWinnerStatus(playerFrom);
      		}

      		//print the winner at current round
      		printWinner();
		}
		System.out.println("Bonus game over");
	}

	static void setWinnerStatus(int id) {
		if (winnersBuf.size() == 0 || winnersBuf.get(0) < id)
			winnersBuf.add(id);
		else
			winnersBuf.add(0, id);
	}

	static void printPlayerCards(int id) {
		System.out.print("Player" + id + ":");
		for (int i = 0; i < players[id].cardNum; i++) {
			System.out.print(" " + CARDS[players[id].cards.get(i)]);
		}
		System.out.print("\n");
	}

	static void printWinner() {
		int num = winnersBuf.size();
		if (num > 0) {
			String output = "";
			for (int i = 0; i < num; i++) {
				output += i == 0 ? "Player" + winnersBuf.get(i) : " and Player" + winnersBuf.get(i);
			}
			output += num == 1 ? " wins" : " win";
			System.out.println(output);
			winnersBuf.clear();
		}
		
  		//check if the first winner comes out
  		//rewrite basic gaame tester!!
  		if (!isBasicGameOver) {
  			checkBasicGameStatus();
  		}
	}

	static void checkBasicGameStatus() {
  		if (playersList.size() < playerNum) {
  			isBasicGameOver = true;

  			System.out.println("Basic game over");
  			System.out.println("Continue");
  		}
	}
}