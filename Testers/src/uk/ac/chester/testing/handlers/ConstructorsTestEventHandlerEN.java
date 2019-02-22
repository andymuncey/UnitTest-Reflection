package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.ConstructorsTester;
import org.junit.Assert;

/**
 * English language implementation of ConstructorsTester.ConstructorTestEventHandler
 */
public class ConstructorsTestEventHandlerEN implements ConstructorsTester.ConstructorTestEventHandler, ClassDescriber {

    @Override
    public void incorrectParameters(Class[] requiredParamTypes) {
        String message = "A constructor was found";
        if (requiredParamTypes.length > 0) {
            message += ", but it has the wrong parameters, expected parameters are " + describe(requiredParamTypes);
        } else {
            message += ", but it has parameters, and a constructor with no parameters is expected";
        }
        Assert.fail(message);
    }

    @Override
    public void incorrectParamOrder(Class[] requiredParams) {
        Assert.fail("Constructor found, but parameters are not in the correct order: "+ describe(requiredParams));
    }

    @Override
    public void paramNameUnconventional(String paramName) {
        Assert.fail("A constructor with the correct parameters was found, but the parameter name '"+paramName+"' does not conform to the Java naming convention (e.g. lowerCamelCase, and longer than a single character in most cases)");
    }

    @Override
    public void wrongAccessModifier(AccessModifier actual, AccessModifier required) {
        Assert.fail("A constructor was found with the correct parameters (or no parameters if this is required), but the access modifier ("+ actual.toString()+") was not as required ("+required.toString()+")");
    }

    @Override
    public void constructionFails(Throwable e, Object... args) {
        StringBuilder messageBuilder = new StringBuilder("The instantiation of the class fails with args: ");
        for (Object arg:args) {
            messageBuilder.append(arg);
            messageBuilder.append(" ");
        }
        String message = messageBuilder.toString().trim()+".";
        Assert.fail(message + " Error details: " + e.getMessage());
    }
}
