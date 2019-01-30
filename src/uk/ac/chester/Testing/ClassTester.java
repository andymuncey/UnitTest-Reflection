package uk.ac.chester.Testing;


import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Modifier;
import java.util.Optional;

public class ClassTester<T> implements ExecutableTester {

    private ReflectionHelper helper;
    private ClassTestEventHandler handler;

    public ClassTester(Class<T> theClass, ClassTestEventHandler handler){

        helper = new ReflectionHelper(theClass);
        this.handler = handler;
    }


    public T testStrict(AccessModifier modifier, Object... constructorArgs){
        return test(modifier,true,constructorArgs);
    }

    public T test(AccessModifier modifier, Object... constructorArgs){
        return test(modifier, false, constructorArgs);
    }

    /**
     *
     * -That the constructor has the correct parameters, though not necessarily in the correct order
     * -That the constructor has the correct parameters, in the correct order
     * -That the constructor has the correct access modifier (if specified)
     * -That the constructor parameter names match the Java convention
     *
     * @param modifier an AccessModifier, or null to skip testing this
     * @param constructorArgs arguments to be passed to the constructor
     * @return an object instantiated using the arguments, or null if this is not possible
     */
    private T test(AccessModifier modifier, boolean strict, Object... constructorArgs) {


        if (!constructorWithArgsExists(false,strict,constructorArgs)){
            //doesn't exist with the parameters in any order
            handler.incorrectParameters(helper.classesForArgs(constructorArgs));
        }

        //exists with the params, order may or may not be correct
        if (!constructorWithArgsExists(true,strict, constructorArgs)){
            //not in correct order
            handler.incorrectParamOrder(helper.classesForArgs(constructorArgs));
        } else {
            try {
                Optional<Constructor> possibleCtor = helper.constructorForArgTypes(true, strict, true, constructorArgs);

                if (possibleCtor.isPresent()) {
                    Constructor<T> c = possibleCtor.get();
                    c.setAccessible(true); //ensures private constructors can be called

                    if (modifier != null) {
                        AccessModifier actualModifier = accessModifier(c);
                        if (!modifier.equals(actualModifier)) {
                            handler.wrongAccessModifier(actualModifier, modifier);
                        }
                    }

                    String[] paramNames = helper.parameterNames(c);
                    for (String name:paramNames){
                        if (!validParamName(name)){
                            handler.paramNameUnconventional(name);
                        }
                    }

                    return c.newInstance(constructorArgs);
                }

            } catch (InstantiationException e){
                //could not instantiate - abstract class?
                handler.constructionFails(e, constructorArgs);

            } catch (IllegalAccessException e){
                //unable to access constructor, maybe private
                handler.constructionFails(e, constructorArgs);
            }
            catch (InvocationTargetException e){
                //underlying constructor threw an exception
                handler.constructionFails(e.getCause(), constructorArgs);
            }
        }
        return null;
    }


    private AccessModifier accessModifier(Executable executable){
        if (Modifier.isPublic(executable.getModifiers())){
            return AccessModifier.PUBLIC;
        }
        if (Modifier.isPrivate(executable.getModifiers())){
            return AccessModifier.PRIVATE;
        }
        if (Modifier.isProtected(executable.getModifiers())){
            return AccessModifier.PROTECTED;
        }
        return AccessModifier.PACKAGE_PRIVATE;
    }

    public boolean constructorWithArgsExists(boolean matchParamOrder, boolean strict, Object... args){
        return helper.constructorForArgTypes(true, strict,matchParamOrder, args).isPresent();
    }

    public interface ClassTestEventHandler {

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
