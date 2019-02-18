package uk.ac.chester.testing;

import org.junit.Assert;

/**
 * English language implementation of ConstructorTester.ConstructorTestEventHandler
 */
public class ConstructorTestEventHandlerEN implements ConstructorTester.ConstructorTestEventHandler, ClassDescriber {

    @Override
    public void incorrectParameters(Class[] requiredParamTypes) {
        Assert.fail("A constructor was found, but it has the wrong parameters, expected parameters are " + describe(requiredParamTypes));
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
    public void wrongAccessModifier(MemberTester.AccessModifier actual, MemberTester.AccessModifier required) {
        Assert.fail("A constructor was found with the correct parameters, but the access modifier ("+ actual.toString()+") was not as required ("+required.toString()+")");
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
