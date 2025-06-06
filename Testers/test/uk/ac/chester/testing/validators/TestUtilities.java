package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtilities {


    /**
     * Finds the first element in the stack which is not part of the JUnit framework
     * Ideal for finding the method in the stack trace which has thrown an error/exception
     * @param stackTrace a stack trace
     * @return the name of a method
     */
    static String firstNonTestingMethodName(StackTraceElement[] stackTrace){
        for (StackTraceElement stackTraceElement : stackTrace){
            if (stackTraceElement.getClassName().startsWith("org.junit")){
                continue;
            }
            return stackTraceElement.getMethodName();
        }
        return null;
    }


    static void assertMethodCallThrowsAssertionErrorInMethod(String methodName, Executable executable){
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, executable);
        assertEquals(methodName,TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

}
