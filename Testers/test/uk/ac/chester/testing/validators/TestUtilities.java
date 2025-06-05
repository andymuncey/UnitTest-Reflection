package uk.ac.chester.testing.validators;

public class TestUtilities {


    /**
     * Finds the first element in the stack which is not part of the JUnit framework
     * Ideal for finding the method in the stack trace which has thrown an error/exception
     * @param stackTrace a stack trace
     * @return the name of a method
     */
    static String firstNonTestingMethod(StackTraceElement[] stackTrace){
        for (StackTraceElement stackTraceElement : stackTrace){
            if (stackTraceElement.getClassName().startsWith("org.junit")){
                continue;
            }
            return stackTraceElement.getMethodName();
        }
        return null;
    }

}
