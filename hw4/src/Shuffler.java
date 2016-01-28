import foop.*;

public class Shuffler {

	static int SIZE = 52;

    public int[] index;
    public int count;

    //ACTIONS:
    public Shuffler() {
		index = new int[SIZE];
    }

    public void shuffle() {
	    initializeIndex();
	    permuteIndex();
	    count = 0;
    }
    
    public Card getNext() {
    	int curIndex = index[count++];
    	byte suit = (byte)(curIndex / Card.VALUE_UPPER + 1);
    	byte rank = (byte)(curIndex % Card.VALUE_UPPER + 1);
		return new Card(suit, rank);
    }
    
    private void initializeIndex() {
		for(int i = 0; i < index.length; i++)
		    index[i] = i;
    }

    private void permuteIndex() {
		java.util.Random rnd = new java.util.Random();
		for(int i = index.length - 1; i >= 0; i--) {
		    int j = rnd.nextInt(i+1);
		    int tmp = index[j];
		    index[j] = index[i];
		    index[i] = tmp;
		}
    }
}