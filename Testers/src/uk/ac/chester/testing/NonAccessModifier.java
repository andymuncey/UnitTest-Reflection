package uk.ac.chester.testing;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.*;

public enum NonAccessModifier {

    FINAL {
        @Override
        public String toString() {
            return "final";
        }
    },
    STATIC {
        @Override
        public String toString() {
            return "static";
        }
    },
    ABSTRACT {
        @Override
        public String toString() {
            return "abstract";
        }
    },
    SYNCHRONIZED {
        @Override
        public String toString() {
            return "synchronized";
        }
    },
    VOLATILE {
        @Override
        public String toString() {
            return "volatile";
        }
    },
    TRANSIENT {
        @Override
        public String toString() {
            return "transient";
        }
    },
    NATIVE {
        @Override
        public String toString() {
            return "native";
        }
    },
    STRICTFP {
        @Override
        public String toString() {
            return "strictfp";
        }
    };

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
