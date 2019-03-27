package uk.ac.chester.testing.reflection;

import uk.ac.chester.testing.AccessModifier;

import java.lang.reflect.*;
import java.util.*;

 public class MethodsHelper<C> {

    final private Class<C> searchClass;

    public MethodsHelper(Class<C> searchClass) {
        this.searchClass = searchClass;
    }


    //region method location

    /**
     * Finds methods matching a particular name
     *
     * @param name method name (case sensitive), no brackets e.g. "myMethod"
     * @param ignoreCase whether to ignore the case of the method name
     * @return Set of methods matching that name
     */
    public Set<Method> findMethods(String name, boolean ignoreCase) {
        final Set<Method> methods = new HashSet<>();
        for (Method method : searchClass.getDeclaredMethods()) {
            String searchMethodName = ignoreCase ? name.toUpperCase() : name;
            String actualMethodName = ignoreCase ? method.getName().toUpperCase() : method.getName();
            if (searchMethodName.equals(actualMethodName)) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * Returns methods where the name and return type match.
     *
     * @param allowAutoboxing     setting 'false' considers primitives and their object equivalents to be different. True matches primitive return types with their object counterparts
     * @param returnType the 'class' type returned by the method
     * @param name       the name of the method
     * @return a set of methods matching the search criteria
     */
    public Set<Method> findMethods(boolean allowAutoboxing, Class<?> returnType, String name) {
        final Set<Method> methods = findMethods(name, false);
        if (!allowAutoboxing) {
            methods.removeIf(m -> (!m.getReturnType().equals(returnType)));
        } else {
            methods.removeIf(m -> (!Utilities.classEquivalent(m.getReturnType()).equals(Utilities.classEquivalent(returnType))));
        }
        return methods;
    }


     /**
     * Returns methods where the name and return type match.
     * Primitive types will be matched to their class equivalents
     * @param name       the name of the method
     * @param returnType the 'class' type returned by the method
     * @return a set of methods matching the search criteria
     */
    public Set<Method> findMethods(Class<?> returnType, String name) {
        return findMethods(false, returnType, name);
    }


    /**
     * Returns all methods with a specified return type and the specified parameter types
     * Primitive types will be matched to their boxed equivalents
     * @param desiredReturnType the return type
     * @param desiredParamTypes the parameter types
     * @return A Set of Method objects
     */
    Set<Method> methodsWithSignature(Class<?> desiredReturnType, Class<?>... desiredParamTypes) {
        return methodsWithSignature(true, desiredReturnType, true, desiredParamTypes);
    }

    /**
     * Returns a list of methods which match the return type and parameters
     * @param allowAutoboxing whether the parameters and return types can be interchanges with their primitive/boxed equivalents
     * @param returnType      class representing the type of data returned by the method
     * @param matchParamOrder whether the parameters must appear in the order supplied
     * @param argTypes        the types for each parameter in the methods parameters. if the only item is an Array, it must be cast as an object
     * @return An arrayList of {@code Method} objects that match the required signature
     */
    public Set<Method> methodsWithSignature(boolean allowAutoboxing, Class<?> returnType, boolean matchParamOrder, Class<?>... argTypes) {

        //must copy to avoid argTypes being reordered
        Class<?>[] desiredParamTypes = !allowAutoboxing ? argTypes.clone() : Utilities.classEquivalents(argTypes);
        Class desiredReturnType = !allowAutoboxing ? returnType : Utilities.classEquivalent(returnType);

        if (!matchParamOrder){
            Utilities.sortParamsTypesByName(desiredParamTypes);
        }
        HashSet<Method> methods = new HashSet<>();

        for (Method method : searchClass.getDeclaredMethods()) {

            Class<?> actualReturnType = !allowAutoboxing ? method.getReturnType() : Utilities.classEquivalent(method.getReturnType());

            //todo: handle boxing of arrays of primitives
            if (actualReturnType.equals(desiredReturnType)) {
           // if (allowAutoboxing ? equivalentType(method.getReturnType(),desiredReturnType) : method.getReturnType().equals(desiredReturnType)) {
                Class<?>[] actualParamTypes = !allowAutoboxing ? method.getParameterTypes() : Utilities.classEquivalents(method.getParameterTypes());

                if (!matchParamOrder) {
                    Utilities.sortParamsTypesByName(actualParamTypes);
                }



                if (Arrays.equals(actualParamTypes, desiredParamTypes)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    /**
     * Finds a method with a given return type, name and parameter types.
     * Will return methods where primitive types are substituted for box types and vice versa
     *
     * @param returnType The return type of the method
     * @param name       the method name
     * @param paramTypes the types of the parameters the method should have
     * @return An Optional, containing the method if found, or empty if not found
     */
    public Optional<Method> findMethod(Class returnType, String name, Class... paramTypes) {

        return findMethod(true, returnType, name, paramTypes);

    }


    public Optional<Method> findMethod(AccessModifier modifier, Class returnType, String name, boolean allowAutoboxing, Class... paramTypes){
        Optional<Method> method = findMethod(allowAutoboxing, returnType, name, paramTypes);
        if (method.isPresent()) {
            if (AccessModifier.accessModifier(method.get()).equals(modifier)){
                return method;
            }
        }
        return Optional.empty();
    }


    /**
     * Finds a method matching specified criteria
     * @param allowAutoboxing set to false if the parameter types / return types must match exactly or true if primitive and boxed types can be used interchangeably
     * @param returnType the type returned by the method
     * @param name the name of the method (case sensitive)
     * @param paramTypes the types of the parameters accepted by the method
     * @return an Optional containing the method, if found
     */
    public Optional<Method> findMethod(boolean allowAutoboxing, Class returnType, String name, Class... paramTypes) {
        Set<Method> methods = findMethods(allowAutoboxing, returnType, name);
        Class[] desiredParamTypes = !allowAutoboxing ? paramTypes : Utilities.classEquivalents(paramTypes);

        for (Method m : methods) {
            m.setAccessible(true); //allows evaluation of private method
            final Class<?>[] methodParamTypes = m.getParameterTypes();
            if (desiredParamTypes.length == methodParamTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < desiredParamTypes.length; i++) {

                    if (allowAutoboxing){
                        if (!Utilities.equivalentType(desiredParamTypes[i],methodParamTypes[i])){
                            matchedParams = false;
                        }
                    } else {
                        if (desiredParamTypes[i] != methodParamTypes[i]){
                            matchedParams = false;
                        }
                    }
//                    if (desiredParamTypes[i] != (allowAutoboxing ?  classEquivalent(methodParamTypes[i]: methodParamTypes[i]))) {
//                        matchedParams = false;
//                    }
                }
                if (matchedParams) {
                    return Optional.of(m);
                }
            }
        }
        return Optional.empty();
    }

//     Optional<Method> methodForParams(boolean allowAutoboxing, Class returnType, String methodName, Object... args) {
//         final Set<Method> possibleMethods = findMethods(returnType, methodName, allowAutoboxing);
//
//         for (Method m : possibleMethods) {
//             m.setAccessible(true); //allows evaluation of private method
//             final Class<?>[] paramTypes = m.getParameterTypes();
//             if (args.length == paramTypes.length) {
//                 boolean matchedParams = true;
//                 for (int i = 0; i < args.length; i++) {
//                     Class<?> paramClass = !allowAutoboxing ? paramTypes[i] : classEquivalent(paramTypes[i]);
//                     Class<?> argClass = args[i].getClass();
//                     if (paramClass != argClass) {
//                         matchedParams = false;
//                     }
//                 }
//                 if (matchedParams) {
//                     return Optional.of(m);
//                 }
//             }
//         }
//         return Optional.empty();
//     }


     /**
      * Finds the parameter names for a method
      * <important>Requires the compiler -parameters switch to be set</important>
      * @param returnType The return type of the method
      * @param name       the method name
      * @param paramTypes the types of the parameters the method should have
      * @return an array of Strings with the names of the parameters
      */
     public String[] methodParamNames(Class returnType, String name, Class... paramTypes){
         Optional<Method> possibleMethod = findMethod(returnType,name,paramTypes);
         if (possibleMethod.isPresent()){
             return Utilities.parameterNames(possibleMethod.get());
         }
         return new String[0];
     }

    //endregion


     //region static method execution

     /**
      * Attempts to invoke a method matching the specified returnType and name, with the supplied values
      * Will use the constructed class if available, else will try and create an instance of the class using the parameterless constructor
      *
      * @param returnType      the type of data returned by the method you wish to invoke, primitives types will be matched with non-primitive equivalents (e.g. int and Integer)
      * @param allowAutoboxing setting 'false' considers primitives and their object equivalents to be different when considering return type. True matches primitive return types with their object counterparts
      * @param methodName      the name of the method to invoke (excluding return type, parameters and parentheses), e.g. "myMethod"
      * @param args            a list of arguments (any primitives will be converted to non-primitive types)
      *                        If the argument is a single array, you need to cast it as an Object (so it's not interpreted a multiple arguments)
      * @param <T>             type of data returned will match the return type specified
      * @return the result of invoking the method
      */
     public <T> T invokeStaticMethod(boolean allowAutoboxing, Class<T> returnType, String methodName, Object... args) {

         final Optional<Method> optionalMethod = findMethod(true, returnType,methodName, Utilities.classesForArgs(args));

         if (optionalMethod.isPresent()) {
             final Method m = optionalMethod.get();
            if (Modifier.isStatic(m.getModifiers())) {
                try {
                    Object result = m.invoke(null, args);
                    if (returnType.isInstance(result)) {
                        return returnType.cast(result);
                    }
                } catch (IllegalAccessException e) {
                    //method is not accessible (i.e. private etc.) - should not occur
                    System.err.println(e.getMessage());
                } catch (InvocationTargetException e) {
                    //invocationTarget exception: the method itself has thrown an exception - error in student code
                    //throw unchecked exception, like the original method would, so student sees reason for error
                    throw new RuntimeException(e.getCause());
                }
            }
         }
         return null;
     }

     //endregion



}
