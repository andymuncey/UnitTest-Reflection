package uk.ac.chester.tasks.strings;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.ConsoleTester;

//Get a String from the user, if it has more than 30 characters, take the first 27, add an ellipsis (three full stops) and print that, otherwise print the whole String
public class TrimWithEllipsisTest {

    @Test
    public void trimRequired() {
        String input = "A long time ago in a galaxy far, far away";
        String expected = "A long time ago in a galaxy...";
        String wrongOutputMessage = "Strings more than 30 characters should take the first 27 and add three dots to the end before printing the output";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to print either the user's input or a shortened version of it";
        test(expected, wrongOutputMessage, noOutputMessage, input);
    }

    @Test
    public void noTrimRequired() {
        String input = "Hello World";
        String expected = "Hello World";
        String wrongOutputMessage = "Strings 30 characters or less should be printed out as they are";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to print either the user's input or a shortened version of it";
        test(expected, wrongOutputMessage, noOutputMessage, input);
    }

    @Test
    public void borderline() {
        String input = "I know a bear that u wont know";
        String expected = "I know a bear that u wont know";
        String wrongOutputMessage = "Strings 30 characters or less should be printed out as they are";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to print either the user's input or a shortened version of it";
        test(expected, wrongOutputMessage, noOutputMessage, input);
    }

    private void test(String expectedOutput, String wrongOutputMessage, String noOutputMessage, String... tokens) {
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

        ConsoleTester<TrimWithEllipsis> tester = new ConsoleTester<>(TrimWithEllipsis.class);
        tester.setCompletionHandler(handler);
        tester.test(tokens);
    }
}