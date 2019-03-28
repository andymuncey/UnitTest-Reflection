package uk.ac.chester.tasks.strings;

import java.util.Scanner;

public class TrimWithEllipsis {


    //Get a String from the user, if it has more than 30 characters, take the first 27, add an ellipsis (three full stops) and print that, otherwise print the whole String
    public static void main(String[] args) {
        System.out.println("Please type a string");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(input.length() > 30 ? input.substring(0,27)+"..." : input);
    }
}
