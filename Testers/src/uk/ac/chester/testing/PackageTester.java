package uk.ac.chester.testing;

import org.jetbrains.annotations.Nullable;
import uk.ac.chester.testing.reflection.PackageHelper;

import java.util.Optional;
import java.util.Set;

public class PackageTester extends Tester {


    private final EventHandler handler;

    public PackageTester(EventHandler handler){
        this.handler = handler;
    }

    @Nullable
    public Class<?> getClass(String className, String packageName) {
        return PackageHelper.findClass(className, packageName).orElse(null);
    }

    public void testClassExists(String className, String packageName) {


        Optional<Class<?>> result = PackageHelper.findClass(className, packageName);
        if (result.isPresent()) {
            return;
        }

        //not found - check other packages
        Set<Class<?>> possibles = PackageHelper.findClasses(className);
        if (!possibles.isEmpty()) {
            handler.foundInWrongPackage(className, packageName, possibles.iterator().next().getPackage().getName());
        }

        //not found - check name variants in correct package
        for (String nameVariant : nameVariants(className)) {
            Optional<Class<?>> nameVariantResult = PackageHelper.findClass(nameVariant, packageName);
            if (nameVariantResult.isPresent()) {
                handler.foundWrongCase(className, nameVariant);
            }
        }

        //not found - check name variants in other packages
        for (String nameVariant : nameVariants(className)) {
            Set<Class<?>> possiblesWithWrongName = PackageHelper.findClasses(nameVariant);
            if (!possiblesWithWrongName.isEmpty()){
                handler.foundWrongCaseAndPackage(className, nameVariant, packageName, possiblesWithWrongName.iterator().next().getPackage().getName());            }

        }

    }


    private String[] nameVariants(String name) {

        String[] names = new String[name.length()>1 ? 3 : 2];

        names[0] = name.toLowerCase(); //lowercase
        names[1] = name.toUpperCase(); //UPPERCASE

        if (name.length() > 1) {
            names[2] = name.substring(0, 1).toLowerCase() + name.substring(1); //lowerCamelCase
        }

        return names;

    }


    public interface EventHandler {

        /**
         * The class was not found
         * @param name the name of the class looked for
         */
        void notFound(String name);

        /**
         * A class with the correct name, but with the wrong casing for the name was found.
         * Only a small set of variations of class name are searched for
         *
         * @param desiredName the name of the class as it should be
         * @param foundName the name of the class as it was found
         */
        void foundWrongCase(String desiredName, String foundName);

        /**
         * A class with the correct name was found, but in the wrong package
         * @param name the name of the class found
         * @param desiredPackage the package it was expected to be in
         * @param actualPackage the package it was found in
         */
        void foundInWrongPackage(String name, String desiredPackage, String actualPackage);


        /**
         * A class with the name in the wrong case was found in the wrong package
         * @param name the desired name of the class
         * @param foundName the name of the class found
         * @param desiredPackage the desired package for the class
         * @param actualPackage the package the class was found in
         */
        void foundWrongCaseAndPackage(String name, String foundName, String desiredPackage, String actualPackage);




    }



}
