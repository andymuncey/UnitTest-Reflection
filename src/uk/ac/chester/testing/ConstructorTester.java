package uk.ac.chester.testing;

import java.lang.reflect.*;
import java.util.Optional;

public class ConstructorTester<T> implements ExecutableTester {

    private ReflectionHelper helper;
    private ConstructorTestEventHandler constructorHandler;

    public ConstructorTester(Class<T> theClass, ConstructorTestEventHandler constructorHandler){
        helper = new ReflectionHelper(theClass);
        this.constructorHandler = constructorHandler;
    }

    /**
     *  Tests the following:
     * -That the constructor has the correct parameters, though not necessarily in the correct order
     * -That the constructor has the correct parameters, in the correct order
     * -That the constructor has the correct access modifier (if specified)
     * -That the constructor parameter names match the Java convention
     * -That the object can be instantiated using the constructor
     *
     * @param modifier an AccessModifier, or null to skip testing this
     * @param allowAutoboxing whether the return type can be autoboxed / unboxed
     * @param constructorArgs arguments to be passed to the constructor
     * @return an object instantiated using the arguments, or null if this is not possible
     */
    public T testConstructor(AccessModifier modifier, boolean allowAutoboxing, Object... constructorArgs) {

        if (!constructorWithArgsExists(false, allowAutoboxing,constructorArgs)){
            //doesn't exist with the parameters in any order
            constructorHandler.incorrectParameters(ReflectionHelper.classesForArgs(constructorArgs));
        }

        //exists with the params, order may or may not be correct
        if (!constructorWithArgsExists(true, allowAutoboxing, constructorArgs)){
            //not in correct order
            constructorHandler.incorrectParamOrder(ReflectionHelper.classesForArgs(constructorArgs));
        } else {
            try {
                Optional<Constructor> possibleCtor = helper.constructorForArgTypes(true, allowAutoboxing, true, constructorArgs);

                if (possibleCtor.isPresent()) {
                    Constructor<T> c = possibleCtor.get();
                    c.setAccessible(true); //ensures private constructors can be called

                    if (modifier != null) {
                        AccessModifier actualModifier = accessModifier(c);
                        if (!modifier.equals(actualModifier)) {
                            constructorHandler.wrongAccessModifier(actualModifier, modifier);
                        }
                    }

                    String[] paramNames = ReflectionHelper.parameterNames(c);
                    for (String name:paramNames){
                        if (!validVariableName(name)){
                            constructorHandler.paramNameUnconventional(name);
                        }
                    }

                    return c.newInstance(constructorArgs);
                }

            } catch (InstantiationException e){
                //could not instantiate - abstract class?
                constructorHandler.constructionFails(e, constructorArgs);

            } catch (IllegalAccessException e){
                //unable to access constructor, maybe private
                constructorHandler.constructionFails(e, constructorArgs);
            }
            catch (InvocationTargetException e){
                //underlying constructor threw an exception
                constructorHandler.constructionFails(e.getCause(), constructorArgs);
            }
        }
        return null;
    }


    private boolean constructorWithArgsExists(boolean matchParamOrder, boolean allowAutoboxing, Object... args){
        return helper.constructorForArgTypes(true, allowAutoboxing,matchParamOrder, args).isPresent();
    }

    public interface ConstructorTestEventHandler {

        /**
         * A constructor has been found, but the parameters are not as required
         * @param requiredParamTypes the required parameters for the constructor
         */
        void incorrectParameters(Class[] requiredParamTypes);

        /**
         * Correct parameters exist for the constructor, but not in the required order
         * @param requiredParams the order the parameters should be in
         */
        void incorrectParamOrder(Class[] requiredParams);

        /**
         * Correct parameters exist for the constructor, but don't match the naming convention for Java parameters
         * Test will continue unless an assertion fails in the implementation of this method
         * @param paramName the parameter that doesn't meet the convention
         */
        void paramNameUnconventional(String paramName);

        /**
         * Constructor with correct parameters identified, but access level is incorrect
         * @param actual the access modifier declared for the constructor
         * @param required the access modifier that should be declared
         */
        void wrongAccessModifier(AccessModifier actual, AccessModifier required);

        /**
         * Calling the constructor with the values in args fails
         * @param e the exception thrown upon attempting construction
         * @param args the arguments used when construction fails
         */
        void constructionFails(Throwable e, Object... args);

    }


}
