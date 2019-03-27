package uk.ac.chester.testing;

public class TestingExecutionException extends Exception {
    
    public TestingExecutionException(String message){
        super(message);
    }

    public TestingExecutionException(){
        super("An error has occurred when trying to execute a method, or access the value of a field");
    }

}
