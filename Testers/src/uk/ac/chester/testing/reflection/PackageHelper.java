package uk.ac.chester.testing.reflection;

import java.util.*;

public class PackageHelper {

    private static final String[] ignoredPackages = {
            "com.intellij",
            "java",
            "jdk",
            "junit",
            "org.hamcrest",
            "org.jetbrains",
            "org.junit",
            "org.opentest4j",
            "sun",
            "org.apiguardian"
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
    public static Set<Class<?>> findClasses(String name) {

        Set<Class<?>> classes = new HashSet<>();


        Package[] packages = Package.getPackages();

        List<Package> relevantPackages = new ArrayList<>();
        for (Package p : packages) {
            final String packageName = p.getName();
            if (!isIgnoredPackage(packageName)) {
                relevantPackages.add(p);
            }


        }



        for (Package p : relevantPackages) {
            final String packageName = p.getName();
            final String fullyQualifiedClassName = packageName + "." + name;
            final Optional<Class<?>> foundClass = classForName(fullyQualifiedClassName);
            foundClass.ifPresent(classes::add);
        }
        return classes;
    }

    /**
     * finds a named class in a specific package
     * @param className the name of the class to find
     * @param packageName the name of the package to look in
     * @return An Optional containing the class, if found
     */
    public static Optional<Class<?>> findClass(String className, String packageName) {
        String fullyQualifiedClassName = packageName + "." + className;
        return classForName(fullyQualifiedClassName);
    }

    /**
     * finds a class given it's fully qualified name
     * @param fullyQualifiedName fully qualified class name, e.g. com.example.Widget
     * @return An Optional containing the class, if found
     */
    public static Optional<Class<?>> classForName(String fullyQualifiedName) {
        try {
            return Optional.of(Class.forName(fullyQualifiedName));
        } catch (ClassNotFoundException | NoClassDefFoundError e ) {
            return Optional.empty();
        }
    }
}
