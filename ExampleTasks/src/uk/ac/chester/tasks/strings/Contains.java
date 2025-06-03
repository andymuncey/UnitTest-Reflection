package uk.ac.chester.tasks.strings;

import java.util.Scanner;

//Get a String from the user, check to see if it contains the words: Java, code or String (using any combination of case such as jAvA),
// and if it contains at least one of them, print the String, otherwise ask for a new String - you will likely need to use a loop to do this, but it may be easier to verify the checking works before adding the loop.

public class Contains {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("Type the phrase");
            input = scanner.nextLine();
        } while (!(input.toLowerCase().contains("java") || input.toLowerCase().contains("code") || input.toLowerCase().contains("string")));
        System.out.println(input);
    }

}
