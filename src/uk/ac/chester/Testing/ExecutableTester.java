package uk.ac.chester.Testing;

import java.util.Arrays;

public interface ExecutableTester {

    default boolean validParamName(String name){
        //as per conventions at: https://www.oracle.com/technetwork/java/codeconventions-135099.html
        String[] throwAwayVarNames = {"c", "d", "e", "i", "j", "k", "m", "n" }; //keep in order or sort before using binary search
        boolean atLeastTwoChars = name.length() >= 2;
        boolean validThrowAwayVarName = Arrays.binarySearch(throwAwayVarNames,name) >= 0;
        return validThrowAwayVarName || (atLeastTwoChars && startsWithLowerChar(name));
    }

    default boolean startsWithLowerChar(String text){
        if (!text.isEmpty()){
            char firstLetter = text.charAt(0);
            return firstLetter >= 'a' && firstLetter <= 'z';
        }
        return false;
    }

    /**
     * Note: PACKAGE_PRIVATE is represented by the absence of a modifier in code
     */
    enum AccessModifier {
        PRIVATE{
            @Override
            public String toString() {
                return "private";
            }
        }, PROTECTED{
            @Override
            public String toString() {
                return "protected";
            }
        }, PUBLIC {
            @Override
            public String toString() {
                return "public";
            }
        }, PACKAGE_PRIVATE {
            @Override
            public String toString() {
                return "package-private (i.e. no modifier";
            }
        };



    }
}
