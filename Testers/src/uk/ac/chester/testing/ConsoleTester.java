package uk.ac.chester.testing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.ac.chester.testing.reflection.MethodsHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

/**
 * KNOWN ISSUES:
 *  * The first instance of Scanner for System.in used will consume all input tokens
 *  * using a single static scanner is advisable
 * @param <C> the class to be tested (e.g. Main)
 */
public class ConsoleTester<C> {

    private final MethodsHelper<C> helper;
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



    /*todo: Currently only facilitates testing static methods,
    consider inheriting from method tester so instance methods can be tested*/

    /**
     * Creates a ConsoleTester preconfigured to test a static void main(String[] args) method
     * @param aClass the class to test (e.g. Main)
     */
    public ConsoleTester(Class<C> aClass) {
        this.helper = new MethodsHelper<>(aClass);
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
            sb.append(System.lineSeparator());
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

    public <T> boolean methodCompletesWithNonNullResult(int timeout, @NotNull Class<T> returnType, @NotNull String methodName, String[] inputTokens, Object... args ) {

        //adapted from https://stackoverflow.com/questions/2275443/how-to-timeout-a-thread
        Callable<T> callable = () -> {
            T result = helper.invokeStaticMethod(true,returnType,methodName,inputTokens,args);
           // test(inputTokens);
            return result;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callable);

        try {
            T result = future.get(timeout, TimeUnit.SECONDS);
            if (result != null) {
                return true;
            }

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
            executor.shutdown();
        }
        return false;

    }



    /**
     * Tests the main method with an empty array of Strings
     * @param inputTokens the input which would be input at the command line
     */
    public void test(String... inputTokens) {
        test(void.class, "main", inputTokens, (Object)new String[]{});
    }

    /**
     *
     * @param returnType the class of the type the method to be called returns
     * @param methodName name of the method to call
     * @param args parameters passed to the method (use an empty array if none)
     * @param inputTokens values for System.in
     * @param <T> the type expected to be returned
     * @return the result of executing the method or null if an exception is thrown
     */
    @Nullable
    public <T> T test(@NotNull Class<T> returnType, @NotNull String methodName, String[] inputTokens, Object... args ) {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        final ByteArrayInputStream stream = streamFromStrings(inputTokens);
        System.setIn(stream);

        T result;
        try {
           result =  helper.invokeStaticMethod(true, returnType, methodName, args);
        } catch (Exception e){
            if (causedByNoSuchElementException(e)) {
                if (nonCompletionHandler != null){
                    nonCompletionHandler.stillAwaitingInput(inputTokens);
                }
            } else if (exceptionHandler != null) {
                exceptionHandler.otherException(inputTokens, e);
            }
            return null;
        }

        if (completionHandler != null) {
            if (out.toString().isEmpty()) {
                completionHandler.noOutputGenerated(inputTokens);
            } else {
                completionHandler.outputGenerated(inputTokens, out.toString().split(System.lineSeparator()));
            }
        }
        return result;
    }

    /**
     * Gets the lines of output generated by a console application (in the first 250ms of execution) before it reads any data
     * @return output lines of output
     */
    @Nullable
    public String[] outputGeneratedBeforeInputProcessed(){

        //todo: should be able to use this approach to get the state of System.out after any number of tokens

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Callable<Void> callable = () -> {
            System.setOut(new PrintStream(out));

            try {
                //Call the Class's main method (must pass empty string array as the method takes an array of Strings as a parameter
                helper.invokeStaticMethod(true, void.class, "main", (Object) new String[]{});
            } catch (Exception ignored){}

            return null;
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(callable);

        try {
            future.get(250, TimeUnit.MILLISECONDS);
        } catch (Exception ignored) {

        }  finally {
            future.cancel(true);
            executor.shutdown();
        }
        if (!out.toString().isEmpty()) {
            return out.toString().split(System.lineSeparator());
        }
        return null;
    }

    public interface CompletionHandler {

        /**
         * Calling the main method with the specified tokens has generated console output
         * @param inputTokens the input supplied
         * @param linesOfOutput the output generated
         */
        void outputGenerated(String[] inputTokens, @NotNull String[] linesOfOutput);

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
         * no more elements for the scanner to process)
         * Be aware that input tokens will be consumed by the first instance of Scanner used, so limit instances of Scanner used to one!
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
         * @param inputTokens the input used
         */
        void wrongInputType(String[] inputTokens);


        /**
         * Some exception, other than an InputMismatchException or NoSuchElement has been thrown:
         * Typically indicates a bug in the application's code
         * @param inputTokens the input used
         * @param e the exception
         */
        void otherException(String[] inputTokens, Exception e);

    }


}
