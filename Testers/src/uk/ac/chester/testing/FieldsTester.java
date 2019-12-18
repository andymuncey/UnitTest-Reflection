package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.FieldsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

public class FieldsTester<T> extends Tester {

    private final EventHandler handler;
    private final Set<Field> fields;

    /**
     * Creates a FieldsTester for a specified class
     * @param theClass the class to testExistence the fields in
     * @param handler An implementation of EventHandler, likely containing unit testExistence assertions
     */
    public FieldsTester(Class<T> theClass, EventHandler handler){
        FieldsHelper<T> helper = new FieldsHelper<>(theClass);
        this.handler = handler;
        Set<Field> allFields = helper.fields();
        allFields.removeIf(Field::isSynthetic);
        fields = allFields;
    }

    /**
     * Tests a specific field - autoboxing is not permitted
     * @param desiredModifier the expected access modifier for the field
     * @param name the name of the field to testExistence
     * @param desiredClass the type of the field
     */
    public void test(AccessModifier desiredModifier, Class desiredClass, String name){
        test(desiredModifier, desiredClass, name, false);
    }



    public void test(AccessModifier desiredAccessModifier, Class desiredClass, String name, boolean allowAutoboxing) {
        test(desiredAccessModifier, null, desiredClass,name,allowAutoboxing);
    }

        /**
         * Tests a specific field
         * @param desiredAccessModifier the expected access modifier for the field
         * @param desiredNonAccessModifiers any expected desired non access modifiers
         * @param desiredClass the type of the field
         * @param name the name of the field to testExistence
         * @param allowAutoboxing whether the type can be considered equal to its boxed/unboxed counterpart
         */
    public void test(AccessModifier desiredAccessModifier, Set<NonAccessModifier> desiredNonAccessModifiers ,Class desiredClass, String name, boolean allowAutoboxing){

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
                if (!actualModifier.equals(desiredAccessModifier)){
                    handler.fieldHasIncorrectModifier(name,desiredAccessModifier,actualModifier );
                }

                //check correct non-access modifiers
                Set<NonAccessModifier> actualNonAccessModifiers = NonAccessModifier.nonAccessModifiers(field);
                if (!actualNonAccessModifiers.equals(desiredNonAccessModifiers)){
                    handler.fieldHasIncorrectNonAccessModifiers(name,desiredNonAccessModifiers,actualNonAccessModifiers);
                }

                return;
            }
        }
        handler.fieldNotFound(name);
    }


    public T getValue(Class<T> fieldType, String name, Object objectInstance) {
//        Optional<Field> possibleField = fields.stream().filter(x -> x.getName().equals(name)).findFirst();
//        if (possibleField.isPresent()){
//            Field field = possibleField.get();
//            Class actualClass = field.getType();
//            field.setAccessible(true);
//            try {
//                Object fieldValue = field.get(objectInstance);
//                return (T) fieldValue; //will be boxed as per documentation
//            } catch (IllegalAccessException ignored) {
//                //shouldn't occur, as accessible set true
//            }
//        }


        for (Field field : fields) {
            if (field.getName().equals(name)) {
                Class actualClass = field.getType();
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(objectInstance);
                    return (T) fieldValue; //will be boxed as per documentation
                } catch (IllegalAccessException ignored) {
                    //shouldn't occur, as accessible set to true
                }
                break;
            }
        }
        return null;
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


        /**
         * Indicates that a field does not have the expected non-access modifier(s)
         * @param name the name of the field
         * @param desiredNonAccessModifiers expected modifiers
         * @param actualNonAccessModifiers actual modifiers
         */
        void fieldHasIncorrectNonAccessModifiers(String name, Set<NonAccessModifier> desiredNonAccessModifiers, Set<NonAccessModifier> actualNonAccessModifiers);
    }
}
