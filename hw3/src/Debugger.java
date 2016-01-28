import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Debugger {

	int CARDS_NUM;
	int[] CARDS;
    int jokerID;
    int offset;

	public Debugger(int num, int joker) {
        CARDS_NUM = num;
        CARDS = new int[CARDS_NUM];
        jokerID = joker;
        offset = hasTargetJoker() ? 2 : 0;
	}

    public void initCards() {
        for (int i = 0; i < CARDS_NUM; i++)
            CARDS[i] = 0;
    }

    public void addCards(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            CARDS[cardIDtoIndex(cards.get(i))] = 1;
        }
    }

    public void complementCards() {
        for (int i = 0; i < CARDS_NUM; i++) {
            CARDS[i] = (CARDS[i] + 1) % 2;
        }
    }

    public boolean isDealCardsCorrect() {
        for (int i = 0; i < CARDS_NUM; i++) {
            if (CARDS[i] == 0)
                return false;
        }
        return true;
    }

    public boolean isDropCardsCorrect() {
        for (int i = 0; i < CARDS_NUM; i++) {
            //first card of the dropping pairs
            if (CARDS[i] == 0) {
                boolean foundSecond = false;
                for (int j = i + 1; j < CARDS_NUM; j++) {
                    //second card of the dropping pairs
                    if (CARDS[j] == 0) {
                        if (isPairCardsCorrect(i, j)) {
                            i = j;
                            foundSecond = true;
                            break;
                        } else
                            return false;
                    }
                }
                if (!foundSecond)
                    return false;
            }
        }
        return true;
    }

    public boolean isPairCardsCorrect(Card card1, Card card2) {
        return isPairCardsCorrect(cardIDtoIndex(card1), cardIDtoIndex(card2));
    }

    public boolean isGameOverCorrect() {
        return isDealCardsCorrect();
    }

    private boolean isPairCardsCorrect(int card1Index, int card2Index) {
        int alignedCard1Index;
        if (hasTargetJoker())
            alignedCard1Index = card1Index > jokerID - offset ? card1Index + 1 : card1Index;
        else
            alignedCard1Index = card1Index - 2;

        int validRange = 3 - alignedCard1Index % 4;
        if (hasTargetJoker() && (alignedCard1Index / 4) == ((jokerID - offset) / 4) && (alignedCard1Index % 4) < ((jokerID - offset) % 4))
            validRange--;
        return card2Index < card1Index + validRange + 1;
    }

    private boolean hasTargetJoker() {
        return jokerID != -1;
    }

    public int cardIDtoIndex(Card card) {
        int index = card.id;
        if (hasTargetJoker() && index > jokerID)
            index--;
        return index - offset;
    }
}