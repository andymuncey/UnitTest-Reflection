package uk.ac.chester.Testing;

import org.junit.Assert;

/**
 * An English language implementation of the MethodTester.MethodTestEventHandler interface
 */
public class TestEventHandlerEN implements MethodTester.MethodTestEventHandler {

    @Override
    public void notFound(String methodName) {
        Assert.fail("No method with the name \""+ methodName + "\" was found");
    }

    @Override
    public void incorrectReturnType(String methodName, Class requiredReturnType) {
        Assert.fail("A method named \"" + methodName + "\" was found, but it does not return the expected type of: " + requiredReturnType.getSimpleName());
    }

    @Override
    public void incorrectParameters(String methodName, Class[] requiredParamTypes) {
        Assert.fail("A method \"" + methodName + "\" was found, but it has the wrong parameters, expected parameters are " + parametersDescription(requiredParamTypes));
    }

    @Override
    public void incorrectParamOrder(String methodName, Class[] requiredParams){
        Assert.fail("Method \"" + methodName + "\" found, but parameters are not in the correct order");
    }

    private String parametersDescription(Class[] params){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i].getSimpleName());
            if (params.length > 1 && i == params.length - 2){
                sb.append(" and ");
            } else if (i < params.length - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}