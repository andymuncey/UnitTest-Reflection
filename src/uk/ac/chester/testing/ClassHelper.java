package uk.ac.chester.testing;

import java.util.Optional;

public class ClassHelper {

    private String packageName;

    public ClassHelper(String packageName) {
        this.packageName = packageName;
    }

    public Optional<Class> findClass(String className) {
        String fullyQualifiedClassName = packageName + "." + className;
        return classForName(fullyQualifiedClassName);
    }

    private static Optional<Class> classForName(String fullyQualifiedName) {
        try {
            return Optional.of(Class.forName(fullyQualifiedName));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

}
