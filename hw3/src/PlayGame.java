import java.util.*;
import java.util.Scanner;

public class PlayGame {
	public static void main(String[] args) {

        System.out.println("Welcome to OldMaid Paradise.");
        System.out.println("Here we offer multiple OldMaids for you to enjoy.");
        boolean quitGame = false;
        while(!quitGame) { 	
	        VariantOne gameOne = new VariantOne();
	        VariantTwo gameTwo = new VariantTwo();

        	System.out.println("(a) VariantOne (b) VariantOne [debugMode] (c) VariantTwo (d) VariantTwo [debugMode]");
        	System.out.print("Please choose your game type (q for quitting the game): ");
        	String gameType = getUserInput();

        	switch (gameType) {
        		case "a":
        			gameOne.debugModeOn = false;
        			gameOne.start();
        			break;
        		case "b":
        			gameOne.debugModeOn = true;
        			gameOne.start();
        			break;
        		case "c":
        			gameTwo.debugModeOn = false;
        			gameTwo.start();
        			break;
        		case "d":
        			gameTwo.debugModeOn = true;
        			gameTwo.start();
        			break;
        		case "q":
        			quitGame = true;
        			System.out.println("Good bye. Hope you enjoy it.");
        			break;
        		default:
        			break;
        	}
        }
    }

	public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
	}
}