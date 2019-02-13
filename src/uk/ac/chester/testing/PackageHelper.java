package uk.ac.chester.testing;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class PackageHelper {

    private static String[] ignoredPackages = {
            "com.intellij",
            "java",
            "jdk",
            "junit",
            "org.hamcrest",
            "org.jetbrains",
            "org.junit",
            "sun"
    };

    private static boolean isIgnoredPackage(String name){
        for (String ignoredPackage: ignoredPackages) {
            if (name.startsWith(ignoredPackage + ".") ||
                    name.equals(ignoredPackage)){
                return true;
            }
        }
        return false;
    }


    /**
     * find all classes matching a given name
     * @param name the name (not qualified) of the class to find
     * @return all classes matching that name
     */
    static Set<Class> findClasses(String name) {

        Set<Class> classes = new HashSet<Class>();

        for (Package p : Package.getPackages()) {
            final String packageName = p.getName();
            if (!isIgnoredPackage(packageName)) {
                final String fullyQualifiedClassName = packageName + "." + name;
                final Optional<Class> foundClass = classForName(fullyQualifiedClassName);
                if (foundClass.isPresent()){
                    classes.add(foundClass.get());
                }
            }
        }
        return classes;
    }


    /**
     * finds a named class in a specific package
     * @param className the name of the class to find
     * @param packageName the name of the package to look in
     * @return An Optional containing the class, if found
     */
    static Optional<Class> findClass(String className, String packageName) {
        String fullyQualifiedClassName = packageName + "." + className;
        return classForName(fullyQualifiedClassName);
    }


    /**
     * finds a class given it's fully qualified name
     * @param fullyQualifiedName fully qualified class name, e.g. com.example.Widget
     * @return An Optional containing the class, if found
     */
    private static Optional<Class> classForName(String fullyQualifiedName) {
        try {
            return Optional.of(Class.forName(fullyQualifiedName));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }


}
