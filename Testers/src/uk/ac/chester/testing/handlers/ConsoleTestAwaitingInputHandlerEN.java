package uk.ac.chester.testing.handlers;

import org.junit.Assert;
import uk.ac.chester.testing.ConsoleTester;


/**
 * A generic handler for testing console applications where the result of the tests should be the the application is awaiting user input
 */
public class ConsoleTestAwaitingInputHandlerEN implements ConsoleTester.ExceptionHandler, ConsoleTester.NonCompletionHandler, ConsoleTester.CompletionHandler {

    @Override
    public void outputGenerated(String[] linesOfOutput) {
        //we don't care about this
    }

    @Override
    public void noOutputGenerated() {
        //we don't care about this
    }

    @Override
    public void stillAwaitingInput() {
        //OK this is expected
    }

    @Override
    public void timeout(int seconds) {
        Assert.fail("The application did not finish executing in the time permitted (maximum " + seconds + " seconds)");
    }

    @Override
    public void wrongInputType() {
        Assert.fail("The application tried to read data in a format other than that supplied (for example trying to read an String as an int)");
    }

    @Override
    public void otherException(Exception e) {
        Assert.fail("The application threw a "+e.getClass().getSimpleName()+": " + e.getMessage());
    }
}
