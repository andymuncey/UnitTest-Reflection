package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.MethodsTester;
import org.junit.Assert;

/**
 * An English language implementation of the MethodsTester.EventHandler interface
 */
public class MethodTestEventHandlerEN implements MethodsTester.EventHandler, ClassDescriber {

    @Override
    public void notFound(String methodName) {
        Assert.fail("No method with the name \""+ methodName + "\" was found");
    }

    @Override
    public void wrongCaseName(String methodName) {
        Assert.fail("The case required for the method is not correct - check which letters should be uppercase, and which should be lower. " +
                "Method names in java use lowerCamelCase");
    }

    @Override
    public void incorrectReturnType(String methodName, Class requiredReturnType) {
        Assert.fail("A method named \"" + methodName + "\" was found, but it does not return the expected type of: " + requiredReturnType.getSimpleName());
    }

    @Override
    public void incorrectNumberOfParameters(String methodName, int expectedParamCount) {
        Assert.fail("A method named \"" + methodName + "\" was found but it does not have the expected number of parameters, which is " + expectedParamCount);
    }

    @Override
    public void incorrectParameters(String methodName, Class[] requiredParamTypes) {
        String plural = requiredParamTypes.length == 1 ? "": "s";
        String isOrAre = requiredParamTypes.length == 1 ? "is": "are";
        Assert.fail("A method \"" + methodName + "\" was found, but it has the wrong parameter"+plural+", expected parameter"+plural +" " +isOrAre+" "+ describe(requiredParamTypes));
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

    @Override
    public void accessModifierIncorrect(String methodName, AccessModifier requiredModifier) {
        Assert.fail("Method '" +methodName+ "' does not have the correct access modifier. The expected modifier is " + requiredModifier);
    }

    @Override
    public void staticDeclarationIncorrect(String methodName, boolean requiredStatic) {
        final String should = requiredStatic ? "should" : "should not";
        final String isNot = requiredStatic ? "is not" : "is";
        Assert.fail("Method '" + methodName + "' " + should + " be marked as static but it " + isNot);
    }
}