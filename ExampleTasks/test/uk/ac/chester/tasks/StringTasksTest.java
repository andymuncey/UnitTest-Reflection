//package uk.ac.chester.tasks;
//
//import org.junit.Assert;
//import org.junit.Test;
//import uk.ac.chester.testing.ConsoleTester;
//
//public class StringTasksTest {
//
//
//    @Test
//    public void testPreamble() {
//        ConsoleTester<StringTasks> tester = new ConsoleTester<>(StringTasks.class);
//        tester.setCompletionHandler(handler);
//        Assert.fail(tester.outputGeneratedBeforeInputProcessed()[0]);
//        tester.test("Hello world", "more input");
//    }
//
//    @Test
//    public void main() {
//        ConsoleTester<StringTasks> tester = new ConsoleTester<>(StringTasks.class);
//        tester.setCompletionHandler(handler);
//        tester.test("Hello world", "more input");
//    }
//
//    private final ConsoleTester.CompletionHandler handler = new ConsoleTester.CompletionHandler() {
//        @Override
//        public void outputGenerated(String[] inputTokens, String[] linesOfOutput) {
//            Assert.assertEquals("Hello world",linesOfOutput[0]);
//            Assert.assertFalse("Too many output lines", linesOfOutput.length > 1);
//        }
//
//        @Override
//        public void noOutputGenerated(String[] inputTokens) {
//            Assert.fail("No output was generated, you were meant to print the input");
//        }
//
//    };
//
//
//}