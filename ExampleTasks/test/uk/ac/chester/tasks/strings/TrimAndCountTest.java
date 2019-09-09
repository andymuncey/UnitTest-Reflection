package uk.ac.chester.tasks.strings;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.ConsoleTester;

//Get a String from the user, remove spaces from the start and end and output how many characters the remaining phrase has
public class TrimAndCountTest {


    @Test
    public void emptyUserInput() {
        String expected = "0";
        String wrongOutputMessage = "If the user types no input, the length printed should be 0";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to count the number of characters in the input, which should be 0 if the input is empty";
        test(expected,wrongOutputMessage,noOutputMessage,"");
    }

    @Test
    public void noTrimRequired() {
        String expected = "5";
        String wrongOutputMessage = "If the user types 'hello', the length printed should be 5";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to count the number of characters in the input, which should be 5 if the input is hello";
        test(expected,wrongOutputMessage, noOutputMessage, "hello");
    }

    @Test
    public void trimRequiredNoSpaces() {
        String expected = "9";
        String wrongOutputMessage = "If the user types 'hello', the length printed should be 9";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to count the number of characters in the input, which should be 9 if the input is hello";
        test(expected,wrongOutputMessage, noOutputMessage, "  greetings  ");
    }

    @Test
    public void trimRequired() {
        String expected = "11";
        String wrongOutputMessage = "If the user types 'hello', the length printed should be 11";
        String noOutputMessage = "Your programme didn't provide any output, it is expected to count the number of characters in the input, which should be 11 if the input is hello";
        test(expected,wrongOutputMessage, noOutputMessage, "     hello world  ");
    }


    private void test(String expectedOutput, String wrongOutputMessage, String noOutputMessage, String... tokens){
        ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {
            @Override
            public void outputGenerated(String[] linesOfOutput) {
                String lastLine  = linesOfOutput[linesOfOutput.length-1];
                Assert.assertEquals(wrongOutputMessage, lastLine,expectedOutput);
            }

            @Override
            public void noOutputGenerated() {
                Assert.fail(noOutputMessage);
            }

        };

        ConsoleTester<TrimAndCount> tester = new ConsoleTester<>(TrimAndCount.class);
        tester.setCompletionHandler(handler);
        tester.test(tokens);
    }





}