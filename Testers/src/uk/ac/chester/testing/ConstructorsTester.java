package uk.ac.chester.testing;

import java.lang.reflect.*;
import java.util.Optional;


/**
 * A class to test Constructors for a class
 * <p>
 * Known issue:
 * * When using the test() method to test a method that returns a primitive type will result in a object type being
 * * returned which has to be cast as a primitive in order to be used in an assertion
 */
public class ConstructorsTester<T> extends Tester {

    private ReflectionHelper<T> helper;
    private ConstructorTestEventHandler constructorHandler;

    /**
     * Creates a ConstructorsTester for the provided class
     *
     * @param theClass           the class to test the constructors of
     * @param constructorHandler An implementation of ConstructorTestEventHandler, likely containing unit test assertions
     */
    public ConstructorsTester(Class<T> theClass, ConstructorTestEventHandler constructorHandler) {
        helper = new ReflectionHelper<>(theClass);
        this.constructorHandler = constructorHandler;
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
     * @param constructorArgs arguments to be passed to the constructor
     * @return an object instantiated using the arguments, or null if this is not possible
     */
    public T test(AccessModifier modifier, Object... constructorArgs) {

        if (checkExistence(constructorArgs)) {
            Optional<Constructor<T>> possibleCtor = helper.constructorForArgTypes(true, true, constructorArgs);

            if (possibleCtor.isPresent()) {
                Constructor<T> c = possibleCtor.get();
                c.setAccessible(true); //ensures private constructors can be called

                if (modifier != null) {
                    checkModifier(modifier, c);
                }
                checkParameterNames(c);

                try {
                    return c.newInstance(constructorArgs);
                } catch (InstantiationException e) {
                    //could not instantiate - abstract class?
                    constructorHandler.constructionFails(e, constructorArgs);
                } catch (IllegalAccessException e) {
                    //unable to access constructor, maybe private (shouldn't happen)
                    constructorHandler.constructionFails(e, constructorArgs);
                } catch (InvocationTargetException e) {
                    //underlying constructor threw an exception
                    constructorHandler.constructionFails(e.getCause(), constructorArgs);
                }
            }
        }
        return null;
    }

    private boolean checkExistence(Object[] args) {
        if (!constructorWithArgsExists(false, args)) {
            //doesn't exist with the parameters in any order
            constructorHandler.incorrectParameters(ReflectionHelper.classesForArgs(args));
            return false;
        }

        //exists with the params, order may or may not be correct
        if (!constructorWithArgsExists(true, args)) {
            //not in correct order
            constructorHandler.incorrectParamOrder(ReflectionHelper.classesForArgs(args));
            return false;
        }
        return true;
    }

    private void checkModifier(AccessModifier modifier, Constructor c) {
        AccessModifier actualModifier = AccessModifier.accessModifier(c);
        if (!modifier.equals(actualModifier)) {
            constructorHandler.wrongAccessModifier(actualModifier, modifier);
        }
    }

    private void checkParameterNames(Constructor c) {
        String[] paramNames = ReflectionHelper.parameterNames(c);
        for (String name : paramNames) {
            if (!getConventionChecker().validVariableName(name)) {
                constructorHandler.paramNameUnconventional(name);
            }
        }
    }

    private boolean constructorWithArgsExists(boolean matchParamOrder, Object... args) {
        return helper.constructorForArgTypes(true, matchParamOrder, args).isPresent();
    }

    public interface ConstructorTestEventHandler {

        /**
         * A constructor has been found, but the parameters are not as required
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