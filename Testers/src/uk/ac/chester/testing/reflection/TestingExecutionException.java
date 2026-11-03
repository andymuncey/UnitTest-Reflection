package uk.ac.chester.testing.reflection;

/**
 * Custom exception type for exceptions that are thrown when methods are invoked via reflection throw exceptions
 */
public class TestingExecutionException extends Exception {

    /**
     * Constructor for {@link TestingExecutionException}
     * @param message the exception's message
     */
    public TestingExecutionException(String message){
        super(message);
    }

}
