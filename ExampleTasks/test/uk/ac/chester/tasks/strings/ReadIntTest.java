package uk.ac.chester.tasks.strings;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.ConsoleTester;

import static org.junit.Assert.*;

public class ReadIntTest {

    @Test
    public void invalidInput() {
        ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {
            @Override
            public void outputGenerated(String[] linesOfOutput) {
                Assert.fail("the programme continued to work when a string was entered");
            }

            @Override
            public void noOutputGenerated() {
                Assert.fail("the programme continued to work when a string was entered");
            }

        };

        ConsoleTester<ReadInt> tester = new ConsoleTester<>(ReadInt.class);
        tester.setCompletionHandler(handler);
        tester.test("hello");
    }
}