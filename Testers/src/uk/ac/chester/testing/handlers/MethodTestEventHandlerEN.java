package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.MethodsTester;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An English language implementation of the MethodsTester.EventHandler interface
 */
public class MethodTestEventHandlerEN implements MethodsTester.EventHandler, ClassDescriber {

    @Override
    public void notFound(String methodName, Class<?> searchClass) {
        fail("No method with the name \""+ methodName + "\" was found in the class \"" + searchClass.getSimpleName() + "\"");
    }

    @Override
    public void wrongCaseName(String methodName) {
        fail("The case required for the method is not correct - check which letters should be uppercase, and which should be lower. " +
                "Method names in java use lowerCamelCase, so this method should be written as " + methodName);
    }

    @Override
    public void incorrectReturnType(String methodName, Class<?> requiredReturnType) {
        fail("A method named \"" + methodName + "\" was found, but it does not return the expected type of: " + requiredReturnType.getSimpleName());
    }

    @Override
    public void incorrectNumberOfParameters(String methodName, int expectedParamCount) {
        fail("A method named \"" + methodName + "\" was found but it does not have the expected number of parameters, which is " + expectedParamCount);
    }

    @Override
    public void incorrectParameters(String methodName, Class<?>[] requiredParamTypes) {
        String plural = requiredParamTypes.length == 1 ? "": "s";
        String isOrAre = requiredParamTypes.length == 1 ? "is": "are";
        fail("A method \"" + methodName + "\" was found, but it has the wrong parameter type"+plural+", expected parameter type"+plural +" " +isOrAre+" "+ describe(requiredParamTypes));
    }

    @Override
    public void incorrectParamOrder(String methodName, Class<?>[] requiredParams){
        fail("Method \"" + methodName + "\" found, but parameters are not in the correct order. The expected order is as follows: " + typesToString(requiredParams));
    }

    @Override
    public void paramNameUnconventional(String methodName, String paramName) {
        fail("Method \"" + methodName + "\" found, but the parameter: \""+ paramName+ "\" does not meet the convention for naming Java parameters " +
                "(e.g. lowerCamelCase, and longer than a single character in most cases)");
    }

    @Override
    public void accessModifierIncorrect(String methodName, AccessModifier requiredModifier) {
        fail("Method '" +methodName+ "' does not have the correct access modifier. The expected modifier is " + requiredModifier);
    }

    @Override
    public void staticDeclarationIncorrect(String methodName, boolean requiredStatic) {
        final String should = requiredStatic ? "should" : "should not";
        final String isNot = requiredStatic ? "is not" : "is";
        fail("Method '" + methodName + "' " + should + " be marked as static but it " + isNot);
    }

    public String typesToString(Class<?>[] types){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0){
                builder.append(", ");
            }
            builder.append(types[i].getSimpleName());
        }
        return builder.toString();
    }
}