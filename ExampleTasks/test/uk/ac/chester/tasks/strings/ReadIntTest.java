package uk.ac.chester.tasks.strings;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.ConsoleTester;

public class ReadIntTest {

    @Test
    public void invalidInput() {
        ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {

            @Override
            public void outputGenerated(String[] inputTokens, String[] linesOfOutput) {
                Assert.fail("the programme continued to work when a string was entered");

            }

            @Override
            public void noOutputGenerated(String[] inputTokens) {
                Assert.fail("the programme continued to work when a string was entered");
            }

        };

        ConsoleTester<ReadInt> tester = new ConsoleTester<>(ReadInt.class);
        tester.setCompletionHandler(handler);
        tester.test("hello");
    }
}