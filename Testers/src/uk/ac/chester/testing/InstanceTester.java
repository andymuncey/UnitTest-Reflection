package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.InstanceReflectionHelper;

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

    private InstanceReflectionHelper<C> helper;
    private final EventHandler handler;

    /**
     *
     * @param searchClass The class from which an object will be created
     * @param handler An instance of InstanceTester.EventHandler to handle errors
     * @param args The arguments to pass to the constructor for the class
     */
    public InstanceTester(Class<C> searchClass, EventHandler handler, Object... args){

        this.handler = handler;
        try {
            helper = new InstanceReflectionHelper<>(searchClass, args);
        } catch (Exception e) {
            this.handler.cannotConstructWithArgs(searchClass.getSimpleName(), args);
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

    public <T> T executeMethod(Class<T> returnType, String name, Object... args){
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

    public interface EventHandler {

        void cannotConstructWithArgs(String className, Object[] args);

        void notConstructed();

        void cannotInvokeMethod(Class returnType, String name, Object[] args);

        void cannotRetrieveFieldValue(Class type, String name);
    }

}

