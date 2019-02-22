package uk.ac.chester.testing;

/**
 * Tests methods which may not be implemented in a given class.
 * Known issues:
 *  * Passing an array as the only argument to the test method is ambiguous as it could be inferred as an array object or varargs
 *
 * @param <R> return type of the method that is being tested
 * @param <C> type of the class that's being tested
 */
public class MethodTester<R, C> extends Tester {

    private ReflectionHelper<C> helper;
    private String methodName;
    private MethodTestEventHandler handler;
    private Class<R> returnTypeClass;

    /**
     * The type parameter should correlate to the class type of the methods return type (e.g. for a return type of int, specify Integer)
     * @param searchClass     the class in which the method should be written
     * @param returnTypeClass the class of the return type
     * @param methodName      the name of the method to be tested, as a string, with no parentheses or parameter types
     * @param handler         a {@link MethodTestEventHandler} for handling non-existent methods
     */
    public MethodTester(Class<C> searchClass, Class<R> returnTypeClass, String methodName, MethodTestEventHandler handler) {
        this.helper = new ReflectionHelper<>(searchClass);
        this.methodName = methodName;
        this.handler = handler;
        this.returnTypeClass = returnTypeClass;
    }

    /**
     * Indicates if a method exists with the name corresponding to the one supplied when the class was initialised
     * @return true if found, else false
     */
    private boolean methodMatchingNameFound(){
        return !helper.findMethods(methodName,false).isEmpty();
    }

    /**
     * Indicates a method is found matching the name, but the case may not match, e.g. MyMethod() would find myMethod()
     * @return true if a method exists, false otherwise
     */
    private boolean methodMatchingCaseInsensitiveNameFound(){
        return !helper.findMethods(methodName,true).isEmpty();
    }

    /**
     * Indicates if a method with a given name and returnType exists
     * @param returnType A Class indicating the return type, this may be the class of a primitive type or and object type
     * @param allowAutoboxing whether the return type must be an exact match (Integer and int would not be considered an exact match)
     * @return true if found, else false
     */
    private boolean methodMatchingNameAndReturnTypeFound(Class returnType, boolean allowAutoboxing){
        return !helper.findMethods(returnType, methodName, allowAutoboxing).isEmpty();
    }

    /**
     * Indicates if a method with the corresponding return type and parameter types is found (not strict with regard to class / primitive types)
     * @param returnType the return type of the method
     * @param paramTypes the parameter types of the method
     * @return true if the specified method exists, false otherwise
     */
    private boolean methodFound(Class returnType, Class[] paramTypes){
        return helper.findMethod(returnType, methodName,paramTypes).isPresent();
    }

    /**
     * If the method exists, method is invoked, and the value returned
     * If the method is not found, an appropriate {@link MethodTestEventHandler} event will fire and null is returned
     * @param allowAutoboxing whether the return type must be an exact match (Integer and int would not be considered an exact match)
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    private R test(boolean allowAutoboxing, Object[] args){

        if (testExistence(returnTypeClass, args, allowAutoboxing)) {
            R result = helper.invokeMethod(returnTypeClass, methodName, args);

        }

        return testExistence(returnTypeClass, args, allowAutoboxing) ? helper.invokeMethod(returnTypeClass, methodName, args) : null;
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type may be autoboxed/unboxed
     * If the method is not found, an appropriate {@link MethodTestEventHandler} event will fire and null is returned
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public R test(Object... args){
        return test(true,args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type will not be matched to boxed/unboxed type
     * If the method is not found, an appropriate {@link MethodTestEventHandler} event will fire and null is returned
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    public R testForExactReturnType(Object... args){
        return test(false, args);
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
     * @param returnType the type of data returned by the class
     * @param args arguments for passing to the method (actual values, not types)
     * @param allowAutoboxing setting 'false' considers primitives and their object equivalents to be different. True matches primitive return types with their object counterparts
     */
    private boolean testExistence(Class<?> returnType,  Object[] args, boolean allowAutoboxing) {

        if (!methodMatchingNameFound()){
            if (methodMatchingCaseInsensitiveNameFound()){
                handler.wrongCaseName(methodName);
            } else {
                handler.notFound(methodName);
            }
            return false;
        }

        if (!methodMatchingNameAndReturnTypeFound(returnType, allowAutoboxing)) {
            handler.incorrectReturnType(methodName,returnType);
            return false;
        }

        final Class[] argTypes = ReflectionHelper.classesForArgs(args);

        if (!methodFound(returnType,argTypes)){
            if (!helper.methodsWithSignature(returnType,false, allowAutoboxing, argTypes).isEmpty()){
                handler.incorrectParamOrder(methodName,argTypes);
            }else {
                handler.incorrectParameters(methodName, argTypes);
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
    public interface MethodTestEventHandler {
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
    }
}
