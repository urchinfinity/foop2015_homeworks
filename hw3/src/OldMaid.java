import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public abstract class OldMaid {
	
	int CARDS_NUM;
	int[] CARDS;
	int[] cardsDealer;

	final int ID_TO = 0;
	final int ID_FROM = 1;
	final int PLAYERS_NUM = 4;

	Shuffler shuffler;
	Player[] players;

	ArrayList<Integer> playersList;
	ArrayList<Integer> winnersBuf;

	int joker = -1;
	boolean debugModeOn = false;
	Debugger debugger;

	public OldMaid() {
        players = new Player[PLAYERS_NUM];
    	playersList = new ArrayList<Integer>();
    	winnersBuf = new ArrayList<Integer>();

		initPlayers();
		initCards();

		debugger = new Debugger(CARDS_NUM, joker);
	}

    public abstract void initPlayers();
    public abstract void initCards();

    public void start() {
		fillplayersID();
 		shuffleCards();
 		dealCards();
 		dropCards();
 		initPlayersList();
 		startGame();
    }

    public void fillplayersID() {
    	int pos = 0;
    	for (int i = 0; i < PLAYERS_NUM; i++) {
    		for (int j = 0; j < players[i].cardNum; j++) {
    			cardsDealer[pos++] = i;
    		}
    	}
    }

    public void shuffleCards() {
		shuffler = new Shuffler(52);
		shuffler.shuffleGivenIndex(cardsDealer);
	}

	public void dealCards() {
		System.out.println("Deal cards");

		for (int i = 0; i < CARDS_NUM; i++) {
			Card card = new Card(CARDS[i]);
			players[shuffler.getNext()].cards.add(card);
		}

		for (int i = 0; i < PLAYERS_NUM; i++)
			printPlayerCards(i);

		if (debugModeOn) {
			debugger.initCards();
			for (int i = 0; i < PLAYERS_NUM; i++)
				debugger.addCards(players[i].cards);
			System.out.println("Checking dealing cards correctness: " + debugger.isDealCardsCorrect());
		}
	}

	public void dropCards() {
		System.out.println("Drop cards");

		for (int i = 0; i < PLAYERS_NUM; i++)
			players[i].dropCards();

		for (int i = 0; i < PLAYERS_NUM; i++)
			printPlayerCards(i);

		if (debugModeOn) {
			debugger.initCards();
			for (int i = 0; i < PLAYERS_NUM; i++)
				debugger.addCards(players[i].cards);
			System.out.println("Checking dropping cards correctness: " + debugger.isDropCardsCorrect());
		}
	}

	public void initPlayersList() {
		for (int i = 0; i < PLAYERS_NUM; i++) {
			//check if the player has no cards already before starting game
			if (players[i].cardNum == 0) {
				setWinnerStatus(i);
			} else {
				playersList.add(i);
			}
		}
	}

	public void startGame() {
		System.out.println("Game start");
		printWinner();

		debugger.complementCards();
		//game stops when there remains only one player
		while (playersList.size() > 1) {

			int playerTo = playersList.get(ID_TO);
			int playerFrom = playersList.get(ID_FROM);

			//draw a card
	    	Random rand = new Random();
      		Card drawCard = players[playerFrom].cards.get(rand.nextInt(players[playerFrom].cardNum));
      		players[playerTo].insertnDropCard(drawCard);
      		players[playerFrom].dropCard(drawCard);
      		System.out.println("Player" + playerTo + " draws a card from Player" + playerFrom + " " + drawCard.toString());
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

      		if (debugModeOn && players[playerTo].droppedCards.size() == 2) {
				debugger.addCards(players[playerTo].droppedCards);
				System.out.println("Checking dropping pair cards correctness: " + 
								   debugger.isPairCardsCorrect(players[playerTo].droppedCards.get(0), 
								   							   players[playerTo].droppedCards.get(1)));
			}
		}

  		if (debugModeOn) {
			for (int i = 0; i < PLAYERS_NUM; i++)
				debugger.addCards(players[i].cards);
			System.out.println("Checking game over correctness: " + debugger.isGameOverCorrect());
		}
	}

	public void setWinnerStatus(int id) {
		if (winnersBuf.size() == 0 || winnersBuf.get(0) < id)
			winnersBuf.add(id);
		else
			winnersBuf.add(0, id);
	}

	public void printPlayerCards(int id) {
		System.out.print("Player" + id + ":");
		for (int i = 0; i < players[id].cardNum; i++) {
			System.out.print(" " + players[id].cards.get(i).toString());
		}
		System.out.print("\n");
	}

	public void printWinner() {
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
	}
}