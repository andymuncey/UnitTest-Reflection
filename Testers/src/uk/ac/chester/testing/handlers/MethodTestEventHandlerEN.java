package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.MethodTester;
import org.junit.Assert;

/**
 * An English language implementation of the MethodTester.MethodTestEventHandler interface
 */
public class MethodTestEventHandlerEN implements MethodTester.MethodTestEventHandler, ClassDescriber {

    @Override
    public void notFound(String methodName) {
        Assert.fail("No method with the name \""+ methodName + "\" was found");
    }

    @Override
    public void wrongCaseName(String methodName) {
        Assert.fail("The case required for the method is not as required - check which letters should be uppercase, and which should be lower. " +
                "Method names in java use lowerCamelCase");
    }

    @Override
    public void incorrectReturnType(String methodName, Class requiredReturnType) {
        Assert.fail("A method named \"" + methodName + "\" was found, but it does not return the expected type of: " + requiredReturnType.getSimpleName());
    }

    @Override
    public void incorrectParameters(String methodName, Class[] requiredParamTypes) {
        Assert.fail("A method \"" + methodName + "\" was found, but it has the wrong parameters, expected parameters are " + describe(requiredParamTypes));
    }

    @Override
    public void incorrectParamOrder(String methodName, Class[] requiredParams){
        Assert.fail("Method \"" + methodName + "\" found, but parameters are not in the correct order");
    }

    @Override
    public void paramNameUnconventional(String methodName, String paramName) {
        Assert.fail("Method \"" + methodName + "\" found, but the parameter: \""+ paramName+ "\" does not meet the convention for naming Java parameters " +
                "(e.g. lowerCamelCase, and longer than a single character in most cases)");
    }
}