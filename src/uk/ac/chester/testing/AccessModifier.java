package uk.ac.chester.testing;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Our own enum to represent access modifiers (prevents dependent classes having to import reflection package)
 * Note: PACKAGE_PRIVATE is represented by the absence of a modifier in code
 */
public enum AccessModifier {
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
}