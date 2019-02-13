package uk.ac.chester.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class FieldTester<T> implements ExecutableTester {

    private ReflectionHelper helper;
    private FieldsTestEventHandler fieldsHandler;

    public FieldTester(Class<T> theClass, FieldsTestEventHandler fieldsHandler){

        helper = new ReflectionHelper(theClass);
        this.fieldsHandler = fieldsHandler;
    }

    /*
    Run all tests against the fields
     */
    public void testFields(){
        testFieldsArePrivate();
        testFieldNames();
        testForStaticNonFinalFields();
    }

    /**
     * Tests that fields that are not static and are private
     */
    private void testFieldsArePrivate() {
        Set<Field> fields = helper.fields();
        for (Field field: fields){
            if (field.getName().startsWith("this$")){ //ignores anonymous inner class variable names created by Java
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                if (accessModifier(field) != AccessModifier.PRIVATE) {
                    fieldsHandler.nonPrivateFieldFound(field.getName());
                }
            }
        }
    }

    /**
     * Tests that field names match Java naming conventions
     */
    private void testFieldNames() {
        Set<Field> fields = helper.fields();
        for (Field field: fields){
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers)){
                if (!this.validVariableName(field.getName())){
                    fieldsHandler.fieldNameUnconventional(field.getName(),false);
                }
            } else if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)){
                if (!this.validClassConstantName(field.getName())){
                    fieldsHandler.fieldNameUnconventional(field.getName(),true);
                }
            }
        }
    }

    /**
     * Tests for the presence of static fields which are not declared as final
     */
    private void testForStaticNonFinalFields() {
        Set<Field> fields = helper.fields();
        for (Field field: fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)){
                fieldsHandler.fieldStaticButNotFinal(field.getName());
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
    }

}
