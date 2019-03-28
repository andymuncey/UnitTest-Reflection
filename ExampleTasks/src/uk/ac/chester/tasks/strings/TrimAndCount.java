package uk.ac.chester.tasks.strings;

import java.util.Scanner;

public class TrimAndCount {

    //Get a String from the user, remove spaces from the start and end and output how many characters the remaining phrase has
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextLine().trim().length());


    }

}
