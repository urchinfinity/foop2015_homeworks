import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class StatusChecker {

	static Map<String, Integer> payoffTable;
	ArrayList<Integer> accounts;
	ArrayList<Integer> bets;
	ArrayList<String> hands;
	ArrayList<Integer> payoffs;

	int count;

	public StatusChecker() {
		payoffTable = new HashMap<String, Integer>();
		accounts = new ArrayList<Integer>();
		bets = new ArrayList<Integer>();
		hands = new ArrayList<String>();
		payoffs = new ArrayList<Integer>();
		count = 0;

		initPayoffTable();
	}

	public void initPayoffTable() {
    	payoffTable.put(    "royal flush1",  250);
    	payoffTable.put(    "royal flush2",  500);
    	payoffTable.put(    "royal flush3",  750);
    	payoffTable.put(    "royal flush4", 1000);
    	payoffTable.put(    "royal flush5", 4000);
    	payoffTable.put( "straight flush1",   50);
    	payoffTable.put( "straight flush2",  100);
    	payoffTable.put( "straight flush3",  150);
    	payoffTable.put( "straight flush4",  200);
    	payoffTable.put( "straight flush5",  250);
    	payoffTable.put( "four of a kind1",   25);
    	payoffTable.put( "four of a kind2",   50);
    	payoffTable.put( "four of a kind3",   75);
    	payoffTable.put( "four of a kind4",  100);
    	payoffTable.put( "four of a kind5",  125);
    	payoffTable.put(     "full House1",    9);
    	payoffTable.put(     "full House2",   18);
    	payoffTable.put(     "full House3",   27);
    	payoffTable.put(     "full House4",   36);
    	payoffTable.put(     "full House5",   45);
    	payoffTable.put(          "flush1",    6);
    	payoffTable.put(          "flush2",   12);
    	payoffTable.put(          "flush3",   18);
    	payoffTable.put(          "flush4",   24);
    	payoffTable.put(          "flush5",   30);
    	payoffTable.put(       "straight1",    4);
    	payoffTable.put(       "straight2",    8);
    	payoffTable.put(       "straight3",   12);
    	payoffTable.put(       "straight4",   16);
    	payoffTable.put(       "straight5",   20);
    	payoffTable.put("three of a kind1",    3);
    	payoffTable.put("three of a kind2",    6);
    	payoffTable.put("three of a kind3",    9);
    	payoffTable.put("three of a kind4",   12);
    	payoffTable.put("three of a kind5",   15);
    	payoffTable.put(       "two pair1",    2);
    	payoffTable.put(       "two pair2",    4);
    	payoffTable.put(       "two pair3",    6);
    	payoffTable.put(       "two pair4",    8);
    	payoffTable.put(       "two pair5",   10);
    	payoffTable.put("Jacks or better1",    1);
    	payoffTable.put("Jacks or better2",    2);
    	payoffTable.put("Jacks or better3",    3);
    	payoffTable.put("Jacks or better4",    4);
    	payoffTable.put("Jacks or better5",    5);
    	payoffTable.put(             "no1",    0);
    	payoffTable.put(             "no2",    0);
    	payoffTable.put(             "no3",    0);
    	payoffTable.put(             "no4",    0);
    	payoffTable.put(             "no5",    0);
	}

	public void pushStatus(int curAccount, int curBet, String curHand, int curPayoff) {
		accounts.add(curAccount);
		bets.add(curBet);
		hands.add(curHand);
		payoffs.add(curPayoff);
	}

	public boolean isCorrect(int nextAccount) {
		int idealPayoff = payoffTable.get(hands.get(count) + bets.get(count));
		int idealAccount = accounts.get(count) + idealPayoff;
		count++;
        
		return idealPayoff == payoffs.get(count-1) && idealAccount == nextAccount;
	}
}