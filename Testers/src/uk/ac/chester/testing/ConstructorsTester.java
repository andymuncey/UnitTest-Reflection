package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.ConstructorsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.*;
import java.util.Optional;


/**
 * A class to testExistence Constructors for a class
 * Known issue:
 * * When using the testExistence() method to testExistence a method that returns a primitive type will result in a object type being
 * * returned which has to be cast as a primitive in order to be used in an assertion
 */
public class ConstructorsTester<T> extends Tester {

    private final ConstructorsHelper<T> helper;
    private final EventHandler handler;

    /**
     * Creates a ConstructorsTester for the provided class
     *
     * @param theClass           the class to testExistence the constructors of
     * @param handler An implementation of EventHandler, likely containing unit testExistence assertions
     */
    public ConstructorsTester(Class<T> theClass, EventHandler handler) {
        helper = new ConstructorsHelper<>(theClass);
        this.handler = handler;
    }

    /**
     * Tests the following:
     * -That the constructor has the correct parameters, though not necessarily in the correct order
     * -That the constructor has the correct parameters, in the correct order
     * -That the constructor has the correct access modifier (if specified)
     * -That the constructor parameter names match the Java convention
     * -That the object can be instantiated using the constructor
     *
     * @param modifier        an AccessModifier, or null to skip testing this
     * @param args arguments to be passed to the constructor
     * @return an object instantiated using the arguments, or null if this is not possible
     */
    public T test(AccessModifier modifier, Object... args) {

        if (checkExistence(args)) {
            Optional<Constructor<T>> possibleConstructor = helper.constructorForArgTypes(true, true, args);

            if (possibleConstructor.isPresent()) {
                Constructor<T> c = possibleConstructor.get();
                c.setAccessible(true); //ensures private constructors can be called

                if (modifier != null) {
                    checkModifier(modifier, c);
                }
                checkParameterNames(c);

                try {
                    return c.newInstance(args);
                } catch (InstantiationException e) {
                    //could not instantiate - abstract class?
                    handler.constructionFails(e, args);
                } catch (IllegalAccessException e) {
                    //unable to access constructor, maybe private (shouldn't happen)
                    handler.constructionFails(e, args);
                } catch (InvocationTargetException e) {
                    //underlying constructor threw an exception
                    handler.constructionFails(e.getCause(), args);
                }
            }
        }
        return null;
    }

    private boolean checkExistence(Object[] args) {
        if (!constructorWithArgsExists(false, args)) {
            //doesn't exist with the parameters in any order
            handler.incorrectParameters(Utilities.classesForArgs(args));
            return false;
        }

        //exists with the params, order may or may not be correct
        if (!constructorWithArgsExists(true, args)) {
            //not in correct order
            handler.incorrectParamOrder(Utilities.classesForArgs(args));
            return false;
        }
        return true;
    }

    private void checkModifier(AccessModifier modifier, Constructor c) {
        AccessModifier actualModifier = AccessModifier.accessModifier(c);
        if (!modifier.equals(actualModifier)) {
            handler.wrongAccessModifier(actualModifier, modifier);
        }
    }

    private void checkParameterNames(Constructor c) {
        String[] paramNames = Utilities.parameterNames(c);
        for (String name : paramNames) {
            if (!getConventionChecker().validVariableName(name)) {
                handler.paramNameUnconventional(name);
            }
        }
    }

    private boolean constructorWithArgsExists(boolean matchParamOrder, Object... args) {
        return helper.constructorForArgTypes(true, matchParamOrder, args).isPresent();
    }

    public interface EventHandler {

        /**
         * No constructor with the required parameters could be found
         *
         * there will always be a no-argument constructor unless a non-parameterless one is added
         * There is no way (using reflection) to differentiate between an inherited no-argument constructor
         * and one that has been created
         *
         * @param requiredParamTypes the required parameters for the constructor
         */
        void incorrectParameters(Class[] requiredParamTypes);

        /**
         * Correct parameters exist for the constructor, but not in the required order
         *
         * @param requiredParams the order the parameters should be in
         */
        void incorrectParamOrder(Class[] requiredParams);

        /**
         * Correct parameters exist for the constructor, but don't match the naming convention for Java parameters
         * Test will continue unless an assertion fails in the implementation of this method
         *
         * @param paramName the parameter that doesn't meet the convention
         */
        void paramNameUnconventional(String paramName);

        /**
         * Constructor with correct parameters identified, but access level is incorrect
         *
         * @param actual   the access modifier declared for the constructor
         * @param required the access modifier that should be declared
         */
        void wrongAccessModifier(AccessModifier actual, AccessModifier required);

        /**
         * Calling the constructor with the values in args fails
         *
         * @param e    the exception thrown upon attempting construction
         * @param args the arguments used when construction fails
         */
        void constructionFails(Throwable e, Object... args);
    }
}