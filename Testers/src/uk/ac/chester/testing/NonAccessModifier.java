package uk.ac.chester.testing;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Represents the non-access modifiers of final, static, abstract, synchronised, volatile, transient, native and strictfp
 */
public enum NonAccessModifier {

    /** Represents Java's final non-access modifier, i.e. use of the final keyword */
    FINAL {
        @Override
        public String toString() {
            return "final";
        }
    },
    /** Represents Java's static non-access modifier, i.e. use of the static keyword */
    STATIC {
        @Override
        public String toString() {
            return "static";
        }
    },
    /** Represents Java's abstract non-access modifier, i.e. use of the abstract keyword */
    ABSTRACT {
        @Override
        public String toString() {
            return "abstract";
        }
    },
    /** Represents Java's synchronized non-access modifier, i.e. use of the synchronized keyword */
    SYNCHRONIZED {
        @Override
        public String toString() {
            return "synchronized";
        }
    },
    /** Represents Java's volatile non-access modifier, i.e. use of the volatile keyword */
    VOLATILE {
        @Override
        public String toString() {
            return "volatile";
        }
    },
    /** Represents Java's transient non-access modifier, i.e. use of the transient keyword */
    TRANSIENT {
        @Override
        public String toString() {
            return "transient";
        }
    },
    /** Represents Java's native non-access modifier, i.e. use of the native keyword */
    NATIVE {
        @Override
        public String toString() {
            return "native";
        }
    },
    /** Represents Java's strictfp non-access modifier, i.e. use of the strictfp keyword */
    STRICTFP {
        @Override
        public String toString() {
            return "strictfp";
        }
    };

    /**
     * Returns all non access modifiers for a member (e.g. field, method)
     * @param member the class {@link Member} to check
     * @return a Set of access modifiers
     */
    public static Set<NonAccessModifier> nonAccessModifiers(Member member) {
        Set<NonAccessModifier> nonAccessModifiers = new HashSet<>();

        int modifiers = member.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            nonAccessModifiers.add(STATIC);
        }
        if (Modifier.isFinal(modifiers)) {
            nonAccessModifiers.add(FINAL);
        }
        if (Modifier.isAbstract(modifiers)) {
            nonAccessModifiers.add(ABSTRACT);
        }
        if (Modifier.isNative(modifiers)) {
            nonAccessModifiers.add(NATIVE);
        }
        if (Modifier.isStrict(modifiers)) {
            nonAccessModifiers.add(STRICTFP);
        }
        if (Modifier.isSynchronized(modifiers)) {
            nonAccessModifiers.add(SYNCHRONIZED);
        }
        if (Modifier.isTransient(modifiers)) {
            nonAccessModifiers.add(TRANSIENT);
        }
        if (Modifier.isVolatile(modifiers)) {
            nonAccessModifiers.add(VOLATILE);
        }
        return nonAccessModifiers;
    }

}
