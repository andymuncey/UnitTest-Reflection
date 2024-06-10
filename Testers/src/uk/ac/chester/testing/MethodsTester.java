package uk.ac.chester.testing;


import org.jetbrains.annotations.Nullable;
import uk.ac.chester.testing.reflection.FieldsHelper;
import uk.ac.chester.testing.reflection.MethodsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Tests methods which may not be implemented in a given class.
 * Known issues:
 *  * Passing an array as the only argument to the testExistence method is ambiguous as it could be inferred as an array object or varargs
 * @param <C> type of the class that's being tested
 */
public class MethodsTester<C> extends Tester {

    private final MethodsHelper<C> helper;
    private final EventHandler handler;
    private final Class<C> searchClass;

    /**
     * The type parameters should be the type of the class that's being tested,
     * and the reference type corresponding to type the tested method returns (e.g. for a return type of int, specify Integer)
     * @param searchClass     the class in which the method should be written
     * @param handler         a {@link EventHandler} for handling non-existent methods
     */
    public MethodsTester(Class<C> searchClass, EventHandler handler) {
        this.helper = new MethodsHelper<>(searchClass);
        this.handler = handler;
        this.searchClass = searchClass;
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
         return helper.findMethod(modifier, null, returnType,  methodName,true,paramTypes).isPresent();
    }

    /**
     * Indicates if a method with the corresponding return type and parameter types is found (not strict with regard to class / primitive types)
     * @param modifier the expected access modifier for the method
     * @param isStatic whether the method is static or not
     * @param returnType the return type of the method
     * @param paramTypes the parameter types of the method
     * @return true if a method is found matching the specification
     */
    private boolean methodFound(AccessModifier modifier, Boolean isStatic, Class returnType, String methodName, Class[] paramTypes){
        return helper.findMethod(modifier, isStatic, returnType,  methodName,true,paramTypes).isPresent();
    }



    public <T> T executeStaticWithSpecifics(boolean allowAutoboxing, AccessModifier modifier, Class<T> returnTypeClass, String methodName, Object... args){
        return testExistenceForValues(allowAutoboxing, modifier, true, returnTypeClass,methodName, args) ? helper.invokeStaticMethod(allowAutoboxing,returnTypeClass,methodName,args) : null ;
    }


    /**
     * Executes a static method, allowing autoboxing/unboxing, and any access modifier
     * @param returnType the expected return type of the method to be called
     * @param methodName name of the method (null to use the name of the calling method)
     * @param args args to pass to the method (must cast arrays as objects)
     * @param <T> the type of class returned
     * @return there result of executing the method
     */
    public <T> T executeStatic(Class<T> returnType, String methodName, Object... args){

        //Finds the name of the calling method
              if (methodName == null){
                  //adapted from Nathan (2021) https://stackoverflow.com/questions/4065518/java-how-to-get-the-caller-function-name
                  //noinspection OptionalGetWithoutIsPresent
                  methodName = StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().get()).getMethodName();
        }

        return executeStaticWithSpecifics(true,null,returnType,methodName,args);
    }




//    /**
//     * If the method exists, method is invoked, and the value returned
//     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
//     * @param allowAutoboxing whether the return type must be an exact match (Integer and int would not be considered an exact match)
//     * @param args arguments to invoke the method with
//     * @return the result of invoking the method (or null)
//     */
//    private boolean testExistence(boolean allowAutoboxing, AccessModifier modifier, Class returnTypeClass, String methodName, Object[] args){
//        return testExistence(allowAutoboxing, modifier, returnTypeClass,methodName, args) ;
////        return  helper.invokeMethod(returnTypeClass, methodName, args);
//    }


    /*
    Tests for the existence of a method matching the supplied criteria
     */
    public boolean testExistenceForValues(@Nullable AccessModifier modifier, Class returnTypeClass, String methodName, Object... args){
        return testExistenceForValues(true, modifier, null, returnTypeClass,methodName, args);
    }


    /*
    Tests for the existence of a method matching the supplied criteria
     */
    public boolean testExistenceForValues(boolean isStatic, Class returnTypeClass, String methodName, Object... args) {
        return testExistenceForValues(true, null,isStatic,returnTypeClass,methodName,args);
    }

    /*
 Tests for the existence of a method matching the supplied criteria
  */
    public boolean testExistence(boolean isStatic, Class returnTypeClass, String methodName, Class... paramTypes) {
        return testExistence(true, null,isStatic,returnTypeClass,methodName,paramTypes);
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
//    public <R> R testExistence(Class<R> returnTypeClass, String methodName){
//        return testExistence(null, returnTypeClass, methodName);
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
    public boolean testExistenceForValues(Class returnTypeClass, String methodName, Object... args){
        return testExistenceForValues(null, returnTypeClass, methodName, args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type will not be matched to boxed/unboxed type
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param modifier An AccessModifier indicating the desired access modifier, or null if this is unimportant
     * @param returnTypeClass the type the method is expected to return
     * @param methodName the name of the method
     * @param args arguments to invoke the method with
     * @return true if the method exists, false otherwise
     */
    public boolean testForExactReturnType(AccessModifier modifier, Class returnTypeClass, String methodName,Object... args){
        return testExistenceForValues(false, modifier, null, returnTypeClass,methodName, args);
    }

    /**
     * If the method exists, method is invoked, and the value returned. Return type will not be matched to boxed/unboxed type
     * If the method is not found, an appropriate {@link EventHandler} event will fire and null is returned
     * @param returnTypeClass the type the method is expected to return
     * @param methodName the name of the method
     * @param args arguments to invoke the method with
     * @return true if the method exists, false otherwise
     */
    public boolean testForExactReturnType(Class returnTypeClass, String methodName, Object... args){
        return testForExactReturnType(null,returnTypeClass,methodName,args);
    }


    public boolean testExistenceForValues(boolean allowAutoboxing, @Nullable AccessModifier accessModifier, @Nullable Boolean isStatic, Class<?> returnType, String methodName, Object... args) {
        final Class[] argTypes = Utilities.classesForArgs(args);
        return testExistence(allowAutoboxing,accessModifier,isStatic,returnType,methodName,argTypes);

    }



    /**
     * Tests the following, in relation to the method, returning null soon as any condition is not met:
     * -That a method with a matching name, but not necessarily with the correct case exists
     * -That a method with an exact match to the name exists
     * -That the method returns the correct type
     * -That the method has the correct parameters, though not necessarily in the correct order
     * -That the method has the correct parameters, in the correct order
     * -That the method has the correct access modifier
     * -That the method is or is not static as specified by the isStatic parameter
     * @param allowAutoboxing setting 'false' considers primitives and their object equivalents to be different. True matches primitive return types with their object counterparts
     * @param accessModifier whether the method should be private, package-private, protected or public. May be null
     * @param isStatic whether the method should be static, use null if not important. May be null
     * @param returnType the type of data returned by the class
     * @param methodName the name of the method
     * @param paramTypes arguments for passing to the method (actual values, not types)
     */
    public boolean testExistence(boolean allowAutoboxing, @Nullable AccessModifier accessModifier, @Nullable Boolean isStatic, Class<?> returnType, String methodName, Class... paramTypes) {

        //Name
        if (!methodMatchingNameFound(methodName)){
            if (methodMatchingCaseInsensitiveNameFound(methodName)){

                handler.wrongCaseName(methodName);
            } else {
                handler.notFound(methodName, searchClass);
            }
            return false;
        }

        //Return type
        if (!methodMatchingNameAndReturnTypeFound(returnType, methodName, allowAutoboxing)) {
            handler.incorrectReturnType(methodName,returnType);
            return false;
        }

        //check for correct number of params
        Set<Method> methods = helper.findMethods(allowAutoboxing, returnType,methodName);
        boolean matchedParameterCount = false;
        for (Method method : methods){
            if (method.getParameterCount() == paramTypes.length){
                matchedParameterCount = true;
                break;
            }
        }
        if (!matchedParameterCount){
            handler.incorrectNumberOfParameters(methodName,paramTypes.length);
            return false;
        }

        //found method with correct name, return type, but wrong type or order of params


        if (!methodFound(allowAutoboxing, returnType, methodName,paramTypes)){
            //not found an exact match

            Set<Method> similarMethods = helper.methodsWithSignature(allowAutoboxing, returnType,false, paramTypes);
            similarMethods.removeIf(m -> !m.getName().equals(methodName));


            if (!similarMethods.isEmpty()){
                //found a method
                handler.incorrectParamOrder(methodName,paramTypes);
            }else {
                handler.incorrectParameters(methodName, paramTypes);
            }
            return false;
        }


        if (accessModifier != null){
            if (!methodFound(accessModifier,returnType,methodName, paramTypes)){
                handler.accessModifierIncorrect(methodName,accessModifier);
                return false;
            }

        }

        //static not correct
        if (isStatic != null) {
            if (!methodFound(accessModifier,isStatic,returnType,methodName,paramTypes)){
                handler.staticDeclarationIncorrect(methodName,isStatic);
                return false;
            }
        }

        //found method
        String[] paramNames = helper.methodParamNames(returnType,methodName,paramTypes);
        for (String paramName: paramNames){
            if (!getConventionChecker().validVariableName(paramName)){
                handler.paramNameUnconventional(methodName, paramName);
            }
        }
        return true;
    }


    /**
     * Returns a set of methods where the method name matches a field name (first letter capitalised) prefixed with
     * 'get' (for non-boolean types), or 'is' (for boolean types). If specified, alternate prefixes for booleans can be specified
     * Will allow methods which return the boxed/unboxed type (e.g. the field could hold an int and the getter return Integer
     * @param booleanPrefixes specify allowable prefixes for boolean, or null to use 'is' alone
     * @return a set of methods which, due to the type returned and the name, are likely to be getters
     */
    public Set<Method> getters(String... booleanPrefixes){

        if (booleanPrefixes.length == 0){
            booleanPrefixes = new String[]{"is"};
        }

        FieldsHelper<C> fieldshelper = new FieldsHelper<>(searchClass);
        Set<Field> fields = fieldshelper.fields();
        Set<Method> getters = new HashSet<>();
        for (Field field: fields){

            if (field.getType() == Boolean.class || field.getType() == boolean.class){
            for (String prefix:booleanPrefixes) {
                String getterName = prefix + capitaliseFirstLetter(field.getName());
                Optional<Method> method = helper.findMethod(AccessModifier.PUBLIC,false,field.getType(),getterName,true);
                if (method.isPresent()){
                    getters.add(method.get());
                   break;
                }
            }
            } else {
                //not a boolean
                String getterName = "get" + capitaliseFirstLetter(field.getName());
                Optional<Method> method = helper.findMethod(AccessModifier.PUBLIC,false,field.getType(),getterName,true);
                if (method.isPresent()){
                    getters.add(method.get());
                    break;
                }
            }

        }
        return getters;
    }


    public Set<Method> setters(){
        FieldsHelper<C> fieldshelper = new FieldsHelper<>(searchClass);
        Set<Field> fields = fieldshelper.fields();
        Set<Method> setters = new HashSet<>();
        for (Field field: fields){
            String setterName = "set" + capitaliseFirstLetter(field.getName());
            Optional<Method> method = helper.findMethod(AccessModifier.PUBLIC,false,void.class,setterName,true,field.getType());
            if (method.isPresent()){
                setters.add(method.get());
                break;
            }
        }
        return setters;
    }


    private static String capitaliseFirstLetter(String word){
        if (word.isEmpty()) {
            return word;
        }
        if (word.length() == 1){
            return word.toUpperCase(Locale.ROOT);
        }
        return word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1);
    }




    /**
     * Allows callbacks to indicate problems when attempting to testExistence methods that do not exist
     * It is anticipated that <code>Assert.fail</code> will be used in the implementation of each method to provide a description
     */
    public interface EventHandler {
        /**
         * Cannot find a method with the correct name
         * @param methodName the name of the method as it should be
         */
        void notFound(String methodName, Class searchClass);

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
         * A method has been found, but it does not have the expected number of parameters
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


        /**
         * Correct parameters exist for the method, but it is either marked as static and shouldn't be or vice versa
         * @param methodName the name of the method
         * @param requiredStatic whether the method should be static
         */
        void staticDeclarationIncorrect(String methodName, boolean requiredStatic);


    }
}
