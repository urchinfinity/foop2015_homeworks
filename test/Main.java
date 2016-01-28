import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class Main {
	public static void main(String[] args) {
		// Test test = new Test() {
		// 	@Override
		// 	public void myPrint(int a) {
  //   			System.out.println(a);
		// 	}
		// };
  //       test.myPrint(1==1 ? 1 : 2);

		ArrayList<Integer> original = new ArrayList<Integer>();
		original.add(1);
		original.add(2);
		original.add(3);
		original.add(4);
		original.add(5);
		ArrayList<Integer> copy = new ArrayList<Integer>();
		copy = original;
		copy.remove(3);
		System.out.println(original);
		System.out.println(copy);
    }
}