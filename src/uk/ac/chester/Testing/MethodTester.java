package uk.ac.chester.Testing;

import org.junit.Assert;

public class MethodTester {


    public interface MethodTestEventHandler {
        /**
         *
         * @param methodName the name of the method as it should be
         */
        void notFound(String methodName);

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
        void incorrectParameters(String methodName);

        /**
         * Correct parameters exist for the method, but not in the required order
         * @param methodName the name of the method
         * @param requiredParamOrder the order the parameters should be in
         */
        void incorrectParamOrder(String methodName, String requiredParamOrder);
    }

    private ReflectionHelper helper;
    private String methodName;

    private MethodTestEventHandler handler;


    public MethodTester(Class searchClass, String methodName){
        this.helper = new ReflectionHelper(searchClass);
        this.methodName = methodName;
    }

    public MethodTester(Class searchClass, String methodName, MethodTestEventHandler handler){
        this(searchClass, methodName);
        this.handler = handler;
    }

    private boolean methodMatchingNameFound(){
        return !helper.findMethods(methodName).isEmpty();
    }

    private boolean methodMatchingNameAndReturnTypeFound(Class returnType, boolean strict){
        return !helper.findMethods(returnType, methodName, strict).isEmpty();
    }

    private boolean methodFound(Class returnType, Class[] paramTypes){
        return helper.findMethod(returnType, methodName,paramTypes).isPresent();
    }

    public void testExecution(Object expected, Object... args ){
        testExecution("",expected,args);
    }

    public void testExecution(String message, Object expected, Object... args){

        final Class returnType = expected.getClass();

        testExistence(returnType, args, false);
        //must have matched name, return type, and args by now

        final Object result = helper.invokeMethod(returnType, methodName, args);

        if (message != null && !message.isEmpty()){
            Assert.assertEquals(message,expected,result);
        } else {
            Assert.assertEquals(expected,result);
        }
    }

    /**
     * Tests the existence of a method and provides assertions where the name, return type or parameter types do not match a method in the class
     * @param returnType the type of data returned by the class
     * @param args example arguments for the methods (actual values, not types)
     * @param strict setting 'true' considers primitives and their object equivalents to be different. False matches primitive return types with their object counterparts
     */
    private void testExistence(Class<?> returnType,  Object[] args, boolean strict) {

        if (!methodMatchingNameFound()){

            if (handler != null){
                handler.notFound(methodName);
            } else {
                Assert.fail("No method with the name: "+ methodName + " was found");
            }
        }

        if (!methodMatchingNameAndReturnTypeFound(returnType, strict)) {

            if (handler != null){
                handler.incorrectReturnType(methodName,returnType);
            } else {
                Assert.fail("A method with the correct name was found, but it does not return the correct type: " + returnType.getName());
            }
        }

        //final Class[] argTypes = paramTypesFromParams(args);
        final Class[] argTypes = helper.classesForArgs(args);

        if (!methodFound(returnType,argTypes)){
            if (!helper.methodsWithSignature(returnType,false, strict, argTypes).isEmpty()){
                if (handler != null){
                    handler.incorrectParamOrder(methodName,paramNames(argTypes));
                } else {
                Assert.fail("One or more method with the correct name, return type and parameters were found, but none of the method(s) have the parameters in the correct order");
                }
            }else {
                if (handler != null){
                    handler.incorrectParameters(methodName);
                }

                String paramTypeNames = paramNames(argTypes);
                Assert.fail("One or more methods with the correct name and return type were found, but none have the correct parameters: " + paramTypeNames);
            }
        }
    }

    static String paramNames(Class[] argTypes){
        StringBuilder paramTypeNames = new StringBuilder();
        for (int i = 0; i < argTypes.length; i++) {
            Class paramType =argTypes[i];
            paramTypeNames.append(paramType.getName());
            if (i < argTypes.length - 1) {
                paramTypeNames.append(", ");
            }
        }
        return paramTypeNames.toString();
    }


    //todo: add tests with deltas for floats

    /**
     * tests the existence and execution of a method
     * @param expected the expected return value
     * @param delta the acceptable difference between values
     * @param args arguments to invoke the method with
     */
    void testWithTolerance(Double expected, double delta, Object... args){
        testWithTolerance((String)null,expected,delta,args);
    }


    /**
     * tests the existence and execution of a method
     * @param message the assertion failure message
     * @param expected the expected return value
     * @param delta the acceptable difference between values
     * @param args arguments to invoke the method with
     */
    public void testWithTolerance(String message, Double expected, double delta, Object... args){
        final Class<?> returnType = expected.getClass();
        testExistence(returnType,args, false);

        final Double result = helper.invokeMethod(expected.getClass(),methodName,args);
        if (message != null && !message.isEmpty()){
            Assert.assertEquals(message,expected,result, delta);
        } else {
            Assert.assertEquals(expected, result, delta);
        }
    }



}
