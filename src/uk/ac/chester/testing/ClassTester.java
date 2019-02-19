package uk.ac.chester.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassTester<T> extends Tester {

    private Class<T> theClass;
    private ClassTestEventHandler handler;
    private Set<Field> fields;
    private Set<Method> methods;

    /**
     * Creates a ClassTester for a specified class
     * @param theClass the class to test the fields in
     * @param handler An implementation of FieldsTestEventHandler, likely containing unit test assertions
     */
    public ClassTester(Class<T> theClass, ClassTestEventHandler handler){
        this.theClass = theClass;
        this.handler = handler;

        Set<Field> allFields = new HashSet<>(Arrays.asList(theClass.getDeclaredFields()));
        allFields.removeIf(Field::isSynthetic);
        fields = allFields;

        Set<Method> allMethods = new HashSet<>(Arrays.asList(theClass.getDeclaredMethods()));
        allMethods.removeIf(Method::isSynthetic);
        methods = allMethods;
    }

    /*
    Run tests against all fields to check non static fields are private,
    static fields are declared as final and names conform to convention
     */
    public void checkFields(){
        checkNonStaticArePrivate();
        checkStaticAreFinal();
        checkFieldNames();
    }

    /**
     * Tests that fields that are not static and are private
     */
    private void checkNonStaticArePrivate() {
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
    private void checkStaticAreFinal() {
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
    private void checkFieldNames() {
        for (Field field: fields){
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers)) {
                if (!getConventionChecker().validVariableName(field.getName())) {
                    handler.fieldNameUnconventional(field.getName(), false);
                }
            } else if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
                if (!getConventionChecker().validClassConstantName(field.getName())) {
                    handler.fieldNameUnconventional(field.getName(), true);
                }
            }
        }
    }

    /**
     * verifies method names in a given class follow convention
     */
    public void checkMethods(){

        Method[] methods = theClass.getDeclaredMethods();
        for (Method m: methods){
            String name = m.getName();
            if (!getConventionChecker().validMethodName(name)){
                handler.methodNameUnconventional(name);
            }
        }
    }

    public interface ClassTestEventHandler {

        /**
         * This methods does not conform to the naming convention for Java methods
         * @param name the name of the method
         */
        void methodNameUnconventional(String name);

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

    }
}
