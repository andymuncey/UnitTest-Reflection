package uk.ac.chester.testing.handlers;

import static org.junit.jupiter.api.Assertions.fail;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.reflection.Utilities;

public class InstanceTestEventHandlerEN implements InstanceTester.EventHandler, ClassDescriber {
    @Override
    public void cannotConstructWithArgs(String className, Object[] args) {

        fail("Couldn't construct an instance of " + className + " with the supplied arguments: " + Utilities.commaSeparatedArgList(args));
    }

    @Override
    public void notConstructed() {
        fail("No constructed instance of the class is available to evaluate");
    }

    @Override
    public void cannotInvokeMethod(Class<?> returnType, String name, Object[] args) {
        fail("Unable to invoke method " + name +" with return type '"+returnType.getSimpleName()+"' using the arguments: " + describe(args));
    }

    @Override
    public void cannotRetrieveFieldValue(Class<?> type, String name) {
        fail("Unable to retrieve value from field named '" + name + "' of type: " + type.getSimpleName());
    }

    @Override
    public void fieldNotInitialized(Class<?> type, String name) {
        fail("After construction, the field named '"+ name + "' of type: " + type.getSimpleName()+ " has not been initialised" +
                " (i.e. it is null). " + System.lineSeparator() +
                "It's generally best to avoid fields having a null value, instead the value should be set by the constructor");
    }

    static String describe(Object[] args){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0){
                builder.append(", ");
            }
            builder.append(args[i]);
        }

        return builder.toString();
    }

}
