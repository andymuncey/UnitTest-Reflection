package uk.ac.chester.testing.handlers;

import org.junit.Assert;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.reflection.Utilities;

public class InstanceTestEventHandlerEN implements InstanceTester.EventHandler, ClassDescriber {
    @Override
    public void cannotConstructWithArgs(String className, Object[] args) {

        Assert.fail("Couldn't construct an instance of " + className + " with the supplied arguments: " + Utilities.commaSeparatedArgList(args));
    }

    @Override
    public void notConstructed() {
        Assert.fail("No constructed instance of the class is available to evaluate");
    }

    @Override
    public void cannotInvokeMethod(Class returnType, String name, Object[] args) {

        Assert.fail("Unable to invoke method " + name);
    }

    @Override
    public void cannotRetrieveFieldValue(Class type, String name) {
        Assert.fail("Unable to retrieve value from field named '" + name + "' of type: " + type.getSimpleName());
    }
}
