package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.MethodsHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

public class ConsoleTester<C> {

    private final MethodsHelper helper;

    private CompletionHandler completionHandler;
    private NonCompletionHandler nonCompletionHandler;
    private ExceptionHandler exceptionHandler;

    public void setCompletionHandler(CompletionHandler completionHandler) {
        this.completionHandler = completionHandler;
    }

    public void setNonCompletionHandler(NonCompletionHandler nonCompletionHandler) {
        this.nonCompletionHandler = nonCompletionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }


    public ConsoleTester(Class<C> searchClass) {
        this.helper = new MethodsHelper<>(searchClass);
    }

    private static boolean causedByNoSuchElementException(Throwable e) {
        if (e == null) {
            return false;
        } else {
            if (e instanceof NoSuchElementException) {
                return true;
            } else {
                return causedByNoSuchElementException(e.getCause());
            }
        }
    }

    private static ByteArrayInputStream streamFromStrings(String[] strings) {
        final StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
            sb.append("\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    /**
     * @param timeout seconds to wait for execution
     * @param inputTokens input for System.in
     */
    public void testWithTimeOut(int timeout, String... inputTokens) {

        //adapted from https://stackoverflow.com/questions/2275443/how-to-timeout-a-thread
        Callable<Void> callable = () -> {
            test(inputTokens);
            return null;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(callable);

        try {
            future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            if (nonCompletionHandler != null) {
                nonCompletionHandler.timeout(inputTokens, timeout);
            }
        } catch (InterruptedException ignored) {
            //called if the current thread was interrupted while waiting, shouldn't happen
        } catch (ExecutionException e) {
            if (causedByNoSuchElementException(e)) {
                if (nonCompletionHandler != null){
                    nonCompletionHandler.stillAwaitingInput(inputTokens);
                }
            } else {
                if (exceptionHandler != null){
                    exceptionHandler.otherException(inputTokens, e);
                }
            }
        } finally {
            future.cancel(true);
        }
        executor.shutdown();
    }

    public void test(String... inputTokens) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        //Route the system output to the above ByteArrayOutputStream
        System.setOut(new PrintStream(out));

        final ByteArrayInputStream stream = streamFromStrings(inputTokens);
        System.setIn(stream);

        try {
            //Call the Class's main method (must pass empty string array as the method takes an array of Strings as a parameter
            helper.invokeStaticMethod(true, void.class, "main", (Object) new String[]{});
        } catch (Exception e){
            if (causedByNoSuchElementException(e)) {
                if (nonCompletionHandler != null){
                    nonCompletionHandler.stillAwaitingInput(inputTokens);
                }
            } else if (exceptionHandler != null) {
                exceptionHandler.otherException(inputTokens, e);
            }
            return;
        }

        if (completionHandler != null) {
            if (!out.toString().isEmpty()) {
                completionHandler.outputGenerated(inputTokens, out.toString().split("\n"));
            } else {
                completionHandler.noOutputGenerated(inputTokens);
            }
        }
    }

    public interface CompletionHandler {

        /**
         * Calling the main method with the specified tokens has generated console output
         * @param inputTokens the input supplied
         * @param linesOfOutput the output generated
         */
        void outputGenerated(String[] inputTokens, String[] linesOfOutput);

        /**
         * Calling the main method with the specified tokens has generated no output
         */
        void noOutputGenerated(String[] inputTokens);

    }

    public interface NonCompletionHandler {
        /**
         * Called when the tokens provided should have resulted in the application terminating,
         * but the application would still be awaiting input
         * (When executed with System.in routed to a stream, it actually throws a NoSuchElementException as there are
         * no more elements for the scanner to process
         */
        void stillAwaitingInput(String[] inputTokens);

        /**
         * Called when the application did not finish running in time
         */
        void timeout(String[] inputTokens, int seconds);

    }

    public interface ExceptionHandler {

        /**
         * Indicates a InputMismatchException has been thrown:
         * For example if the programme has tried to read in an int, but the input buffer contained a String
         * @param inputTokens
         */
        void wrongInputType(String[] inputTokens);


        /**
         * Some exception, other than an InputMismatchException or NoSuchElement has been thrown:
         * Typically indicates a bug in the application's code
         * @param inputTokens
         * @param e the exception
         */
        void otherException(String[] inputTokens, Exception e);

    }


}
