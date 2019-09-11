package uk.ac.chester.tasks.strings;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.ConsoleTester;
import uk.ac.chester.testing.handlers.ConsoleTestAwaitingInputHandlerEN;
import uk.ac.chester.testing.handlers.ConsoleTestNonCompletionHandlerEN;


//Get a String from the user, check to see if it contains the words: Java, code or String (using any combination of case such as jAvA),
// and if it contains at least one of them, print the String, otherwise ask for a new String -
// you will likely need to use a loop to do this, but it may be easier to verify the checking works before adding the loop.
public class ContainsTest {

    @Test
    public void validInput() {
        String expected = "some java";
        String wrongOutputMessage = "You should only output when the input contains the correct word";
        String noOutputMessage = "Your programme didn't provide any output";
        testEventualValidInput(expected, wrongOutputMessage, noOutputMessage, "invalid", "some java");
    }

    private void testEventualValidInput(String expectedOutput, String wrongOutputMessage, String noOutputMessage, String... tokens) {
        ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {
            @Override
            public void outputGenerated(String[] inputTokens, String[] linesOfOutput) {
                String lastLine = linesOfOutput[linesOfOutput.length - 1];
                Assert.assertEquals(wrongOutputMessage, lastLine, expectedOutput);
            }

            @Override
            public void noOutputGenerated(String[] inputTokens) {
                Assert.fail(noOutputMessage);
            }

        };

        ConsoleTester<Contains> tester = new ConsoleTester<>(Contains.class);
        tester.setCompletionHandler(handler);
        tester.setNonCompletionHandler(new ConsoleTestNonCompletionHandlerEN());
        tester.testWithTimeOut(3, tokens);
    }


    /**
     * This testExistence should leave the application in a state where it is waiting for more input
     */
    @Test
    public void invalidInput() {
        ConsoleTestAwaitingInputHandlerEN handler = new ConsoleTestAwaitingInputHandlerEN();
        ConsoleTester<Contains> tester = new ConsoleTester<>(Contains.class);
        tester.setCompletionHandler(handler);
        tester.setNonCompletionHandler(handler);
        tester.testWithTimeOut(3,  "abc", "DEF", "GHI", "jkl"); //shouldn't finish executing
    }

}