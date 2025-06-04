package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.FieldsHelper;
import uk.ac.chester.testing.reflection.Utilities;

import java.lang.reflect.Field;
import java.util.HashSet;
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
        fields = helper.fields();
    }

    /**
     * Tests a specific field - autoboxing is not permitted
     * @param desiredModifier the expected access modifier for the field
     * @param name the name of the field to testExistence
     * @param desiredClass the type of the field
     */
    public void test(AccessModifier desiredModifier, Class<?> desiredClass, String name){
        test(desiredModifier, desiredClass, name, false);
    }



    public void test(AccessModifier desiredAccessModifier, Class<?> desiredClass, String name, boolean allowAutoboxing) {
        test(desiredAccessModifier, null, desiredClass,name,allowAutoboxing);
    }

        /**
         * Tests a specific field
         * @param desiredAccessModifier the expected access modifier for the field
         *                              pass null if you don't care
         * @param desiredNonAccessModifiers any expected desired non access modifiers
         *                                  pass null if you don't care
         *                                  pass an empty set if you want to make sure there aren't any modifiers
         * @param desiredClass the type of the field
         * @param name the name of the field to testExistence
         * @param allowAutoboxing whether the type can be considered equal to its boxed/unboxed counterpart
         */
    public void test(AccessModifier desiredAccessModifier, Set<NonAccessModifier> desiredNonAccessModifiers ,Class<?> desiredClass, String name, boolean allowAutoboxing){

        for (Field field : fields){
            if (field.getName().equals(name)){
                Class<?> actualClass = field.getType(); //careful not to use getClass() here (which will be Field!)
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
                if (desiredAccessModifier != null) {
                    AccessModifier actualModifier = AccessModifier.accessModifier(field);
                    if (!actualModifier.equals(desiredAccessModifier)) {
                        handler.fieldHasIncorrectModifier(name, desiredAccessModifier, actualModifier);
                    }
                }

                //check correct non-access modifiers
                if (desiredNonAccessModifiers != null) {
                    Set<NonAccessModifier> actualNonAccessModifiers = NonAccessModifier.nonAccessModifiers(field);
                    if (!actualNonAccessModifiers.equals(desiredNonAccessModifiers)) {
                        handler.fieldHasIncorrectNonAccessModifiers(name, desiredNonAccessModifiers, actualNonAccessModifiers);
                    }
                }

                return;
            }
        }
        handler.fieldNotFound(name);
    }


    /**
     * Gets a field's value, assuming the type and name match, regardless of access modifier
     * @param fieldType the type of the field
     * @param name the name of the field
     * @param objectInstance the object from which you want to get a field's value
     * @return the value of the field, or null if the field can't be found
     * @param <F> The type of the field
     */
    public <F> F getValue(Class<F> fieldType, String name, Object objectInstance) {

        for (Field field : fields) {
            if (field.getName().equals(name)) {
                if (field.getType().equals(fieldType)) {
                    field.setAccessible(true);
                    try {
                        Object fieldValue = field.get(objectInstance);
                        @SuppressWarnings("unchecked") //fieldValue be boxed as per documentation
                        F result = (F)fieldValue;
                        return result;
                    } catch (IllegalAccessException ignored) {
                        //shouldn't occur, as accessible set to true
                    }
                }
                break;
            }
        }
        return null;
    }


    /**
     * returns every type used for declaring fields
     * excludes inherited and synthetic fields
     * @param ignoreStatic ignore fields with the static modifier
     * @return the types used by the fields in the class
     */
    public Set<Class<?>> fieldTypes(boolean ignoreStatic){
       Set<Class<?>> fieldTypes = new HashSet<>();
        for (Field field: fields){
            if (ignoreStatic && NonAccessModifier.nonAccessModifiers(field).contains(NonAccessModifier.STATIC)){
                continue;
            }
            fieldTypes.add(field.getType());
        }
        return fieldTypes;
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
        void fieldFoundButNotCorrectType(String fieldName, Class<?> requiredType, Class<?> actualType);

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
