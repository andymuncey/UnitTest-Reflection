package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.ConsoleTester;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * English language implementation of {@link ConsoleTester.ExceptionHandler}
 * and {@link ConsoleTester.NonCompletionHandler} interfaces
 */
public class ConsoleTestAwaitingInputHandlerEN implements ConsoleTester.ExceptionHandler, ConsoleTester.NonCompletionHandler {

    @Override
    public void stillAwaitingInput(String[] inputTokens) {
        //OK this is expected
    }

    @Override
    public void timeout(String[] inputTokens, int seconds) {
        fail("The application did not finish executing in the time permitted (maximum " + seconds + " seconds)");
    }

    @Override
    public void wrongInputType(String[] inputTokens) {
        fail("The application tried to read data in a format other than that supplied (for example trying to read an String as an int)");
    }

    @Override
    public void otherException(String[] inputTokens, Exception e) {
        fail("The application threw a "+e.getClass().getSimpleName()+": " + e.getMessage());
    }
}
