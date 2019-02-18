package uk.ac.chester.testing;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public interface MemberTester {

    /**
     * Indicates whether a variable name is valid as per the conventions at: https://www.oracle.com/technetwork/java/codeconventions-135099.html
     * @param name a string representing the name of a variable
     * @return true or false
     */
    default boolean validVariableName(String name){
        String[] throwAwayVarNames = {"c", "d", "e", "i", "j", "k", "m", "n" }; //keep in order, or sort before using binary search
        boolean atLeastTwoChars = name.length() >= 2;
        boolean validThrowAwayVarName = Arrays.binarySearch(throwAwayVarNames,name) >= 0;
        return validThrowAwayVarName || (atLeastTwoChars && startsWithLowerChar(name));
    }

    /**
     * Verifies that the String matches the convention for a class constant (i.e. UPPERCASE_WITH_UNDERSCORES)
     * As per the conventions at: https://www.oracle.com/technetwork/java/codeconventions-135099.html
     * @param name the String to test
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

    /**
     * Returns our own type of access modifier so dependent classes don't need to import the reflection package
     * @param member a class member such as a field, method or constructor
     * @return An AccessModifier enum value
     */
    static AccessModifier accessModifier(Member member){
        if (Modifier.isPublic(member.getModifiers())){
            return AccessModifier.PUBLIC;
        }
        if (Modifier.isPrivate(member.getModifiers())){
            return AccessModifier.PRIVATE;
        }
        if (Modifier.isProtected(member.getModifiers())){
            return AccessModifier.PROTECTED;
        }
        return AccessModifier.PACKAGE_PRIVATE;
    }

    /**
     * Our own enum to represent access modifiers (prevents dependent classes having to import reflection package)
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
                return "package-private (i.e. no modifier)";
            }
        };

    }
}
