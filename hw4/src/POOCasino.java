import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import foop.*;
import java.util.Scanner;

public class POOCasino {

	static final String AUTHOR_ID 	= "b01902080";
	static final String AUTHOR_NAME = "Yu-Ching Wang";
    static final int    PLAYER_NUM  = 4;

	static Player[] players;
    static boolean[] isBroke;
    static int[] insurances;
    static boolean[] surrenders;
    static ArrayList<Integer> bets;
    static ArrayList<Hand> hands;
    static ArrayList<Hand> currentTable;
    static ArrayList<Card> holes;
    static ArrayList<Byte> handValues;
    static ArrayList<Integer> indexMatcher;

	static int curRound, nRound;
    static int nChip;

    static Shuffler shuffler;

	public static void main(String[] args) {
System.out.printf("\nPOOCasino Blackjack, written by %s %s.\n\n", AUTHOR_ID, AUTHOR_NAME);

        initVariables();
        parseCommands(args);

//----------------------------test start--------------------------------------------------
        // testPlayerActions();
//----------------------------test end--------------------------------------------------


        while(curRound++ < nRound) {
        // new Scanner(System.in).nextLine();

System.out.printf("####### Round %d #######\n\n", curRound);

            clearVariables();
            shuffleOneDeck();

            askBets();

            if (indexMatcher.size() == 0) {
System.out.printf("All players are out of the game.\n");
                break;
            }

            assignInitialCards();
            assignCardsbyCommands(args);

            if (isDealerFaceUpACE())
                askInsurances();

            if (!isDealerBlackjackWithHoleCard()) {
                askSurrenders();

                for (int i = 0; i < indexMatcher.size(); i++) {
System.out.printf(">> Player%d's turn <<\n", indexMatcher.get(i)+1);
                    //skip the player who surrenders
                    if (isSurrender(indexMatcher.get(i)))
                        continue;

                    flipUpFaceDownCard(i);

                    Card card1 = hands.get(i).getCards().get(0);
                    Card card2 = hands.get(i).getCards().get(1);
                    if (isEqualFaceValue(card1, card2) && isFirstSplit(i)) {
                        if (askSplit(i, indexMatcher.get(i))) {
                            i--;
                            continue;
                        }
                    }

                    if (!askDoubleDown(i, indexMatcher.get(i))) {
                        boolean isHit = true;
                        while(calculateHand(hands.get(i)) <= 21 && isHit) {
                            isHit = askHit(i, indexMatcher.get(i));
                        }
System.out.println("[Stand]");
                    }
System.out.println();
                }

System.out.println(">> Dealer's turn <<");
                flipUpDealerFaceDownCard();
                if (!isDealerFitStand())
                    hitDealer();
System.out.println("[Stand]\n");
            } else {
                //show dealer Blackjack message
System.out.println("Dealer gets a Blackjack <3 <3 <3");
                for (int i = 0; i < indexMatcher.size(); i++) {
                    flipUpFaceDownCard(i);
                }
                flipUpDealerFaceDownCard();
System.out.println();
            }
            compareHands();
            showRemaining();
        }
System.out.println("Blackjack game over. The result is as below:\n");
        showRemaining();
System.out.println("Thank you for your playing :)\nSee you next time.\n");
    }

    static void initVariables() {
        shuffler = new Shuffler();
        players = new Player[PLAYER_NUM];
        isBroke = new boolean[PLAYER_NUM];
        insurances = new int[PLAYER_NUM];
        surrenders = new boolean[PLAYER_NUM];
        bets = new ArrayList<Integer>();
        hands = new ArrayList<Hand>();
        currentTable = new ArrayList<Hand>();
        holes = new ArrayList<Card>();
        handValues = new ArrayList<Byte>();
        indexMatcher = new ArrayList<Integer>();

        curRound = 0;
        for (int i = 0; i < PLAYER_NUM; i++)
            isBroke[i] = false;

        clearVariables();
    }

    static void clearVariables() {
        bets.clear();
        holes.clear();
        handValues.clear();
        indexMatcher.clear();

        for (int i = 0; i < PLAYER_NUM; i++) {
            insurances[i] = 0;
            surrenders[i] = false;
        }
    }

    static void parseCommands(String[] commands) {

        // COMMANDS = nRound nChip Player1 Player2 Player3 Player4
        nRound = Integer.parseInt(commands[0]);
        nChip  = Integer.parseInt(commands[1]);

        for (int i = 2; i < PLAYER_NUM + 2; i++) {
            switch (commands[i]) {
                case "PlayerB01902030":
                    players[i-2] = new PlayerB01902030(nChip);
                    break;
                case "PlayerB01902041":
                    players[i-2] = new PlayerB01902041(nChip);
                    break;
                case "PlayerB01902058":
                    players[i-2] = new PlayerB01902058(nChip);
                    break;
                case "PlayerB01902102":
                    players[i-2] = new PlayerB01902102(nChip);
                    break;
                case "PlayerB01902135":
System.out.println("PlayerB01902135 is in the blacklist.");
System.out.println("PlayerB01902080 will replace PlayerB01902135 to be the new player :).\n");
                    players[i-2] = new PlayerB01902080(nChip);
                    break;
                default:
                    players[i-2] = new PlayerB01902080(nChip);
            }
        }

System.out.printf("There will be %d round(s) Blackjack game.\n", nRound);
System.out.printf("Each player starts with initial chips %d.\n\n", nChip);
    }

    static void shuffleOneDeck() {
        shuffler.shuffle();
    }

    static void askBets() {
System.out.println(">> Start betting <<");

        for (int i = 0; i < PLAYER_NUM; i++) {
            //skip broke player
            if (isBroke[i]) {
System.out.printf("Oops! Player%d is broke. >.^\n", i+1);
                continue;
            }

            int bet = players[i].make_bet(hands, PLAYER_NUM, i);
            if (bet < 0) {
System.out.printf("Player%d gets crazy and makes a negative bet!\n", i+1);
            } else if (bet == 0) {
System.out.printf("Player%d makes a 0 bet and quits this round.\n", i+1);
            } else if (decreasePlayerChips(i, (double)bet)) {
System.out.printf("Player%d bets %d chip(s).\n", i+1, bet);
                bets.add(bet);
                //add player position to index matcher
                indexMatcher.add((Integer)i);
            } else {
                increasePlayerChips(i, (double)bet);
System.out.printf("Player%d has no money to bet %d chip(s).\n", i+1, bet);
            }
        }
System.out.println();
    }

    static void assignInitialCards() {
System.out.println(">> Assign cards <<");

        //clear hands in last round
        hands.clear();

        //assign players' face-up & face-down cards
        for (int i = 0; i < indexMatcher.size(); i++) {
            //assign player[i]'s face-up card
            Card open = shuffler.getNext();
            // open = new Card((byte)1, (byte)5);
            hands.add(getHand(open));
System.out.printf("Player%d gets a face-up card %s.\n", indexMatcher.get(i)+1, getCardValue(open));

            //assign player[i]'s face-down card
            Card hole = shuffler.getNext();
            // hole = new Card((byte)1, (byte)6);
            holes.add(hole);
        }
        //assign dealer's face-up card
        Card dealerOpen = shuffler.getNext();
        hands.add(getHand(dealerOpen));
System.out.printf("Dealer gets a face-up card %s.\n", getCardValue(dealerOpen));

        //assign dealer's face-down card
        Card dealerHole = shuffler.getNext();
        holes.add(dealerHole);
System.out.println();
    }

    static boolean isDealerFaceUpACE() {
        return hands.get(hands.size()-1).getCards().get(0).getValue() == Card.VALUE_LOWER;
    }

    static void askInsurances() {
System.out.println("Dealer gets an ACE face-up card <3");
        Card dealerOpen = hands.get(hands.size()-1).getCards().get(0);
        for (int i = 0; i < indexMatcher.size(); i++) {
            Card playerOpen = hands.get(i).getCards().get(0);
            if (players[indexMatcher.get(i)].buy_insurance(playerOpen, dealerOpen, getCurrentTable(i))) {
                if (decreasePlayerChips(indexMatcher.get(i), (double)bets.get(i)/2)) {
                    insurances[indexMatcher.get(i)] = bets.get(i);
System.out.printf("Player%d buys an insurance of %d chip(s).\n", indexMatcher.get(i)+1, (int)(bets.get(i)/2));
                } else {
                    increasePlayerChips(indexMatcher.get(i), (double)bets.get(i)/2);
                    //show money not enough message
System.out.printf("Oops! Player%d does not have money to buy an insurance :(\n", indexMatcher.get(i)+1);
                }
            } else
System.out.printf("Player%d does not buy an insurance\n", indexMatcher.get(i)+1);
        }
System.out.println();
    }

    static void askSurrenders() {
System.out.println("Dealer does not get a Blackjack.");
System.out.println(">> ask surrender <<");
        Card dealerOpen = hands.get(hands.size()-1).getCards().get(0);
        for (int i = 0; i < indexMatcher.size(); i++) {
            Card playerOpen = hands.get(i).getCards().get(0);
            surrenders[indexMatcher.get(i)] = players[indexMatcher.get(i)].do_surrender(playerOpen, dealerOpen, getCurrentTable(i));
            if (surrenders[indexMatcher.get(i)])
System.out.printf("Player%d chooses to surrender.\n", indexMatcher.get(i)+1);
        }
System.out.println();
    }

    static void flipUpFaceDownCard(int handPosition) {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(hands.get(handPosition).getCards().get(0));
        hand.add(holes.get(handPosition));
        replaceHands(handPosition, new Hand(hand));
System.out.printf("Player%d gets a hand %s.\n", indexMatcher.get(handPosition)+1, getHandValue(hands.get(handPosition)));
    }

    static void flipUpDealerFaceDownCard() {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(hands.get(hands.size()-1).getCards().get(0));
        hand.add(holes.get(hands.size()-1));
        replaceHands(hands.size()-1, new Hand(hand));
System.out.printf("Dealer gets a hand %s.\n", getHandValue(hands.get(hands.size()-1)));
    }

    static boolean isEqualFaceValue(Card card1, Card card2) {
        byte faceValue1 = card1.getValue() >= 10 ? 10 : card1.getValue();
        byte faceValue2 = card2.getValue() >= 10 ? 10 : card2.getValue();
        return faceValue1 == faceValue2;
   }

   static boolean isFirstSplit(int handPosition) {
        int prevPlayer = handPosition > 0 ? indexMatcher.get(handPosition-1) : -1;
        int currPlayer = indexMatcher.get(handPosition);
        int nextPlayer = handPosition < indexMatcher.size() - 1 ? indexMatcher.get(handPosition+1) : -1;

        return (prevPlayer != currPlayer) && (currPlayer != nextPlayer);
   }

    static boolean askSplit(int handPosition, int playerPosition) {
System.out.printf("Player%d gets a pair.\n", playerPosition+1);
        Card dealerOpen = hands.get(hands.size()-1).getCards().get(0);
        ArrayList<Card> playerOpen = hands.get(handPosition).getCards();
        if (players[playerPosition].do_split(playerOpen, dealerOpen, getCurrentTable(handPosition))) {
            if (decreasePlayerChips(playerPosition, (double)bets.get(handPosition))) {
                //split player's hand to two independent hands
                Hand hand1 = getHand(playerOpen.get(0));
                Hand hand2 = getHand(playerOpen.get(1));
                hands.set(handPosition, hand1);
                hands.add(handPosition+1, hand2);

                //assign face-down card for each hand
                Card hole1 = shuffler.getNext();
                Card hole2 = shuffler.getNext();
                holes.set(handPosition, hole1);
                holes.add(handPosition+1, hole2);

                //add bet & indexMatcher
                bets.add(handPosition+1, bets.get(handPosition));
                indexMatcher.add(handPosition+1, playerPosition);

System.out.printf("Player%d chooses to split.\n\n", playerPosition+1);
                return true;
            } else {
                increasePlayerChips(playerPosition, (double)bets.get(handPosition));
                //show money not enough :(
System.out.printf("Oops! Player%d does not have money to split.\n", playerPosition+1);
            }
        }
System.out.printf("Player%d does not choose to split.\n", playerPosition+1);
        return false;
    }

    static boolean askDoubleDown(int handPosition, int playerPosition) {
        Card dealerOpen = hands.get(hands.size()-1).getCards().get(0);
        Hand playerOpen = hands.get(handPosition);
        if (players[playerPosition].do_double(playerOpen, dealerOpen, getCurrentTable(handPosition))) {
            if (decreasePlayerChips(playerPosition, (double)bets.get(handPosition))) {
                bets.set(handPosition, bets.get(handPosition) * 2);
                hitPlayer(handPosition);
System.out.println("[Double Down]");
System.out.printf("Player%d's bet becomes %d chip(s).\n", playerPosition+1, bets.get(handPosition));
System.out.printf("Player%d's hand becomes %s.\n", playerPosition+1, getHandValue(hands.get(handPosition)));
System.out.println("[Stand]");
                return true;
            } else {
                increasePlayerChips(playerPosition, (double)bets.get(handPosition));
                //show money not enough :(
System.out.printf("Oops! Player%d does not have money to double down.\n", playerPosition+1);
            }
        }
        return false;
    }

    static boolean askHit(int handPosition, int playerPosition) {
        Card dealerOpen = hands.get(hands.size()-1).getCards().get(0);
        Hand playerOpen = hands.get(handPosition);
        if (players[playerPosition].hit_me(playerOpen, dealerOpen, getCurrentTable(handPosition))) {    
            hitPlayer(handPosition);
System.out.printf("[Hit] Player%d's hand becomes %s.\n", playerPosition+1, getHandValue(hands.get(handPosition)));
            return true;
        }
        return false;
   }

    static boolean isDealerFitStand() {
        return calculateHand(hands.get(hands.size()-1)) > 16 && !isDealerSoft17();
   }

   static boolean isDealerSoft17() {
        //check if dealer's hand is 17
        byte handValue = calculateHand(hands.get(hands.size()-1));
        if (handValue != 17)
            return false;

        //check if dealer has ACE
        ArrayList<Card> cards = hands.get(hands.size()-1).getCards();
        boolean hasAce = false;
        int acePosition;
        for (acePosition = 0; acePosition < cards.size(); acePosition++) {
            if (cards.get(acePosition).getValue() == Card.VALUE_LOWER) {
                hasAce = true;
                break;
            }
        }

        //check if dealer has soft-17
        if (hasAce) {
            cards.remove(acePosition);
            return calculateHand(new Hand(cards)) == 6;
        }

        return false;
   }

    static void hitPlayer(int handPosition) {
        // hands.get(handPosition).getCards().add(shuffler.getNext());
        Card hitCard = shuffler.getNext();
        Hand newHand = getHand(hands.get(handPosition).getCards(), hitCard);
        replaceHands(handPosition, newHand);
    }

    static void hitDealer() {
        hitPlayer(hands.size()-1);
System.out.printf("[Hit] Dealer's hand becomes %s.\n", getHandValue(hands.get(hands.size()-1)));
    }

    static void compareHands() {
System.out.println(">> Compare hands <<");
        //calculate players' hand values
        for (int i = 0; i < indexMatcher.size(); i++) {
            handValues.add(calculateHand(hands.get(i)));
System.out.printf("Player%d: %d", indexMatcher.get(i)+1, calculateHand(hands.get(i)));
            if (isBlackjack(i))
System.out.printf(" (Blackjack)");
System.out.println();
        }

        //calculate dealer's hand value
        handValues.add(calculateHand(hands.get(hands.size()-1)));
System.out.printf("Dealer: %d", calculateHand(hands.get(hands.size()-1)));
            if (isDealerBlackjack())
System.out.printf(" (Blackjack)");
System.out.println("\n");

        /**
         * isSurrender(int playerPosition)
         * isBusted(int handPosition)
         * isBlackjack(int handPosition)
         * hasInsurance(int playerPosition)
         *
         **/

        byte dealerHandValue = handValues.get(handValues.size()-1);
        boolean isDealerBusted = isBusted(hands.size()-1);

        for (int i = 0; i < indexMatcher.size(); i++) {
            int playerPosition = indexMatcher.get(i);
System.out.printf("[Player%d] ", playerPosition+1);
            if (isSurrender(playerPosition)) {
                increasePlayerChips(playerPosition, (double)bets.get(i)/2);
                //show surrender message
System.out.printf("Player%d surrenders. %d chip(s) goes to the casino.\n", playerPosition+1, bets.get(i)/2);
            } else if (isBusted(i)) {
                //show busted message
System.out.printf("Player%d gets busted. %d chip(s) goes to the casino.\n", playerPosition+1, bets.get(i));
            } else if (isBlackjack(i)) {
                if (isDealerBlackjack()) {
                    increasePlayerChips(playerPosition, (double)bets.get(i));
                    //show Blackjack & push message
System.out.printf("Player%d gets a Blackjack. Push.\n", playerPosition+1);
                } else {
                    increasePlayerChips(playerPosition, (double)bets.get(i)*5/2);
                    //show Blackjack & win message
System.out.printf("Player%d gets a Blackjack. %d chip(s) goes to the player.\n", playerPosition+1, bets.get(i)*3/2);
                }
            } else {
                if (isDealerBusted) {
                    increasePlayerChips(playerPosition, (double)bets.get(i)*2);
                    //show dealer busted message
System.out.printf("Dealer gets busted. %d chip(s) goes to the player.\n", bets.get(i));
                } else if (isDealerBlackjack()) {
                    //show dealer Blackjack message
System.out.printf("Dealer gets a Blackjack. %d chip(s) goes to the casino.\n", bets.get(i));
                } else {
                    byte playerHandValue = handValues.get(i);
                    if (playerHandValue > dealerHandValue) {
                        increasePlayerChips(playerPosition, (double)bets.get(i)*2);
                        //show more values message
System.out.printf("Player%d gets more face values. %d chip(s) goes to the player.\n", playerPosition+1, bets.get(i));
                    } else if (playerHandValue == dealerHandValue) {
                        increasePlayerChips(playerPosition, (double)bets.get(i));
                        //show push message
System.out.printf("Player%d gets the same face values as dealer. Push.\n", playerPosition+1);
                    } else if (playerHandValue < dealerHandValue) {
                        //show less values message
System.out.printf("Dealer gets more face values. %d chip(s) goes to the casino.\n", bets.get(i));
                    }
                }
            }
        }
System.out.println();

System.out.println(">> Check insurance <<");
        for (int i = 0; i < PLAYER_NUM; i++) {
            if (hasInsurance(i)) {
                if (isDealerBlackjack()) {
                    increasePlayerChips(i, (double)insurances[i]);
                    //show insurance message
System.out.printf("Player%d buys an insurance. %d chip(s) goes to the player.\n", i+1, insurances[i]);
                }
            }
        }
System.out.println();
    }

    static void showRemaining() {
        for (int i = 0; i < PLAYER_NUM; i++)
            System.out.printf("Player%d has %s in hand.\n", i+1, players[i].toString());
System.out.println();
    }

    static Hand getHand(Card card) {
        return new Hand(new ArrayList<Card>(Arrays.asList(card)));
    }

    static Hand getHand(ArrayList<Card> curCards, Card newCard) {
        ArrayList<Card> newCards = new ArrayList<Card>();
        newCards.addAll(curCards);
        newCards.add(newCard);
        return new Hand(newCards);
    }

    static ArrayList<Hand> getCurrentTable(int removedHandPosition) {
        ArrayList<Hand> currentTable = new ArrayList<Hand>();

        for (int i = 0; i < indexMatcher.size(); i++) {
            if (i != removedHandPosition)
                currentTable.add(hands.get(i));
        }
        return currentTable;
    }

    static void replaceHands(int handPosition, Hand hand) {
        hands.remove(handPosition);
        hands.add(handPosition, hand);
    }

    static byte calculateHand(Hand hand) {
        ArrayList<Card> cards = hand.getCards();
        byte handValue = 0;
        int aceNum = 0;
        for (int i = 0; i < cards.size(); i++) {
            byte value = cards.get(i).getValue();
            switch(value) {
                case Card.VALUE_LOWER:
                    handValue += 11;
                    aceNum++;
                    break;
                case Card.VALUE_UPPER:
                case Card.VALUE_UPPER-1:
                case Card.VALUE_UPPER-2:
                    handValue += 10;
                    break;
                default:
                    handValue += value;
            }
        }
        while (handValue > 21 && aceNum > 0) {
            handValue -= 10;
            aceNum--;
        }
        return handValue;
    }

    static boolean isSurrender(int playerPosition) {
        return surrenders[playerPosition];
    } 

    static boolean isBusted(int handPosition) {
        return handValues.get(handPosition) > 21;
    }

    static boolean isBlackjack(int handPosition) {
        return handValues.get(handPosition) == 21 && hands.get(handPosition).getCards().size() == 2;
    }

    static boolean isDealerBlackjackWithHoleCard() {
        boolean hasAce = false, hasTen = false;
        byte openCardValue = hands.get(hands.size()-1).getCards().get(0).getValue();
        byte holeCardValue = holes.get(holes.size()-1).getValue();
        if (openCardValue == Card.VALUE_LOWER || holeCardValue == Card.VALUE_LOWER)
            hasAce = true;
        if (openCardValue >= Card.VALUE_UPPER-3 || holeCardValue >= Card.VALUE_UPPER-3)
            hasTen = true;
        return hasAce && hasTen;
    }

    static boolean isDealerBlackjack() {
        return calculateHand(hands.get(hands.size()-1)) == 21 && hands.get(hands.size()-1).getCards().size() == 2;
    }

    static boolean hasInsurance(int playerPosition) {
        return insurances[playerPosition] > 0;
    }

    static boolean increasePlayerChips(int playerPosition, double bet) {
        try {
            players[playerPosition].increase_chips(bet);
        } catch (Player.NegativeException ex) {
            showBrokenMessage(playerPosition, ex);
            return false;
        }
        return true;
    }

    static boolean decreasePlayerChips(int playerPosition, double bet) {
        try {
            players[playerPosition].decrease_chips(bet);
        } catch (Player.BrokeException ex) {
            showBrokenMessage(playerPosition, ex);
            return false;
        } catch (Player.NegativeException ex) {
            showBrokenMessage(playerPosition, ex);
            return false;
        }
        return true;
    }

    static void showBrokenMessage(int playerPosition, Exception ex) {
        System.out.printf("Oops! Player%d is b%s\n", playerPosition+1, ex.getMessage().toString().substring(1));
    }

    static String getCardValue(Card card) {
        String cardSuit = "";
        switch (card.getSuit()) {
            case Card.CLUB:
                cardSuit = "C";
                break;
            case Card.DIAMOND:
                cardSuit = "D";
                break;
            case Card.HEART:
                cardSuit = "H";
                break;
            default:
                cardSuit = "S";
        }

        int cardValue = card.getValue();
        if (cardValue == 1)
            return cardSuit + "A";
        else if (cardValue <= 10)
            return cardSuit + cardValue;
        else if (cardValue == 11)
            return cardSuit + "J";
        else if (cardValue == 12)
            return cardSuit + "Q";
        else
            return cardSuit + "K";
    }

    static String getHandValue(Hand hand) {
        String handValue = "";
        ArrayList<Card> cards = hand.getCards();
        for (int i = 0; i < cards.size(); i++)
            handValue += " " + getCardValue(cards.get(i));
        return handValue.substring(1);
    }

    static void testPlayerActions() {

        int testBet = players[0].make_bet(hands, 4, 0);
System.out.println(testBet);

        byte suit = Card.SPADE;
        byte rank = Card.VALUE_LOWER;
        ArrayList<Card> testSplitCards = new ArrayList<Card>();
        testSplitCards.add(new Card(suit, rank));
        testSplitCards.add(new Card((byte)(suit+1), rank));

        ArrayList<Card> testDoubleCards = new ArrayList<Card>();
        testDoubleCards.add(new Card(suit, (byte)(rank+4)));
        testDoubleCards.add(new Card((byte)(suit+1), (byte)(rank+5)));

        ArrayList<Card> testHitCards1 = new ArrayList<Card>();
        testHitCards1.add(new Card(suit, rank));
        testHitCards1.add(new Card((byte)(suit+1), (byte)(rank+6)));

        ArrayList<Card> testHitCards2 = new ArrayList<Card>();
        testHitCards2.add(new Card(suit, (byte)(rank+6)));
        testHitCards2.add(new Card((byte)(suit+1), (byte)(rank+4)));

        Card dealerOpen = new Card((byte)(suit+2), rank);

        System.out.println(players[0].do_split(testSplitCards, dealerOpen, hands));
        System.out.println(players[0].do_double(new Hand(testDoubleCards), dealerOpen, hands));
        System.out.println(players[0].buy_insurance(new Card(suit, rank), dealerOpen, hands));
        System.out.println(players[0].do_surrender(new Card(suit, (byte)(rank+6)), dealerOpen, hands));
        System.out.println(players[0].hit_me(new Hand(testHitCards1), dealerOpen, hands));
        System.out.println(players[0].hit_me(new Hand(testHitCards2), dealerOpen, hands));
        // decreasePlayerChips(0, (double)1000);
    }

    static void assignCardsbyCommands(String[] commands) {
        for (int i = 6; i < commands.length; i++) {
            String openCardSuit = commands[i].substring(2, 3);
            String openCardValue = commands[i].substring(3, 4);
            String holeCardSuit = commands[i].substring(4, 5);
            String holeCardValue = commands[i].substring(5, 6);
            byte openSuit, holeSuit, openValue, holeValue;

            switch (openCardSuit) {
                case "C":
                    openSuit = Card.CLUB;
                    break;
                case "D":
                    openSuit = Card.DIAMOND;
                    break;
                case "H":
                    openSuit = Card.HEART;
                    break;
                default:
                    openSuit = Card.SPADE;
            } 

            switch (holeCardSuit) {
                case "C":
                    holeSuit = Card.CLUB;
                    break;
                case "D":
                    holeSuit = Card.DIAMOND;
                    break;
                case "H":
                    holeSuit = Card.HEART;
                    break;
                default:
                    holeSuit = Card.SPADE;
            }

            switch (openCardValue) {
                case "T":
                    openValue = 10;
                    break;
                case "J":
                    openValue = 11;
                    break;
                case "Q":
                    openValue = 12;
                    break;
                case "K":
                    openValue = 13;
                    break;
                case "A":
                    openValue = 1;
                    break;
                default:
                    openValue = (byte)Integer.parseInt(openCardValue);
            }
            switch (holeCardValue) {
                case "T":
                    holeValue = 10;
                    break;
                case "J":
                    holeValue = 11;
                    break;
                case "Q":
                    holeValue = 12;
                    break;
                case "K":
                    holeValue = 13;
                    break;
                case "A":
                    holeValue = 1;
                    break;
                default:
                    holeValue = (byte)Integer.parseInt(holeCardValue);
            }

            int playerPosition;
            if (commands[i].substring(1, 2).equals("d"))
                playerPosition = hands.size()-1;
            else
                playerPosition = Integer.parseInt(commands[i].substring(1, 2)) - 1;

            hands.set(playerPosition, getHand(new Card(openSuit, openValue)));
            holes.set(playerPosition, new Card(holeSuit, holeValue));
        }
    }
}
