package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.FieldsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.Field;
import java.util.Set;

public class FieldsTester<T> extends Tester {

    private final EventHandler handler;
    private final Set<Field> fields;

    /**
     * Creates a FieldsTester for a specified class
     * @param theClass the class to test the fields in
     * @param handler An implementation of EventHandler, likely containing unit test assertions
     */
    public FieldsTester(Class<T> theClass, EventHandler handler){
        FieldsHelper<T> helper = new FieldsHelper<>(theClass);
        this.handler = handler;
        Set<Field> allFields = helper.fields();
        allFields.removeIf(Field::isSynthetic);
        fields = allFields;
    }

    /**
     * Tests a specific field
     * @param desiredModifier the expected access modifier for the field
     * @param name the name of the field to test
     * @param desiredClass the type of the field
     * @param allowAutoboxing whether the type can be considered equal to its boxed/unboxed counterpart
     */
    public void test(AccessModifier desiredModifier, String name, Class desiredClass, boolean allowAutoboxing){

        for (Field field : fields){
            if (field.getName().equals(name)){
                Class actualClass = field.getType(); //careful not to use getClass() here (which will be Field!)
                if (allowAutoboxing) {
                    if (!Utilities.equivalentType(actualClass,desiredClass)){
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

    public interface EventHandler {

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
