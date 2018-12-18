package uk.ac.chester;


public class Main {

    public static void main(String[] args) {

        /* DO NOT MODIFY THIS FILE - all code should be written in Problems.java */

        System.out.println("Do not run the programme, instead, run the unit tests in ProblemsTest.java");



        Main.findType(true);

    }


    public static void findType(Object o){
        System.out.println(o.getClass().getTypeName());
    }
}
