package uk.ac.chester.testing.validators;

import org.junit.Test;
import uk.ac.chester.testing.ClassTester;
import uk.ac.chester.testing.TestClass;
import uk.ac.chester.testing.handlers.ClassTestEventHandlerEN;

public class ClassTesterValidator {

    @Test
    public void testPublicField() {
        Object x = new Object(){
            public String publicField;
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester t  = new ClassTester<>(x.getClass(), handler);
        t.checkFields();
    }

    @Test
    public void testBadFieldName() {
        Object x = new Object(){
            private String MY_STRING;
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester t  = new ClassTester<>(x.getClass(),  handler);
        t.checkFields();
    }

    @Test
    public void testFieldStaticButNotFinal() {
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester t  = new ClassTester<>(TestClass.class,  handler);
        t.checkFields();
    }

    @Test
    public void testMethodNameInvalid(){
        Object x = new Object(){
            private String BadMethodName(){return "hello";};
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester t  = new ClassTester<>(x.getClass(),  handler);
        t.checkMethods();
    }
}
