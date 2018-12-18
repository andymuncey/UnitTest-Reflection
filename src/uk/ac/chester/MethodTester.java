package uk.ac.chester;

import org.junit.Assert;

class MethodTester {

    private ReflectionHelper helper;
    private String methodName;

    MethodTester(Class searchClass, String methodName){
        this.helper = new ReflectionHelper(searchClass);
        this.methodName = methodName;
    }

    private boolean methodMatchingNameFound(){
        return !helper.findMethods(methodName).isEmpty();
    }

    private boolean methodMatchingNameAndReturnTypeFound(Class returnType, boolean strict){
        return !helper.findMethods(methodName,returnType, strict).isEmpty();
    }

    private boolean methodFound(Class returnType, Class[] paramTypes){
        return helper.findMethod(returnType, methodName,paramTypes).isPresent();
    }

    private Class[] paramTypesFromParams(Object... args){
        final Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        return argTypes;
    }


    void testExecution(Object expected, Object... args ){
        testExecution("",expected,args);
    }

    void testExecution(String message, Object expected, Object... args){

        final Class returnType = expected.getClass();

        testExistence(returnType, args, false);
        //must have matched name, return type, and args by now

        final Object result = helper.invokeMethod(returnType,methodName,args);

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
     * @param strict false to match primitive return types with their object counterparts
     */
    private void testExistence(Class<?> returnType,  Object[] args, boolean strict) {

        if (!methodMatchingNameFound()){
            Assert.fail("No method with the name: "+ methodName + " was found");
        }

        if (!methodMatchingNameAndReturnTypeFound(returnType, strict)) {
            Assert.fail("A method with the correct name was found, but it does not return the correct type: " + returnType.getName());
        }

        final Class<Object>[] argTypes = paramTypesFromParams(args);

        if (!methodFound(returnType,argTypes)){

            if (!helper.methodsWithSignature(returnType,false, strict, argTypes).isEmpty()){
                Assert.fail("One or more method with the correct name, return type and parameters were found, but none of the method(s) have the parameters in the correct order");
            } else {
                String paramTypeNames = "";
                for (Class paramType:argTypes) {
                    paramTypeNames += paramType.getName() + ", ";
                }
                Assert.fail("One or more methods with the correct name and return type were found, but none have the correct parameters: " + paramTypeNames);
            }
        }
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
    void testWithTolerance(String message, Double expected, double delta, Object... args){
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
