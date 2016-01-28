

public class Shuffler {

    public int[] index;
    public int count;

    //ACTIONS:
    public Shuffler(int size) {
		index = new int[size];
    }

    public void shuffle() {
	    initializeIndex();
	    permuteIndex();
	    count = 0;
    }

   public void shuffleGivenIndex(int[] givenIndex) {
		index = new int[givenIndex.length];

		for(int i = 0; i < givenIndex.length; i++)
		    index[i] =  givenIndex[i];
		
	    permuteIndex();
	    count = 0;
    }
    
    public int getNext() {
		return index[count++];
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