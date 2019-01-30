package uk.ac.chester.Testing;

import java.util.HashSet;
import java.util.Set;

public class PackageHelper {

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
            if (name.startsWith(ignoredPackage + ".") || name.equals(ignoredPackage)){
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param name the name (not qualified) of the class to find
     * @return all classes matching that name
     */
    static Set<Class> findClasses(String name) {

        Set<Class> classes = new HashSet<Class>();

        for (Package p : Package.getPackages()) {
            String packageName = p.getName();
            if (!isIgnoredPackage(packageName)) {
                String fullyQualifiedClassName = packageName + "." + name;
                try {
                Class aClass = Class.forName(fullyQualifiedClassName);
                classes.add(aClass);
                } catch (ClassNotFoundException e) {
                    //Empty catch blocks are usually a bad idea,
                    //but we really don't need to do anything here!
                }
            }
        }
        return classes;
    }


}
