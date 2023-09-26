package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.ConstructorsTester;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * English language implementation of ConstructorsTester.EventHandler
 */
public class ConstructorsTestEventHandlerEN implements ConstructorsTester.EventHandler, ClassDescriber {

    @Override
    public void incorrectParameters(Class[] requiredParamTypes) {
        String message;
        if (requiredParamTypes.length > 0) {
            message = "No constructor with the correct parameters was found, expected parameters are " + describe(requiredParamTypes);
        } else {
            message = "A constructor with parameters was found, but a constructor with no parameters was expected";
        }
        fail(message);
    }

    @Override
    public void incorrectParamOrder(Class[] requiredParams) {
        fail("Constructor found, but parameters are not in the correct order: "+ describe(requiredParams));
    }

    @Override
    public void paramNameUnconventional(String paramName) {
        fail("A constructor with the correct parameters was found, but the parameter name '"+paramName+"' does not conform to the Java naming convention (e.g. lowerCamelCase, and longer than a single character in most cases)");
    }

    @Override
    public void wrongAccessModifier(AccessModifier actual, AccessModifier required) {
        fail("A constructor was found with the correct parameters (or no parameters if this is required), but the access modifier ("+ actual.toString()+") was not as required ("+required.toString()+")");
    }

    @Override
    public void constructionFails(Throwable e, Object... args) {
        StringBuilder messageBuilder = new StringBuilder("The instantiation of the class fails with args: ");
        for (Object arg:args) {
            messageBuilder.append(arg);
            messageBuilder.append(" ");
        }
        String message = messageBuilder.toString().trim()+".";
        fail(message + " Error details: " + e.getMessage());
    }
}
