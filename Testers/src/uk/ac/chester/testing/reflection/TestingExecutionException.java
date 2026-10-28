package uk.ac.chester.testing.reflection;

/**
 * Custom exception type for exceptions that are thrown when methods are invoked via reflection throw exceptions
 */
public class TestingExecutionException extends Exception {
    
    public TestingExecutionException(String message){
        super(message);
    }

}
