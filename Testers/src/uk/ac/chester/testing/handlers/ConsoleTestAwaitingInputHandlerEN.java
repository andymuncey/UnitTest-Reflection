package uk.ac.chester.testing.handlers;

import org.jetbrains.annotations.NotNull;
import uk.ac.chester.testing.ConsoleTester;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * A generic handler for testing console applications where the result of the tests should be the application is awaiting user input
 */
public class ConsoleTestAwaitingInputHandlerEN implements ConsoleTester.ExceptionHandler, ConsoleTester.NonCompletionHandler, ConsoleTester.CompletionHandler {

    @Override
    public void outputGenerated(String[] inputTokens, @NotNull String[] linesOfOutput) {
        //we don't care about this
    }


    @Override
    public void noOutputGenerated(String[] inputTokens) {
        //we don't care about this
    }

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
