package uk.ac.chester.testing;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassTester<T> extends Tester {

    private final EventHandler handler;
    private final Set<Field> fields;
    private final Set<Method> methods;
    private final Set<Constructor<?>> constructors;

    private final Class<T> clazz;

    /**
     * Creates a ClassTester for a specified class
     * @param theClass the class to testExistence the fields in
     * @param handler An implementation of EventHandler, likely containing unit testExistence assertions
     */
    public ClassTester(Class<T> theClass, EventHandler handler){
        this.clazz = theClass;
        this.handler = handler;

        Set<Field> allFields = new HashSet<>(Arrays.asList(theClass.getDeclaredFields()));
        allFields.removeIf(Field::isSynthetic);
        fields = allFields;

        Set<Method> allMethods = new HashSet<>(Arrays.asList(theClass.getDeclaredMethods()));
        allMethods.removeIf(Method::isSynthetic);
        methods = allMethods;

        Set<Constructor<?>> allConstructors = new HashSet<>(Arrays.asList(theClass.getDeclaredConstructors()));
        allConstructors.removeIf(Constructor::isSynthetic);
        constructors = allConstructors;
    }


    //region fields
    /*
    Run tests against all fields to check non-static fields are private,
    static fields are declared as final and names conform to convention
     */
    public void checkFields(){
        checkNonStaticArePrivate();
        checkStaticFieldsAreFinal();
        checkFieldNames();
    }

    /**
     * Tests that fields that are not static and are private
     */
    public void checkNonStaticArePrivate() {
        for (Field field: fields){
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
                handler.fieldNotPrivate(field.getName());
            }
        }
    }

    /**
     * Tests for the presence of static fields which are not declared as final
     */
    public void checkStaticFieldsAreFinal() {
        for (Field field: fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                handler.fieldStaticButNotFinal(field.getName());
            }
        }
    }

    /**
     * Tests that field names match Java naming conventions
     */
    public void checkFieldNames() {
        for (Field field: fields){
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers)) {
                if (!getConventionChecker().validVariableName(field.getName())) {
                    handler.fieldNameUnconventional(field.getName(), false);
                }
            } else if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {

                if (isSerialVersionUID(field)){
                    continue;
                }
                if (!getConventionChecker().validClassConstantName(field.getName())) {
                    handler.fieldNameUnconventional(field.getName(), true);
                }
            }
        }
    }

    private static boolean isSerialVersionUID(Field field) {
        return field.getName().equals("serialVersionUID") && field.getType() == long.class;
    }

    //endregion



    public void checkMethods(){
        checkMethodNames();
        checkMethodParameterNames();
    }

    //region methods
    /**
     * verifies method names in a given class follow convention
     */
    private void checkMethodNames(){
        for (Method m: methods){
            String name = m.getName();
            if (!getConventionChecker().validMethodName(name)){
                handler.methodNameUnconventional(name);
            }
        }
    }

    /**
     * Verifies that parameter names across all methods follow convention
     */
    public void checkMethodParameterNames(){
        for (Method m: methods){
            for (Parameter param : m.getParameters()){
                String paramName = param.getName();
                if (!getConventionChecker().validVariableName(paramName)) {
                    handler.methodParameterNameUnconventional(paramName, m.getName());
                }
            }
        }
    }

    //endregion

    /**
     * Verifies that parameter names across all constructors follow convention
     */
    public void checkConstructorParameterNames(){
        for (Constructor<?> c: constructors){
            for (Parameter param : c.getParameters()){
                String paramName = param.getName();
                if (!getConventionChecker().validVariableName(paramName)) {
                    handler.constructorParameterNameUnconventional(paramName);
                }
            }
        }
    }


    /**
     * Determines whether the class being tested extends a specified superclass,
     * either directly or through a chain of inheritance
     * @param superclass a potential superclass
     * @return true if the class extends the superclass, false otherwise
     */
    public boolean extendsSuperclass(Class<?> superclass){
        return extendsSuperclass(clazz,superclass);
    }

    /**
     * Determines if a class extends a specified superclass
     * @param theClass the class to check
     * @param theSuperclass the superclass to look for
     * @return true if the class extends the superclass, false otherwise
     */
    private static boolean extendsSuperclass(Class<?> theClass, Class<?> theSuperclass){
        if (theClass.getSuperclass() == null){
            return false;
        }
        if (theClass.getSuperclass().equals(theSuperclass)){
            return true;
        }
        return extendsSuperclass(theClass.getSuperclass(), theSuperclass);
    }

    /**
     * Determines if the class being tested extends a particular interface
     * @param anInterface some interface
     * @return true if the class extends the interface, false otherwise
     */
    public boolean implementsInterface(Class<?> anInterface){
        assert(anInterface.isInterface());
        return Arrays.asList(clazz.getInterfaces()).contains(anInterface);

    }

    public interface EventHandler {

        /**
         * Indicates this method does not conform to the naming convention for Java methods
         * @param name the name of the method
         */
        void methodNameUnconventional(String name);

        /**
         * Indicates that a parameter in a method doesn't fit adhere to convention
         * @param paramName the parameter name
         * @param methodName the method name
         */
        void methodParameterNameUnconventional(String paramName, String methodName);

        /**
         *  Indicates when a non-static field is not declared as private
         */
        void fieldNotPrivate(String fieldName);

        /**
         * Indicates when a field doesn't follow the Java naming convention
         * @param fieldName the name of the field
         * @param isStatic whether the field is static
         */
        void fieldNameUnconventional(String fieldName, boolean isStatic);

        /**
         * Indicates a field has been found which is marked as static, but not final
         * @param fieldName the name of the field
         */
        void fieldStaticButNotFinal(String fieldName);

        /**
         * Indicates a parameter for a constrictor doesn't follow the Java naming convention
         * @param paramName the parameter
         */
        void constructorParameterNameUnconventional(String paramName);
    }
}
