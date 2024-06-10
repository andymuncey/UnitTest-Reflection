package uk.ac.chester.testing;

import java.util.Arrays;

public interface ConventionChecker {

    /**
     * Indicates whether a variable name is valid as per the conventions at: <a href="https://www.oracle.com/technetwork/java/codeconventions-135099.html">...</a>
     * @param name a string representing the name of a variable
     * @return true or false
     */
    default boolean validVariableName(String name){
        String[] throwAwayVarNames = {"c", "d", "e", "i", "j", "k", "m", "n" }; //keep in order, or sort before using binary search
        boolean atLeastTwoChars = name.length() >= 2;
        boolean validThrowAwayVarName = Arrays.binarySearch(throwAwayVarNames,name) >= 0;
        return validThrowAwayVarName || (atLeastTwoChars && startsWithLowerChar(name));
    }

    default boolean validMethodName(String name){
        return startsWithLowerChar(name) && name.length() >= 2;
    }

    /**
     * Verifies that the String matches the convention for a class constant (i.e. UPPERCASE_WITH_UNDERSCORES)
     * As per the conventions at: <a href="https://www.oracle.com/technetwork/java/codeconventions-135099.html">...</a>
     * @param name the String to testExistence
     * @return true if all characters are either a capital or underscore and there are at least two characters in the string
     */
    default boolean validClassConstantName(String name){
        if(name.length() < 2){
            return false;
        }
        for (char c : name.toCharArray()){
            if (!(Character.isUpperCase(c) || c == '_')){
                return false;
            }
        }
        //doesn't start or end in underscore
        return name.charAt(0) != '_' && name.charAt(name.length() - 1) != '_';
    }

    /**
     * Determines if the first character of a String is lowercase
     * @param text a string to check
     * @return true if the first letter is lowercase
     */
    default boolean startsWithLowerChar(String text){
        return !text.isEmpty() && Character.isLowerCase(text.charAt(0));
    }

}
