package uk.ac.chester.Testing;




public class MethodTester<T> {


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
        void incorrectParameters(String methodName, Class[] requiredParamTypes);

        /**
         * Correct parameters exist for the method, but not in the required order
         * @param methodName the name of the method
         * @param requiredParams the order the parameters should be in
         */
        void incorrectParamOrder(String methodName, Class[] requiredParams);

    }

    private ReflectionHelper helper;
    private String methodName;
    private MethodTestEventHandler handler;
    private Class<T> returnTypeClass;


    /**
     * The type parameter should correlate to the class type of the methods return type (e.g. for a return type of int, specify Integer)
     * @param searchClass the class in which the method should be written
     * @param returnTypeClass the class of the return type use primitive type if testing will be strict and method should return a primitive
     * @param methodName the name of the method to be tested, as a string, with no parentheses or parameter types
     * @param handler a {@link MethodTestEventHandler} for handling non-existent methods
     */
    public MethodTester(Class searchClass, Class<T> returnTypeClass, String methodName, MethodTestEventHandler handler){
        this.helper = new ReflectionHelper(searchClass);
        this.methodName = methodName;
        this.handler = handler;
        this.returnTypeClass = returnTypeClass;
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


    /**
     * If the method exists, method is invoked, and the value returned
     * If the method is not found, an appropriate {@link MethodTestEventHandler} event will fire and null is returned
     * @param strict whether the return type must be an exact match (Integer and int would not be considered an exact match)
     * @param args arguments to invoke the method with
     * @return the result of invoking the method (or null)
     */
    private T test(boolean strict, Object[] args){
        return testExistence(returnTypeClass, args, strict) ? helper.invokeMethod(returnTypeClass, methodName, args) : null;
    }

    public T test(Object... args){
        return test(false,args);
    }

    public T testStrict(Object... args){
        return test(true, args);
    }

    /**
     * Tests the existence of a method and provides assertions where the name, return type or parameter types do not match a method in the class
     * @param returnType the type of data returned by the class
     * @param args example arguments for the methods (actual values, not types)
     * @param strict setting 'true' considers primitives and their object equivalents to be different. False matches primitive return types with their object counterparts
     */
    private boolean testExistence(Class<?> returnType,  Object[] args, boolean strict) {

        boolean exists = true;

        if (!methodMatchingNameFound()){
            handler.notFound(methodName);
            exists = false;
        }

        if (!methodMatchingNameAndReturnTypeFound(returnType, strict)) {
            handler.incorrectReturnType(methodName,returnType);
            exists = false;
        }

        final Class[] argTypes = helper.classesForArgs(args);

        if (!methodFound(returnType,argTypes)){
            exists = false;
            if (!helper.methodsWithSignature(returnType,false, strict, argTypes).isEmpty()){
                handler.incorrectParamOrder(methodName,argTypes);
            }else {
                handler.incorrectParameters(methodName, argTypes);
            }
        }
        return exists;
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


}
