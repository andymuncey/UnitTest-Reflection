package uk.ac.chester.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class FieldTester<T> extends Tester {

    private FieldsTestEventHandler handler;
    private Set<Field> fields;

    public FieldTester(Class<T> theClass, FieldsTestEventHandler handler){
        ReflectionHelper helper = new ReflectionHelper(theClass);
        this.handler = handler;
        Set<Field> allFields = helper.fields();
        allFields.removeIf(Field::isSynthetic);
        fields = allFields;
    }

    public void testField(AccessModifier desiredModifier, String name, Class desiredClass, boolean allowAutoboxing){

        for (Field field : fields){
            if (field.getName().equals(name)){
                Class actualClass = field.getType(); //careful not to use getClass() here (which will be Field!)
                if (allowAutoboxing) {
                    if (!ReflectionHelper.equivalentType(actualClass,desiredClass)){
                        handler.fieldFoundButNotCorrectType(name,desiredClass,actualClass);
                    }
                } else {
                    if (!actualClass.equals(desiredClass)){
                        handler.fieldFoundButNotCorrectType(name,desiredClass,actualClass);
                    }
                }
                //check correct access modifier
                AccessModifier actualModifier = AccessModifier.accessModifier(field);
                if (!actualModifier.equals(desiredModifier)){
                    handler.fieldHasIncorrectModifier(name,desiredModifier,actualModifier );
                }

                return;
            }
        }
        handler.fieldNotFound(name);
    }


    /*
    Run all tests against all fields
     */
    public void testFields(){
        checkNonStaticArePrivate();
        checkStaticAreFinal();
        checkNames();
    }

    /**
     * Tests that fields that are not static and are private
     */
    private void checkNonStaticArePrivate() {
        for (Field field: fields){
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
                handler.nonPrivateFieldFound(field.getName());
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
    private void checkNames() {
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


    public interface FieldsTestEventHandler {
        /**
         *  Indicates when a non-static field is not declared as private
         */
        void nonPrivateFieldFound(String fieldName);

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
         * No field found matching the specified name
         * @param name the name of the field
         */
        void fieldNotFound(String name);

        /**
         * Indicates a fields has been found, but the type is not as expected
         * @param fieldName the name of the field
         * @param requiredType the expected type
         * @param actualType the actual type
         */
        void fieldFoundButNotCorrectType(String fieldName, Class requiredType, Class actualType);

        /**
         * Indicates that a field does not have the expected access modifier
         * @param name the name of the field
         * @param desiredModifier the expected modifier
         * @param actualModifier the actual modifier
         */
        void fieldHasIncorrectModifier(String name, AccessModifier desiredModifier, AccessModifier actualModifier);
    }

}
