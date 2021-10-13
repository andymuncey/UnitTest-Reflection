package uk.ac.chester.tasks.strings;

import java.util.Scanner;


//    Get a String from the user, then get an int from the user, representing an index in that String. Verify if that character is uppercase or lowercase.
//    Add error handling to the above task, so that if the number exceeds the length of the String, the programme does not crash
public class CaseOfCharacterAtIndex {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int position = scanner.nextInt();

        if (position < input.length()) {
            System.out.println(Character.isUpperCase(input.charAt(position)) ? "Uppercase" : "Lowercase");
        } else {
            System.out.println("invalid position");
        }

    }

}
