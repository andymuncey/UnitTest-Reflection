package uk.ac.chester.testing.handlers;

import org.jetbrains.annotations.NotNull;
import uk.ac.chester.testing.ConsoleTester;

public class ConsoleTestCompletionHandlerEN implements ConsoleTester.CompletionHandler{
    @Override
    public void outputGenerated(String[] inputTokens, @NotNull String[] linesOfOutput) {
        System.out.println("The following output was generated:");
        for (String line: linesOfOutput){
            System.out.println(line);
        }
        System.out.println();
        System.out.println("Input was as follows");
        for (String line: inputTokens){
            System.out.println(line);
        }
    }

    @Override
    public void noOutputGenerated(String[] inputTokens) {
        System.out.println("No output was generated for the following input");
        for (String line: inputTokens){
            System.out.println(line);
        }
    }
}
