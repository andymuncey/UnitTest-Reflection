package uk.ac.chester.Testing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ReflectionHelper {

    final private Class<?> searchClass;

    //region constructors
    public ReflectionHelper(Object o) {
        this(o.getClass());
    }

    public ReflectionHelper(Class searchClass) {
        this.searchClass = searchClass;
    }

    /**
     * Returns a ReflectionHelper given a fully qualified class name
     *
     * @param name the fully qualified name of a class, e.g. java.lang.String
     * @return a ReflectionHelper object for the named class, or null if the Class cannot be found
     */
    static ReflectionHelper forClassName(String name) {
        try {
            Class c = Class.forName(name);
            return new ReflectionHelper(c);
        } catch (Exception e) {
            return null;
        }
    }
    //endregion

    //region method invocation

    /**
     * Calls {@link #invokeMethod(boolean, Class, String, Object...)} with strict set to false
     */
    <T> T invokeMethod(Class<T> returnType, String methodName, Object... args) {
        return invokeMethod(false, returnType, methodName, args);
    }

    /**
     * Attempts to invoke a method matching the specified returnType and name, with the supplied values
     *
     * @param returnType the type of data returned by the method you wish to invoke, primitives types will be matched with non-primitive equivalents (e.g. int and Integer)
     * @param strict     setting 'true' considers primitives and their object equivalents to be different when considering return type. False matches primitive return types with their object counterparts
     * @param methodName the name of the method to invoke (excluding return type, parameters and parentheses), e.g. "myMethod"
     * @param args       a list of arguments (any primitives will be converted to non-primitive types)
     *                   If the argument is a single array, you need to cast it as an Object (so it's not interpreted a multiple arguments)
     * @param <T>        type of data returned will match the return type specified
     * @return the result of invoking the method
     */
    <T> T invokeMethod(boolean strict, Class<T> returnType, String methodName, Object... args) {

        final Set<Method> possibleMethods = findMethods(returnType, methodName, strict);

        for (Method m : possibleMethods) {

            final Class<?>[] paramTypes = m.getParameterTypes();
            if (args.length == paramTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < args.length; i++) {
                    Class<?> paramClass = strict ? paramTypes[i] : classEquivalent(paramTypes[i]);
                    Class<?> argClass = args[i].getClass();
                    if (paramClass != argClass) {
                        matchedParams = false;
                    }
                }
                if (matchedParams) {
                    try {
                        Object classInstance = searchClass.getDeclaredConstructor().newInstance();
                        m.setAccessible(true); //allows testing of private method
                        Object result = m.invoke(classInstance, args);
                        return (T) result;
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
     * @return Set of methods matching that name
     */
    Set<Method> findMethods(String name) {
        final HashSet<Method> methods = new HashSet<>();
        for (Method method : searchClass.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                methods.add(method);
            }
        }
        return methods;
    }

    Set<Method> findMethodsIgnoreCase(String name) {
        final HashSet<Method> methods = new HashSet<>();
        for (Method method : searchClass.getDeclaredMethods()) {
            if (method.getName().toUpperCase().equals(name.toUpperCase())) {
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
     * @param strict     setting 'true' considers primitives and their object equivalents to be different. False matches primitive return types with their object counterparts
     * @return a set of methods matching the search criteria
     */
    Set<Method> findMethods(Class<?> returnType, String name, boolean strict) {
        final Set<Method> methods = findMethods(name);
        if (strict) {
            methods.removeIf(m -> (!m.getReturnType().equals(returnType)));
        } else {
            methods.removeIf(m -> (!classEquivalent(m.getReturnType()).equals(classEquivalent(returnType))));
        }
        return methods;
    }

    /**
     * Returns methods where the name and return type match.
     * Primitive types will be matched to their class equivalents
     *
     * @param name       the name of the method
     * @param returnType the 'class' type returned by the method
     * @return a set of methods matching the search criteria
     */
    Set<Method> findMethods(Class<?> returnType, String name) {
        return findMethods(returnType, name, true);
    }


    Set<Method> methodsWithSignature(Class<?> desiredReturnType, Class<?>... desiredParamTypes) {
        return methodsWithSignature(desiredReturnType, true, false, desiredParamTypes);
    }

    /**
     * Returns a list of methods which match the return type and parameters
     *
     * @param returnType      class representing the type of data returned by the method
     * @param matchParamOrder whether the parameters must appear in the order supplied
     * @param argTypes        the types for each parameter in the methods parameters. if the only item is an Array, it must be cast as an object
     * @return An arrayList of {@code Method} objects that match the required signature
     */
    Set<Method> methodsWithSignature(Class<?> returnType, boolean matchParamOrder, boolean strict, Class<?>... argTypes) {

        //must copy to avoid argTypes being reordered
        Class<?>[] desiredParamTypes = strict ? argTypes.clone() : classEquivalents(argTypes);
        Class desiredReturnType = strict ? returnType : classEquivalent(returnType);

        HashSet<Method> methods = new HashSet<>();

        for (Method method : searchClass.getDeclaredMethods()) {

            Class<?> actualReturnType = strict ? method.getReturnType() : classEquivalent(method.getReturnType());

            if (actualReturnType.equals(desiredReturnType)) {
                Class<?>[] actualParamTypes = strict ? method.getParameterTypes() : classEquivalents(method.getParameterTypes());

                Comparator<Class<?>> comparator = new Comparator<Class<?>>() {
                    @Override
                    public int compare(Class<?> o1, Class<?> o2) {
                        return o1.getCanonicalName().compareTo(o2.getCanonicalName());
                    }
                };

                Class[] possiblySortedDesiredParamTypes = desiredParamTypes.clone();
                if (!matchParamOrder) {
                    Arrays.sort(actualParamTypes, comparator);

                    Arrays.sort(desiredParamTypes, comparator);
                }

                if (Arrays.equals(actualParamTypes, desiredParamTypes)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }


    /**
     * Finds a method with a given return type, name and
     *
     * @param returnType The return type of the method
     * @param name       the method name
     * @param paramTypes the types of the parameters the method should have
     * @return An Optional, containing the method if found, or empty if not found
     */
    Optional<Method> findMethod(Class returnType, String name, Class... paramTypes) {

        return findMethod(returnType, name, false, paramTypes);

    }


    /**
     * Finds the parameter names for a method
     * @param returnType The return type of the method
     * @param name       the method name
     * @param paramTypes the types of the parameters the method should have
     * @return an array of Strings with the names of the parameters
     */
    String[] methodParamNames(Class returnType, String name, Class... paramTypes){

        Optional<Method> possibleMethod = findMethod(returnType,name,paramTypes);

        if (possibleMethod.isPresent()){
           Parameter[] params = possibleMethod.get().getParameters();
           String[] paramNames = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                paramNames[i] = params[i].getName();
            }
            return paramNames;
        }
        return new String[0];
    }


    Optional<Method> findMethod(Class returnType, String name, boolean strict, Class... paramTypes) {
        Set<Method> methods = findMethods(returnType, name, strict);
        Class[] desiredParamTypes = strict ? paramTypes : classEquivalents(paramTypes);

        for (Method m : methods) {
            Class<?>[] methodParamTypes = m.getParameterTypes();
            if (desiredParamTypes.length == methodParamTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < desiredParamTypes.length; i++) {

                    if (desiredParamTypes[i] != (strict ? methodParamTypes[i] : classEquivalent(methodParamTypes[i]))) {
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


    //region conversions

    /**
     * given an array of arguments (values) returns an array of the same size representing the types (as classes) of each argument
     *
     * @param args an array of values of any type
     * @return an array of Class objects
     */
    Class[] classesForArgs(Object[] args) {
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
    private Class classEquivalent(Class primitiveClass) {
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
    private Class[] classEquivalents(Class[] primitiveClasses) {
        Class[] classClasses = new Class[primitiveClasses.length];
        for (int i = 0; i < primitiveClasses.length; i++) {
            classClasses[i] = classEquivalent(primitiveClasses[i]);
        }
        return classClasses;
    }

    //endregion

}
