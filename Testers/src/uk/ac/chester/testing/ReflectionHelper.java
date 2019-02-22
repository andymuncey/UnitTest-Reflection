package uk.ac.chester.testing;

import java.lang.reflect.*;
import java.util.*;

 class ReflectionHelper<C> {

    final private Class<C> searchClass;

    ReflectionHelper(Class<C> searchClass) {
        this.searchClass = searchClass;
    }

    //region fields

    /**
     * Gets all fields within the class
     * @return a Set of Field objects
     */
    Set<Field> fields(){
        Set<Field> fields = new HashSet<>();
        Collections.addAll(fields,searchClass.getDeclaredFields());
        return fields;
    }
    //endregion


    //region method invocation
    /**
     * Calls {@link #invokeMethod(boolean, Class, String, Object...)} with allowAutoboxing set to true
     */
    <T> T invokeMethod(Class<T> returnType, String methodName, Object... args) {
        return invokeMethod(true, returnType, methodName, args);
    }

    /**
     * Attempts to invoke a method matching the specified returnType and name, with the supplied values
     *
     * @param returnType the type of data returned by the method you wish to invoke, primitives types will be matched with non-primitive equivalents (e.g. int and Integer)
     * @param allowAutoboxing     setting 'false' considers primitives and their object equivalents to be different when considering return type. True matches primitive return types with their object counterparts
     * @param methodName the name of the method to invoke (excluding return type, parameters and parentheses), e.g. "myMethod"
     * @param args       a list of arguments (any primitives will be converted to non-primitive types)
     *                   If the argument is a single array, you need to cast it as an Object (so it's not interpreted a multiple arguments)
     * @param <T>        type of data returned will match the return type specified
     * @return the result of invoking the method
     */
    <T> T invokeMethod(boolean allowAutoboxing, Class<T> returnType, String methodName, Object... args) {

        final Set<Method> possibleMethods = findMethods(returnType, methodName, allowAutoboxing);

        for (Method m : possibleMethods) {
            m.setAccessible(true); //allows evaluation of private method
            final Class<?>[] paramTypes = m.getParameterTypes();
            if (args.length == paramTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < args.length; i++) {
                    Class<?> paramClass = !allowAutoboxing ? paramTypes[i] : classEquivalent(paramTypes[i]);
                    Class<?> argClass = args[i].getClass();
                    if (paramClass != argClass) {
                        matchedParams = false;
                    }
                }
                if (matchedParams) {
                    try {
                        Constructor constructor = searchClass.getDeclaredConstructor();
                        constructor.setAccessible(true); //ensures private classes can be tested
                        Object classInstance = constructor.newInstance();

                        Object result = m.invoke(classInstance, args);
                        if (returnType.isInstance(result)){
                            return returnType.cast(result);
                        }
                    } catch (IllegalAccessException e) {
                        //method is not accessible (i.e. private etc.) - should not occur
                        System.err.println(e.getMessage());
                    } catch (InvocationTargetException e) {
                        //invocationTarget exception: the method itself has thrown an exception - error in student code
                        //throw unchecked exception, like the original method would, so student sees reason for error
                        throw new RuntimeException(e.getCause());
                    } catch (InstantiationException e) {
                        //failed while attempting to instantiate something (e.g. call a constructor)
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        //method not found, should not get this far as method has been retrieved from class
                        System.err.println("Method not found in "+searchClass.getSimpleName() +"class. More info: " + e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    //endregion

    //region method location

    /**
     * Finds methods matching a particular name
     *
     * @param name method name (case sensitive), no brackets e.g. "myMethod"
     * @param ignoreCase whether to ignore the case of the method name
     * @return Set of methods matching that name
     */
    Set<Method> findMethods(String name, boolean ignoreCase) {
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
     * @param name       the name of the method
     * @param returnType the 'class' type returned by the method
     * @param allowAutoboxing     setting 'false' considers primitives and their object equivalents to be different. True matches primitive return types with their object counterparts
     * @return a set of methods matching the search criteria
     */
    Set<Method> findMethods(Class<?> returnType, String name, boolean allowAutoboxing) {
        final Set<Method> methods = findMethods(name, false);
        if (!allowAutoboxing) {
            methods.removeIf(m -> (!m.getReturnType().equals(returnType)));
        } else {
            methods.removeIf(m -> (!classEquivalent(m.getReturnType()).equals(classEquivalent(returnType))));
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
    Set<Method> findMethods(Class<?> returnType, String name) {
        return findMethods(returnType, name, false);
    }


    /**
     * Returns all methods with a specified return type and the specified parameter types
     * Primitive types will be matched to their boxed equivalents
     * @param desiredReturnType the return type
     * @param desiredParamTypes the parameter types
     * @return A Set of Method objects
     */
    Set<Method> methodsWithSignature(Class<?> desiredReturnType, Class<?>... desiredParamTypes) {
        return methodsWithSignature(desiredReturnType, true, true, desiredParamTypes);
    }

    /**
     * Returns a list of methods which match the return type and parameters
     * @param returnType      class representing the type of data returned by the method
     * @param matchParamOrder whether the parameters must appear in the order supplied
     * @param allowAutoboxing whether the parameters and return types can be interchanges with their primitive/boxed equivalents
     * @param argTypes        the types for each parameter in the methods parameters. if the only item is an Array, it must be cast as an object
     * @return An arrayList of {@code Method} objects that match the required signature
     */
    Set<Method> methodsWithSignature(Class<?> returnType, boolean matchParamOrder, boolean allowAutoboxing, Class<?>... argTypes) {

        //must copy to avoid argTypes being reordered
        Class<?>[] desiredParamTypes = !allowAutoboxing ? argTypes.clone() : classEquivalents(argTypes);
        Class desiredReturnType = !allowAutoboxing ? returnType : classEquivalent(returnType);

        if (!matchParamOrder){
            sortParamsTypesByName(desiredParamTypes);
        }
        HashSet<Method> methods = new HashSet<>();

        for (Method method : searchClass.getDeclaredMethods()) {

            Class<?> actualReturnType = !allowAutoboxing ? method.getReturnType() : classEquivalent(method.getReturnType());

            if (actualReturnType.equals(desiredReturnType)) {
                Class<?>[] actualParamTypes = !allowAutoboxing ? method.getParameterTypes() : classEquivalents(method.getParameterTypes());

                if (!matchParamOrder) {
                    sortParamsTypesByName(actualParamTypes);
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
    Optional<Method> findMethod(Class returnType, String name, Class... paramTypes) {

        return findMethod(returnType, name, true, paramTypes);

    }

    /**
     * Finds the parameter names for a method
     * <important>Requires the compiler -parameters switch to be set</important>
     * @param returnType The return type of the method
     * @param name       the method name
     * @param paramTypes the types of the parameters the method should have
     * @return an array of Strings with the names of the parameters
     */
    String[] methodParamNames(Class returnType, String name, Class... paramTypes){
        Optional<Method> possibleMethod = findMethod(returnType,name,paramTypes);
        if (possibleMethod.isPresent()){
           return parameterNames(possibleMethod.get());
        }
        return new String[0];
    }

    /**
     * Finds a method matching specified criteria
     * @param returnType the type returned by the method
     * @param name the name of the method (case sensitive)
     * @param allowAutoboxing set to false if the parameter types must match exactly or true if primitive and boxed types can be used interchangeably
     * @param paramTypes the types of the parameters accepted by the method
     * @return an Optional containing the method, if found
     */
    Optional<Method> findMethod(Class returnType, String name, boolean allowAutoboxing, Class... paramTypes) {
        Set<Method> methods = findMethods(returnType, name, allowAutoboxing);
        Class[] desiredParamTypes = !allowAutoboxing ? paramTypes : classEquivalents(paramTypes);

        for (Method m : methods) {
            Class<?>[] methodParamTypes = m.getParameterTypes();
            if (desiredParamTypes.length == methodParamTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < desiredParamTypes.length; i++) {

                    if (desiredParamTypes[i] != (!allowAutoboxing ? methodParamTypes[i] : classEquivalent(methodParamTypes[i]))) {
                        matchedParams = false;
                    }
                }
                if (matchedParams) {
                    return Optional.of(m);
                }
            }
        }
        return Optional.empty();
    }

    //endregion

    //region constructors

    /**
     * Finds a constructor matching specified criteria
     * @param includeNonPublic include constructors not marked as public
     * @param allowAutoboxing set to false if the parameter types must match exactly or true if primitive and boxed types can be used interchangeably
     * @param matchParamOrder set to true if the order of parameters in the constructor must match the order they are passed into this method.
     *                        Note that if this is set to false, the first constructor with matching parameters would be returned, even if there is a better match
     * @param params the type of params the constructor should take
     * @return An Optional containing a matching constructor, if found
     */
    Optional<Constructor<C>> constructorForParamTypes(boolean includeNonPublic, boolean allowAutoboxing, boolean matchParamOrder, Class... params){
        @SuppressWarnings("unchecked") //cast declared constructors not of type, but must be given the class in which they appear
        Constructor<C>[] constructorsArray = (Constructor<C>[]) (includeNonPublic ? searchClass.getDeclaredConstructors() : searchClass.getConstructors());

        Set<Constructor<C>> constructors = new HashSet<>(Arrays.asList(constructorsArray));
        constructors.removeIf(Constructor::isSynthetic);

        if (!matchParamOrder){
            sortParamsTypesByName(params);
        }

        for (Constructor<C> c: constructors) {
            if (params.length == c.getParameterCount()){

                Class[] actualParamTypes = c.getParameterTypes();
                if (!matchParamOrder){
                    sortParamsTypesByName(actualParamTypes);
                }
                boolean matchedParams = true;
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != (allowAutoboxing ?  classEquivalents(actualParamTypes)[i] : actualParamTypes[i] )){
                        matchedParams = false;
                        break;
                    }
                }
                if (matchedParams) {
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a constructor matching specified criteria
     * @param includeNonPublic include constructors not marked as public
     * @param matchParamOrder set to true if the order of parameters in the constructor must match the order they are passed into this method
     * @param args example arguments for the constructor to take (the types of which will be used to locate the constructor)
     * @return An Optional containing a matching constructor, if found
     */
    Optional<Constructor<C>> constructorForArgTypes(boolean includeNonPublic, boolean matchParamOrder, Object... args ){
        Class[] desiredParamTypes = classesForArgs(args);
        return constructorForParamTypes(includeNonPublic,true,matchParamOrder,desiredParamTypes);
    }
    //endregion

    //region helpers and conversions

    /**
     * given an array of arguments (values) returns an array of the same size representing the types (as classes) of each argument
     *
     * @param args an array of values of any type
     * @return an array of Class objects
     */
    static Class[] classesForArgs(Object[] args) {
        List<Class> params = new ArrayList<>();
        Arrays.asList(args).forEach((arg) -> params.add(arg.getClass()));
        return params.toArray(new Class[args.length]);
    }

    /**
     * Given the 'class' of a primitive type (e.g. int.class returns the class of the corresponding boxed type, e.g. Integer.class)
     * Classes that do not belong to primitive types will remain unmodified
     *
     * @param primitiveClass a primitive 'class' such as double.class
     * @return the class of the boxed equivalent (e.g. char.class becomes Character.class)
     */
    private static Class classEquivalent(Class primitiveClass) {
        final Class[] primitives = {boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class};
        final Class[] classes = {Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class};

        for (int i = 0; i < primitives.length; i++) {
            if (primitiveClass == primitives[i]) {
                return classes[i];
            }
        }
        return primitiveClass; //not actually a primitive
    }

    /**
     * Returns an array with the results of calling {@link #classEquivalent} on each item
     *
     * @param primitiveClasses an array of Class object, which should include the class for some Primitive types
     * @return an array of Class, each corresponding to an object type
     */
    private static Class[] classEquivalents(Class[] primitiveClasses) {
        Class[] classClasses = new Class[primitiveClasses.length];
        for (int i = 0; i < primitiveClasses.length; i++) {
            classClasses[i] = classEquivalent(primitiveClasses[i]);
        }
        return classClasses;
    }

    /**
     * Determines if two types are the same
     * @param classA the first class
     * @param classB the second class
     * @return true if types are equal, or one is the boxed equivalent of the other, false otherwise
     */
    static boolean equivalentType(Class classA, Class classB){
            return classEquivalent(classA).equals(classEquivalent(classB));
    }

    /**
     * Returns the parameter names for an executable, such as a method or constructor
     * <important>Requires the compiler -parameters switch to be set</important>
     * @param executable e.g. a method or constructor
     * @return an array of Strings with the parameter names
     */
    static String[] parameterNames (Executable executable){
        Parameter[] params = executable.getParameters();
        String[] paramNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            paramNames[i] = params[i].getName();
        }
        return paramNames;
    }

    /**
     * Sorts an array of Class by canonical name (i.e. including package name)
     * @param paramTypes an array of types
     */
    private static void sortParamsTypesByName(Class<?>[] paramTypes){
        Arrays.sort(paramTypes, Comparator.comparing(Class::getCanonicalName));
    }

    //endregion
}
