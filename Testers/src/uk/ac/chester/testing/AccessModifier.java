package uk.ac.chester.testing;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Our own enum to represent access modifiers (prevents dependent classes having to import reflection package)
 * Note: PACKAGE_PRIVATE is represented by the absence of a modifier in code
 */
public enum AccessModifier {
    /** Represents java's private access modifier, i.e. use of the private keyword */
    PRIVATE{
        @Override
        public String toString() {
            return "private";
        }
    },
    /** Represents java's protected modifier, i.e. use of the protected keyword */
    PROTECTED{
        @Override
        public String toString() {
            return "protected";
        }
    },
    /** Represents java's public access modifier, i.e. use of the public keyword */
    PUBLIC {
        @Override
        public String toString() {
            return "public";
        }
    },
    /** Represents java's package-private access modifier, i.e. omission of any access modifier keyword */
    PACKAGE_PRIVATE {
        @Override
        public String toString() {
            return "package-private (i.e. no modifier)";
        }
    };

    /**
     * Returns our own type of access modifier so dependent classes don't need to import the reflection package
     * @param member a class {@link Member} such as a field, method or constructor
     * @return An AccessModifier enum value
     */
    public static AccessModifier accessModifier(Member member){
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
}