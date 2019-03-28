package uk.ac.chester.testing;

import uk.ac.chester.testing.reflection.MethodsHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleTester<C> {

    private MethodsHelper helper;
    private EventHandler handler;

    public ConsoleTester(Class<C> searchClass, EventHandler handler) {

        this.helper = new MethodsHelper<C>(searchClass);
        this.handler = handler;
    }

    public void test(String... inputTokens){
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        //Route the system output to the above ByteArrayOutputStream
        System.setOut(new PrintStream(out));

        final ByteArrayInputStream stream = streamFromStrings(inputTokens);
        System.setIn(stream);

        //Call the Class's main method
        helper.invokeStaticMethod(true,void.class,"main",(Object)new String[]{});

        if (out.toString().length() > 0){
            handler.outputGenerated(out.toString().split("\n"));
        } else {
            handler.noOutputGenerated();
        }

    }

    private static ByteArrayInputStream streamFromStrings(String[] strings) {
        final StringBuilder sb = new StringBuilder();
        for (String string : strings){
            sb.append(string);
            sb.append("\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    public interface EventHandler{

        void outputGenerated(String[] linesOfOutput);

        void noOutputGenerated();


    }




}
