package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.FieldsHelper;
import uk.ac.chester.testing.reflection.InstanceHelper;

import java.lang.reflect.Field;

/**
 * This class is designed to be used to testExistence method calls and field values and will use an instance of the class passed to the constructor
 * It is important that checks have already been made as to the:
 *      existence of a constructor that takes the required parameters
 *      existence of the methods called (e.g. by using the MethodsTester class)
 *      existence of the fields tested (e.g. by using FieldsTester class)
 * Minimal error information is provided by this class if location of a method or field occurs
 * @param <C>
 */
public class InstanceTester<C> {

    private InstanceHelper<C> helper;
    private final EventHandler handler;

    private final Class<C> searchClass;

    /**
     *
     * @param searchClass The class from which an object will be created
     * @param handler An instance of InstanceTester.EventHandler to handle errors
     * @param constructorArgs The arguments to pass to the constructor for the class
     */
    public InstanceTester(Class<C> searchClass, EventHandler handler, Object... constructorArgs){
        this.searchClass = searchClass;
        this.handler = handler;
        try {
            helper = new InstanceHelper<>(searchClass, constructorArgs);
        } catch (Exception e) {
            this.handler.cannotConstructWithArgs(searchClass.getSimpleName(), constructorArgs);
        }
    }

    private void verifyConstructed(){
        if (!helper.hasValidInstance()){
            handler.notConstructed();
        }
    }

    /**
     * executes a method with no return value
     * @param name the name of the method
     * @param args the args
     */
    public void executeNonReturningMethod(String name, Object... args){
        verifyConstructed();
        try {
            helper.invokeMethod(void.class, name, args);
        } catch (TestingExecutionException e){
            handler.cannotInvokeMethod(void.class, name,args);
        }
    }


    /**
     *
     * @param returnType the class of the type the method to be called returns
     * @param name the name of the method to call, pass null to use the name of the calling method
     * @param args the parameters to pass to the method. Arrays must be cast to an object
     * @return the result of executing the method, if successful, null otherwise
     * @param <T> the type returned by the method to be called
     */
    public <T> T executeMethod(Class<T> returnType, String name, Object... args){

        //Finds the name of the calling method
        //adapted from Nathan (2021) https://stackoverflow.com/questions/4065518/java-how-to-get-the-caller-function-name
        if (name == null){
            //noinspection OptionalGetWithoutIsPresent
            name = StackWalker.getInstance().walk(frames -> frames.skip(1).findFirst().get()).getMethodName();
        }

       verifyConstructed();
        try {
           return helper.invokeMethod( returnType, name, args);
       } catch (TestingExecutionException e){
           handler.cannotInvokeMethod(returnType, name,args);
       }
       return null;
    }


    public <T> T getFieldValue(Class<T> type, String name){
        verifyConstructed();
        try {
            return helper.fieldValue(type, name);
        } catch (TestingExecutionException e) {
            handler.cannotRetrieveFieldValue(type, name);
        }
        return null;
    }

    public <T> boolean setFieldValue(Class<T> type, String name, T value){
        verifyConstructed();
        return helper.setFieldValue(type,name,value);
    }


    /**
     * Verifies that all private fields are initialized after the class has been constructed
     * Note that primitive types have default values (e.g. int defaults to 0),
     * so this method does not check fields of primitive types are initialized
     */
    public void verifyAllFieldsInitialised(){
        verifyConstructed();
        FieldsHelper<C> fieldsHelper = new FieldsHelper<>(searchClass);
        for (Field field: fieldsHelper.fields()){
            if (AccessModifier.accessModifier(field).equals(AccessModifier.PRIVATE)) {
                Object fieldValue = getFieldValue(field.getType(), field.getName());
                if (fieldValue == null) {
                    handler.fieldNotInitialized(field.getType(), field.getName());
                }
            }
        }
    }

    public interface EventHandler {

        void cannotConstructWithArgs(String className, Object[] args);

        void notConstructed();

        void cannotInvokeMethod(Class<?> returnType, String name, Object[] args);

        void cannotRetrieveFieldValue(Class<?> type, String name);

        void fieldNotInitialized(Class<?> type, String name);
    }

}

