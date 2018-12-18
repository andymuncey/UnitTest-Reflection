package uk.ac.chester;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionHelper {

    final private Class<Object> searchClass;
    //private boolean matchParamOrder = true;

    //region constructors
    public ReflectionHelper(Object o){ this(o.getClass()); }

    public ReflectionHelper(Class searchClass){ this.searchClass = searchClass; }
    //endregion

    //region method invocation

    /**
     * Attempts to invoke a method matching the specified returnType and name, with the supplied values
     * @param returnType the type of data returned by the method you wish to invoke, primitives types will be matched with non-primitive equivalents (e.g. int and Integer)
     * @param methodName the name of the method to invoke (excluding return type, parameters and parentheses), e.g. "myMethod"
     * @param args a list of arguments (these will be converted to non-primitive types)
     *             If the arguments is a single array, you need to cast it as an Object (so it's not interpreted a multiple arguments)
     * @param <T> type of data returned will match the return type specified
     * @return the result of invoking
     */
    <T> T invokeMethod(Class<T> returnType, String methodName,Object... args){

        final Set<Method> possibleMethods = findMethods(methodName,returnType);

        for (Method m : possibleMethods){

            final Class<?>[] paramTypes = m.getParameterTypes();
            if (args.length == paramTypes.length){
                boolean matchedParams = true;
                for (int i = 0; i < args.length; i++) {
                    Class<?> paramClass = classEquivalent(paramTypes[i]);
                    Class<?> argClass = args[i].getClass();
                    if (paramClass != argClass){
                        matchedParams = false;
                    }
                }
                if (matchedParams){
                    try {
                        Object classInstance = searchClass.getDeclaredConstructor().newInstance();
                        Object result = m.invoke(classInstance, args);
                        return (T)result;

                    }
                    catch (IllegalAccessException e){
                        //method is not accessible (i.e. private etc.)
                        System.err.println(e.getMessage());
                    }
                    catch (InvocationTargetException | InstantiationException e){
                        System.err.println(e.getMessage());
                    }
                    catch (NoSuchMethodException e){
                        System.err.println("can't find constructor: " + e.getMessage());
                    }
                }
            }
        }
        return null;
    }


    //simple version of above method, no primitive -> non primitive type conversion in place
//    <T> T invokeMethod(Class<T> returnType, String methodName,Object... args){
//
//        ArrayList<Class> params = new ArrayList<>();
//        Arrays.asList(args).forEach((arg) -> params.add(arg.getClass()));
//
//        Optional<Method> m = findMethod(returnType,methodName,params.toArray(new Class[args.length]));
//
//        if (m.isPresent()){
//            try{
//                Object classInstance = searchClass.getDeclaredConstructor().newInstance();
//                Object result = m.get().invoke(classInstance, args);
//                return (T)result;
//            }
//            catch (Exception e){
//                System.err.println("an error has occurred");
//            }
//        }
//        return null;
//    }


    /**
     * given an array of arguments (values) returns an array of the same size representing the types (as classes) of each argument
     * @param args an array of values of any type
     * @return an array of Class&lt;Object&gt;
     */
    Class[] typesForArgs(Object[] args){
        List<Class> params = new ArrayList<>();
        Arrays.asList(args).forEach((arg) -> params.add(classEquivalent(arg.getClass())));
        return params.toArray(new Class[args.length]);
    }


    //only for inclusion with writing, not in use
    public Object invoke(Method method, Class<Object> aClass, Object... args) throws Exception {
        try {
            Object classInstance = aClass.getDeclaredConstructor().newInstance();
            return method.invoke(classInstance, args);
        }
        catch (Exception e){
            throw new Exception("Cannot invoke method with supplied arguments and/or on supplied class");
        }
    }







    //endregion

    //region method location

    /**
     * Finds methods matching a particular name
     * @param name method name (case sensitive), no brackets e.g. "myMethod"
     * @return Set of methods matching that name
     */
    Set<Method> findMethods(String name){
        final HashSet<Method> methods = new HashSet<>();
        for (Method method: searchClass.getDeclaredMethods()){
            if (method.getName().equals(name)){
                methods.add(method);
            }
        }
        return methods;
    }



    /**
     * Returns methods where the name and return type match.
     * Primitive types will be matched to their class equivalents
     * @param name
     * @param returnType
     * @param strict
     * @return
     */
    public Set<Method> findMethods(String name, Class<?> returnType, boolean strict){
        final Set<Method> methods = findMethods(name);
        if (strict){
            methods.removeIf(m -> (!m.getReturnType().equals(returnType)));
        } else {
            methods.removeIf(m -> (!classEquivalent(m.getReturnType()).equals(classEquivalent(returnType))));
        }
        return methods;
    }


    /**
     * Returns methods where the name and return type match.
     * @param name
     * @param returnType
     * @return
     */
    public Set<Method> findMethods(String name, Class<?> returnType){
        return findMethods(name,returnType,true);
    }


    Optional<Method> findMethod(Class returnType, String name, Class... paramTypes){
        Set<Method> methods = findMethods(name,returnType,false);
        for (Method m : methods) {
            Class<?>[] methodParamTypes = m.getParameterTypes();
            if (paramTypes.length == methodParamTypes.length) {
                boolean matchedParams = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (classEquivalent(methodParamTypes[i]) != paramTypes[i]) {
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




//    public Optional<Method>findMethod(Class desiredReturnType, String name, Class... desiredParamTypes){
//        final Collection<Method> methods = methodsWithSignature(desiredReturnType, desiredParamTypes);
//        for (Method method: methods){
//            String mName = method.getName();
//            if (mName.equals(name)){
//                return Optional.of(method);
//            }
//        }
//        return Optional.empty();
//    }

//    public Set<Method> methodsWithSignature(Class<?> desiredReturnType, Class<?>... desiredParamTypes){
//        return methodsWithSignature(desiredReturnType,true,desiredParamTypes);
//    }


    /**
     * Returns a list of methods which match the return type and parameters
     * @param desiredReturnType class representing the type of data returned by the method
     * @param matchParamOrder whether the parameters must appear in the order supplied
     * @param desiredParamTypes the types for each parameter in the methods parameters. if the only item is an Array, it must be cast as an object
     * @return An arrayList of {@code Method} objects that match the required signature
     */
    public Set<Method> methodsWithSignature(Class<?> desiredReturnType, boolean matchParamOrder, boolean strict, Class<?>... desiredParamTypes){

        HashSet<Method> methods = new HashSet<>();

        for (Method method: searchClass.getDeclaredMethods()){

            desiredReturnType = strict ? desiredReturnType : classEquivalent(desiredReturnType);
            //desiredParamTypes = strict ? desiredParamTypes : classEquivalents(desiredParamTypes);

            Class<?> actualReturnType = strict ? method.getReturnType() : classEquivalent(method.getReturnType());

            if (actualReturnType.equals(desiredReturnType)) {
                Class<?>[] actualParamTypes = strict ? method.getParameterTypes() : classEquivalents(method.getParameterTypes());

                Comparator<Class<?>> comparator = new Comparator<Class<?>>() {
                    @Override
                    public int compare(Class<?> o1, Class<?> o2) {
                        return o1.getCanonicalName().compareTo(o2.getCanonicalName());
                    }
                };

                if (!matchParamOrder) {
                    Arrays.sort(actualParamTypes, comparator);
                    Arrays.sort(desiredParamTypes, comparator);
                }

                if (Arrays.equals(actualParamTypes, desiredParamTypes)){
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    //endregion


    //region conversions

    //converts primitive representation of class to object
    private Class classEquivalent(Class primitiveClass){

        final Class[] primitives = {boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class};
        final Class[] classes = {Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class};

        for (int i = 0; i < primitives.length; i++) {
            if (primitiveClass == primitives[i]){
                return classes[i];
            }
        }
        return primitiveClass; //not actually a primitive
    }

    private Class[] classEquivalents(Class[] primitiveClasses){
        for (int i = 0; i < primitiveClasses.length; i++) {
            primitiveClasses[i] = classEquivalent(primitiveClasses[i]);
        }
        return primitiveClasses;
    }


    //endregion

}
