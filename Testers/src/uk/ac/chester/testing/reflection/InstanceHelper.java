package uk.ac.chester.testing.reflection;

import uk.ac.chester.testing.TestingExecutionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class InstanceHelper<C> {

    private final FieldsHelper<C> fieldsHelper;
    private final ConstructorsHelper<C> constructorsHelper;
    private final MethodsHelper<C> methodsHelper;
    private Object instance;

    public InstanceHelper(Class<C> searchClass, Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        fieldsHelper = new FieldsHelper<>(searchClass);
        constructorsHelper = new ConstructorsHelper<>(searchClass);
        methodsHelper = new MethodsHelper<>(searchClass);
        construct(args);
    }

    private void construct(Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Optional<Constructor<C>> possibleConstructor = constructorsHelper.constructorForArgTypes(true, true, args);
        if (possibleConstructor.isPresent()) {
            Constructor<C> constructor = possibleConstructor.get();
            constructor.setAccessible(true); //ensures private classes can be tested
            instance = constructor.newInstance(args);
        }
    }

    /**
     * determines whether there is a valid instance of type C to testExistence
     * @return true or false
     */
    public boolean hasValidInstance() {
        return instance != null;
    }


    /**
     * Calls {@link #invokeMethod(boolean, Class, String, Object...)} with allowAutoboxing set to true
     */
    public <T> T invokeMethod(Class<T> returnType, String methodName, Object... args) throws TestingExecutionException {
        return invokeMethod(true, returnType, methodName, args);
    }


    /**
     * Attempts to invoke a method matching the specified returnType and name, with the supplied values
     * Will use the constructed class if available, else will try and create an instance of the class using the parameter-less constructor
     *
     * @param returnType      the type of data returned by the method you wish to invoke, primitives types will be matched with non-primitive equivalents (e.g. int and Integer)
     * @param allowAutoboxing setting 'false' considers primitives and their object equivalents to be different when considering return type. True matches primitive return types with their object counterparts
     * @param methodName      the name of the method to invoke (excluding return type, parameters and parentheses), e.g. "myMethod"
     * @param args            a list of arguments (any primitives will be converted to non-primitive types)
     *                        If the argument is a single array, you need to cast it as an Object (so it's not interpreted a multiple arguments)
     * @param <T>             type of data returned will match the return type specified
     * @return the result of invoking the method
     */
    <T> T invokeMethod(boolean allowAutoboxing, Class<T> returnType, String methodName, Object... args) throws TestingExecutionException {

        final Optional<Method> optionalMethod = methodsHelper.findMethod(allowAutoboxing, returnType, methodName, Utilities.classesForArgs(args));

        if (optionalMethod.isPresent()) {
            final Method m = optionalMethod.get();

            try {
                Object result = m.invoke(instance, args);

                T possiblePrimitive = Utilities.unBox(returnType, result);
                if (possiblePrimitive != null) return possiblePrimitive;

                return returnType.cast(result);

            } catch (IllegalAccessException e) {
                //method is not accessible (i.e. private etc.) - should not occur
                System.err.println(e.getMessage());
            } catch (InvocationTargetException e) {
                //invocationTarget exception: the method itself has thrown an exception - error in student code
                //throw unchecked exception, like the original method would, so student sees reason for error
                throw new RuntimeException(e.getCause());
            }
        }
        throw new TestingExecutionException("Method matching " + returnType.getName() + " " + methodName + "(" + Utilities.commaSeparatedTypeList(args) + ") not found");

    }

    /**
     * Retrieves the value of a field, with a given name and type
     * @param type the class for the type of the field
     * @param name the name of the field
     * @param <T> the type of the field
     * @return the value,
     * @throws TestingExecutionException if the field with the specified name is not found
     */
    @SuppressWarnings("unchecked")
    public <T> T fieldValue(Class<T> type, String name) throws TestingExecutionException {
        Optional<Field> optionalField = fieldsHelper.field(name);
        if (optionalField.isPresent()) {
            Field field = optionalField.get();
            field.setAccessible(true);
            if (field.getType().equals(type)) {
                try {

                    Object fieldValue = field.get(instance);

                    T possiblePrimitive = Utilities.unBox(type, fieldValue);
                    if (possiblePrimitive != null) return possiblePrimitive;

                    return (T) fieldValue;
                } catch (IllegalArgumentException ignored) {
                    //shouldn't happen as set accessible above
                } catch (IllegalAccessException ignored){
                    //also shouldn't happen as type of field has been checked
                }
            }
        }
        throw new TestingExecutionException("Field named " + name + "of type " + type.getSimpleName() + " not found");
    }


    /**
     * Sets a fields value with a given name and type
     * @param type the class for the type of the field
     * @param name the name of the field
     * @param value the value to set for the field
     * @param <T> the type of the field
     * @return true if successful, false otherwise
     */
    public <T> boolean setFieldValue(Class<T> type, String name, T value) {

        Optional<Field> optionalField = fieldsHelper.field(name);
        if (optionalField.isPresent()) {
            Field field = optionalField.get();
            field.setAccessible(true);
            if (field.getType().equals(type)) {
                try {
                    field.set(instance,value);
                    return true;
                } catch (IllegalArgumentException ignored) {
                    //shouldn't happen as set accessible above
                } catch (IllegalAccessException ignored) {
                    //also shouldn't happen as type of field has been checked
                }
            }
        }
        return false;
    }

}
