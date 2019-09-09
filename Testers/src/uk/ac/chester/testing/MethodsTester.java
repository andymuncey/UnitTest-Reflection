package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.MethodsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Tests methods which may not be implemented in a given class.
 * Known issues:
 *  * Passing an array as the only argument to the test method is ambiguous as it could be inferred as an array object or varargs
 * @param <C> type of the class that's being tested
 */
public class MethodsTester<C> extends Tester {

    private final MethodsHelper<C> helper;
    private final EventHandler handler;

    /**
     * The type parameters should be the type of the class that's being tests,
     * and the reference type corresponding to type the tested method returns (e.g. for a return type of int, specify Integer)
     * @param searchClass     the class in which the method should be written
     * @param handler         a {@link EventHandler} for handling non-existent methods
     */
    public MethodsTester(Class<C> searchClass, EventHandler handler) {
        this.helper = new MethodsHelper<>(searchClass);
        this.handler = handler;
    }

    /**
     * Indicates if a method exists with the name corresponding to the one supplied when the class was initialised
     * @return true if found, else false
     */
    private boolean methodMatchingNameFound(String methodName){
        return !helper.findMethods(methodName,false).isEmpty();
    }

    /**
     * Indicates a method is found matching the name, but the case may not match, e.g. MyMethod() would find myMethod()
     * @return true if a method exists, false otherwise
     */
    private boolean methodMatchingCaseInsensitiveNameFound(String methodName){
        return !helper.findMethods(methodName,true).isEmpty();
    }

    /**
     * Indicates if a method with a given name and returnType exists
     * @param returnType A Class indicating the return type, this may be the class of a primitive type or and object type
     * @param allowAutoboxing whether the return type must be an exact match (Integer and int would not be considered an exact match)
     * @return true if found, else false
     */
    private boolean methodMatchingNameAndReturnTypeFound(Class returnType, String methodName, boolean allowAutoboxing){
        return !helper.findMethods(allowAutoboxing, returnType, methodName).isEmpty();
    }

    /**
     * Indicates if a method with the corresponding return type and parameter types is found (not strict with regard to class / primitive types)
     * @param returnType the return type of the method
     * @param paramTypes the parameter types of the method
     * @return true if the specified method exists, false otherwise
     */
    private boolean methodFound(boolean allowAutoboxing, Class returnType, String methodName, Class[] paramTypes){
        return helper.findMethod(allowAutoboxing, returnType, methodName,paramTypes).isPresent();
    }

    /**
     * Indicates if a method with the corresponding return type and parameter types is found (not strict with regard to class / primitive types)
     * @param modifier the expected access modifier for the method
     * @param returnType the return type of the method
     * @param paramTypes the parameter types of the method
     * @return true if a method is found matching the specification
     */
    private boolean methodFound(AccessModifier modifier, Class returnType, String methodName, Class[] paramTypes){
         return helper.findMethod(modifier,returnType,  methodName,true,paramTypes).isPresent();
    }



    public <T> T executeStaticWithSpecifics(boolean allowAutoboxing, AccessModifier modifier, Class<T> returnTypeClass, String methodName, Object... args){
        return testExistence(allowAutoboxing, modifier, returnTypeClass,methodName, args) ? helper.invokeStaticMethod(allowAutoboxing,returnTypeClass,methodName,args) : null ;
    }


    /**
     * Executes a static method, allowing autoboxing/unboxing, and any access modifier
     * @param returnTypeClass
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    public <T> T executeStatic(Class<T> returnTypeClass, String methodName, Object... args){
        return executeStaticWithSpecifics(true,null,returnTypeClass,methodName,args);
    }


    /**
     * If the method exists, method is invoked, and the value returned
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param allowAutoboxing whether the return type must be an exact match (Integer and int would not be considered an exact match)
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    private boolean test(boolean allowAutoboxing, AccessModifier modifier, Class returnTypeClass, String methodName, Object[] args){
        return testExistence(allowAutoboxing, modifier, returnTypeClass,methodName, args) ;
//        return  helper.invokeMethod(returnTypeClass, methodName, args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type may be autoboxed/unboxed
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public boolean test(AccessModifier modifier, Class returnTypeClass, String methodName, Object... args){
        return test(true, modifier, returnTypeClass,methodName, args);
    }

//    /**
//     * If the method exists, method is invoked, and the value returned. Return type may be autoboxed/unboxed
//     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
//     * Access modifiers are ignored
//     * @param returnTypeClass the type the method is expected to return
//     * @param methodName the name of the method
//     * @param <R> the type the method returns
//     * @return the result of invoking the method (or null)
//     */
//    public <R> R test(Class<R> returnTypeClass, String methodName){
//        return test(null, returnTypeClass, methodName);
//    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type may be autoboxed/unboxed
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * Access modifiers are ignored
     * @param returnTypeClass the type the method is expected to return
     * @param methodName the name of the method
     * @param args the arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public boolean test(Class returnTypeClass, String methodName, Object... args){
        return test(null, returnTypeClass, methodName, args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type will not be matched to boxed/unboxed type
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param modifier An AccessModifier indicating the desired access modifier, or null if this is unimportant
     * @param returnTypeClass the type the method is expected to return
     * @param methodName the name of the method
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public boolean testForExactReturnType(AccessModifier modifier, Class returnTypeClass, String methodName,Object... args){
        return test(false, modifier, returnTypeClass,methodName, args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type will not be matched to boxed/unboxed type
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param returnTypeClass the type the method is expected to return
     * @param methodName the name of the method
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public boolean testForExactReturnType(Class returnTypeClass, String methodName, Object... args){
        return testForExactReturnType(null,returnTypeClass,methodName,args);
    }





    /**
     * Tests the following, in relation to the method, returning null soon as any condition is not met:
     * -That a method with a matching name, but not necessarily with the correct case exists
     * -That a method with an exact match to the name exists
     * -That the method returns the correct type
     * -That the method has the correct parameters, though not necessarily in the correct order
     * -That the method has the correct parameters, in the correct order
     *
     * Finally, if the method is correctly declared, it returns the result of executing the method
     *
     * @param allowAutoboxing setting 'false' considers primitives and their object equivalents to be different. True matches primitive return types with their object counterparts
     * @param returnType the type of data returned by the class
     * @param args arguments for passing to the method (actual values, not types)
     */
    private boolean testExistence(boolean allowAutoboxing, AccessModifier accessModifier, Class<?> returnType, String methodName, Object[] args) {

        if (!methodMatchingNameFound(methodName)){
            if (methodMatchingCaseInsensitiveNameFound(methodName)){
                handler.wrongCaseName(methodName);
            } else {
                handler.notFound(methodName);
            }
            return false;
        }

        if (!methodMatchingNameAndReturnTypeFound(returnType, methodName, allowAutoboxing)) {
            handler.incorrectReturnType(methodName,returnType);
            return false;
        }

        //check for correct number of params
        Set<Method> methods = helper.findMethods(allowAutoboxing, returnType,methodName);
        boolean matchedParameterCount = false;
        for (Method method : methods){
            if (method.getParameterCount() == args.length){
                matchedParameterCount = true;
                break;
            }
        }
        if (!matchedParameterCount){
            handler.incorrectNumberOfParameters(methodName,args.length);
            return false;
        }

        //found method with correct name, return type, but wrong type or order of params


        final Class[] argTypes = Utilities.classesForArgs(args);

        if (!methodFound(allowAutoboxing, returnType, methodName,argTypes)){
            //not found an exact match

            Set<Method> similarMethods = helper.methodsWithSignature(allowAutoboxing, returnType,false, argTypes);
            for (Method m: similarMethods){
                if (!m.getName().equals(methodName)){
                    similarMethods.remove(m);
                }
            }


            if (!similarMethods.isEmpty()){
                //found a method
                handler.incorrectParamOrder(methodName,argTypes);
            }else {
                handler.incorrectParameters(methodName, argTypes);
            }
            return false;
        }


        if (accessModifier != null){
            if (!methodFound(accessModifier,returnType,methodName, argTypes)){
                handler.accessModifierIncorrect(methodName,accessModifier);
            }
            return false;
        }


        //found method
        String[] paramNames = helper.methodParamNames(returnType,methodName,argTypes);
        for (String paramName: paramNames){
            if (!getConventionChecker().validVariableName(paramName)){
                handler.paramNameUnconventional(methodName, paramName);
            }
        }
        return true;
    }


    /**
     * Allows callbacks to indicate problems when attempting to test methods that do not exist
     * It is anticipated that Assert.fail will be used in the implementation of each method to provide a describe
     */
    public interface EventHandler {
        /**
         * Cannot find a method with the correct name
         * @param methodName the name of the method as it should be
         */
        void notFound(String methodName);

        /**
         * A method with the correct name is found, but the case is wrong e.g. MyMethod instead of myMethod
         * @param methodName the name of the method as it should be
         */
        void wrongCaseName(String methodName);

        /**
         * A method has been found but the return type is wrong
         * @param methodName the name of the method
         * @param requiredReturnType the type the methods is expected to return
         */
        void incorrectReturnType(String methodName, Class requiredReturnType);

        /**
         * A method has been found but it does not have the expected number of parameters
         * @param methodName the name of the method
         * @param expectedParamCount the number of parameters expected
         */
        void incorrectNumberOfParameters(String methodName, int expectedParamCount);

        /**
         * A method has been found, but the parameters are not as required
         * @param methodName the name of the method
         */
        void incorrectParameters(String methodName, Class[] requiredParamTypes);

        /**
         * Correct parameters exist for the method, but not in the required order
         * @param methodName the name of the method
         * @param requiredParams the order the parameters should be in
         */
        void incorrectParamOrder(String methodName, Class[] requiredParams);

        /**
         * Correct parameters exist for the method, but don't match the naming convention for Java parameters
         * @param methodName the name of the method
         * @param paramName the parameter that doesn't meet the convention
         */
        void paramNameUnconventional(String methodName, String paramName);

        /**
         * Correct parameters exist for the method, but the access modifier is incorrect
         * @param methodName the name of the method
         * @param requiredModifier the access modifier expected
         */
        void accessModifierIncorrect(String methodName, AccessModifier requiredModifier);
    }
}
