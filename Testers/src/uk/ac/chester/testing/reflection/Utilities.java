package uk.ac.chester.testing.reflection;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utilities {


    /**
     * given an array of arguments (values) returns an array of the same size representing the types (as classes) of each argument
     *
     * @param args an array of values of any type
     * @return an array of Class objects
     */
    public static Class<?>[] classesForArgs(Object[] args) {
        List<Class<?>> params = new ArrayList<>();
        Arrays.asList(args).forEach((arg) -> params.add(arg.getClass()));
        return params.toArray(new Class[args.length]);
    }

    private static final Class<?>[] primitives = {boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class}; //do not change order, must match below
    private static final Class<?>[] classes = {Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class}; // do not change order, must match above


    /**
     * Given the 'class' of a primitive type (e.g. int.class returns the class of the corresponding boxed type, e.g. Integer.class)
     * Classes that do not belong to primitive types will remain unmodified
     *
     * @param primitiveClass a primitive 'class' such as double.class
     * @return the class of the boxed equivalent (e.g. <code>char.class</code> becomes <code>Character.class</code>)
     */
    static Class<?> classEquivalent(Class<?> primitiveClass) {

        for (int i = 0; i < primitives.length; i++) {
            if (primitiveClass == primitives[i]) {
                return classes[i];
            }
        }
        return primitiveClass; //not actually a primitive
    }

    static Class<?> primitiveEquivalent(Class<?> boxedType) {
        for (int i = 0; i < classes.length; i++) {
            if (boxedType == classes[i]) {
                return primitives[i];
            }
        }
        return boxedType; //no primitive equivalent
    }

    /**
     * Returns an array with the results of calling {@link #classEquivalent} on each item
     *
     * @param primitiveClasses an array of Class object, which should include the class for some Primitive types
     * @return an array of Class, each corresponding to an object type
     */
    static Class<?>[] classEquivalents(Class<?>[] primitiveClasses) {
        Class<?>[] classClasses = new Class[primitiveClasses.length];
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
    public static boolean equivalentType(Class<?> classA, Class<?> classB){
        return classEquivalent(classA) == classEquivalent(classB);
    }

    /**
     * Returns the parameter names for an executable, such as a method or constructor
     * <important>Requires the compiler -parameters switch to be set</important>
     * @param executable e.g. a method or constructor
     * @return an array of Strings with the parameter names
     */
    public static String[] parameterNames (Executable executable){
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
    static void sortParamsTypesByName(Class<?>[] paramTypes){
        Arrays.sort(paramTypes, Comparator.comparing(Class::getCanonicalName));
    }


    /**
     * Verify if two sets of types can be considered equal
     * @param allowAutoboxing whether boxed and primitive types can be considered equal
     * @param types an Array of types
     * @param otherTypes an Array of types
     * @return true or false
     */
    static boolean typesMatch(boolean allowAutoboxing, Class<?>[] types, Class<?>[] otherTypes) {
        if (types.length != otherTypes.length){
            return  false;
        }
        for (int i = 0; i < otherTypes.length; i++) {
            Class<?> type = allowAutoboxing ? classEquivalent(otherTypes[i]) : otherTypes[i];
            Class<?> otherType = allowAutoboxing ?  classEquivalent(types[i]) : types[i];
            if (type != otherType){
                return false;
            }
        }
        return true;
    }

    /**
     * Given a list of arguments, provide a list of types as a String
     * @param args an array of objects
     * @return a String in the format "String, Integer, Object"
     */
    static String commaSeparatedTypeList(Object[] args) {
        StringBuilder builder = new StringBuilder();
        Class<?>[] paramClasses = classesForArgs(args);
        for (int i = 0; i < paramClasses.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(paramClasses[i].getSimpleName());
        }
        return builder.toString();
    }


    public static String commaSeparatedArgList(Object[] args) {
        StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (Object arg: args){
            if (first){
                first = false;
            } else {
                builder.append(", ");
            }

            if (arg instanceof String) {
                builder.append("\"");
            }
            builder.append(arg.toString());
            if (arg instanceof String) {
                builder.append("\"");
            }
        }
        return builder.toString();
    }

    /**
     * Attempts to unbox an object to a specified primitive type
     * will return null if the return type is not primitive to start with or the types do not match
     * This method doesn't really unbox the type as you cannot know at runtime whether a type is boxed
     * @param returnType the unboxed type corresponding to primitive equivalent of the object provided in the second parameter
     * @param objectOrPrimitive the object to unbox
     * @param <T> The type of object to unbox to
     * @return the unboxed type of the object, where possible, null if the unboxing fails
     */
    @Nullable
    @SuppressWarnings("unchecked") //Must do this as alternative is to call cast on return type
                                    //which you can't do as you cant cast Integer to int for example
    static <T> T unBox(Class<T> returnType, Object objectOrPrimitive) {

        if (!returnType.isPrimitive()){
            throw new IllegalArgumentException("Only primitive return types permitted");
        }

        if (objectOrPrimitive instanceof Boolean && returnType == boolean.class
            || objectOrPrimitive instanceof Byte && returnType == byte.class
            || objectOrPrimitive instanceof Character && returnType == char.class
            ||objectOrPrimitive instanceof Double && returnType == double.class
            ||objectOrPrimitive instanceof Float && returnType == float.class
            ||objectOrPrimitive instanceof Integer && returnType == int.class
            ||objectOrPrimitive instanceof Long && returnType == long.class
            ||objectOrPrimitive instanceof Short && returnType == short.class){
            return (T)objectOrPrimitive;
        } else {
            throw new IllegalArgumentException("return type and object type must match");
        }

    }
}
