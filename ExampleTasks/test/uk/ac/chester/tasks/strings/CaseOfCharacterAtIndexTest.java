package uk.ac.chester.tasks.strings;


import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.ConsoleTester;

import static org.junit.jupiter.api.Assertions.*;

//    Get a String from the user, then get an int from the user, representing an index in that String. Verify if that character is uppercase or lowercase.
//    Add error handling to the above task, so that if the number exceeds the length of the String, the programme does not crash
public class CaseOfCharacterAtIndexTest {

    @Test
    public void lower() {
        String expected = "lower";
        String prohibited = "upper";
        String wrongOutputMessage = "the character at index 2 of \"United Kingdom\" is lowercase, the last line of your output does not include the word 'lower' or contains the word 'upper'";
        String noOutputMessage = "Your programme didn't provide any output";
        test(expected,prohibited,wrongOutputMessage, noOutputMessage, "United Kingdom", "2");
    }


    @Test
    public void upper() {
        String expected = "upper";
        String prohibited = "lower";
        String wrongOutputMessage = "the character at index 2 of \"UK\" is uppercase, the last line of your output does not include the word 'upper' or contains the word 'lower'";
        String noOutputMessage = "Your programme didn't provide any output";
        test(expected,prohibited,wrongOutputMessage, noOutputMessage, "UK", "1");
    }


    private void test(String expectedOutput, String prohibitedOutput, String wrongOutputMessage, String noOutputMessage, String... tokens){
        ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {
            @Override
            public void outputGenerated(String[] inputTokens, String[] linesOfOutput) {
                String lastLine  = linesOfOutput[linesOfOutput.length-1];
                assertTrue(lastLine.toLowerCase().contains(expectedOutput),wrongOutputMessage);
                assertFalse(lastLine.toLowerCase().contains(prohibitedOutput),wrongOutputMessage);
            }

            @Override
            public void noOutputGenerated(String[] inputTokens) {
                fail(noOutputMessage);
            }


        };

        ConsoleTester<CaseOfCharacterAtIndex> tester = new ConsoleTester<>(CaseOfCharacterAtIndex.class);
        tester.setCompletionHandler(handler);
        tester.test(tokens);
    }
}