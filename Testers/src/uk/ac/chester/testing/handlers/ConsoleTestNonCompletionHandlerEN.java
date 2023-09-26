package uk.ac.chester.testing.handlers;


import uk.ac.chester.testing.ConsoleTester;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Implements generic failures methods for console tests where the testExistence is expected to finish normally
 */
public class ConsoleTestNonCompletionHandlerEN implements ConsoleTester.NonCompletionHandler {

    @Override
    public void stillAwaitingInput(String[] inputTokens) {
        fail("The application should have finished given the input supplied, but would still be waiting for user input" + System.lineSeparator() +
                "This can happen if the method results in the creation of more than one instance of Scanner"+ System.lineSeparator() +
                "The input supplied was: " + Arrays.toString(inputTokens));

    }

    @Override
    public void timeout(String[] inputTokens, int seconds) {
        fail("The application did not finish within the " + seconds + " seconds permitted");

    }
}
