package uk.ac.chester.tasks.strings;

import java.util.Scanner;

public class Filter {


    //ask the user to type a line of text, if the text contains the words, "java", "code" or "string" (using any combination of casing, such as JaVa), do nothing.
    //if none of the words are found in the user's input, print the input back out
    public static void main(String[] args) {

        String[] banned = {"java", "code", "string"};

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean filter = false;
        for (String word : banned){
            if (input.toLowerCase().contains(word)){
                filter = true;
                break;
            }
        }
        if (!filter){
            System.out.println(input);
        }

    }

}
