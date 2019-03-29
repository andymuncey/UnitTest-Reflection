package uk.ac.chester.testing.handlers;

import org.junit.Assert;
import uk.ac.chester.testing.ConsoleTester;


/**
 * Implements generic failures methods for console tests where the test is expected to finish normally
 */
public class ConsoleTestNonCompletionHandlerEN implements ConsoleTester.NonCompletionHandler {

    @Override
    public void stillAwaitingInput() {
        Assert.fail("The application should have finished given the input supplied, but would still be waiting for user input");
    }

    @Override
    public void timeout(int seconds) {
        Assert.fail("The application did not finish within the " + seconds + " seconds permitted");
    }
}
