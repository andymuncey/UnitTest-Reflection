package uk.ac.chester.testing.validators;


import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.ClassTester;
import uk.ac.chester.testing.testclasses.TestClass;
import uk.ac.chester.testing.handlers.ClassTestEventHandlerEN;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTesterTest {

    @Test
    public void testPublicField() {
        Object x = new Object(){
            public String publicField;
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(x.getClass(), handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkFields);
        assertEquals("fieldNotPrivate", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    @Test
    public void testBadFieldName() {
        Object x = new Object(){
            private String MY_STRING;
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(x.getClass(),  handler);

        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkFields);
        assertEquals("fieldNameUnconventional", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void testSerialVersionUID() {
        Object x = new Object() {
            private static final long serialVersionUID = 1L;
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(x.getClass(),  handler);
        t.checkFields();
    }

    @Test
    public void testFieldStaticButNotFinal() {
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(TestClass.class,  handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkFields);
        assertEquals("fieldStaticButNotFinal", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void testMethodNameInvalid(){
        Object x = new Object(){
            private String BadMethodName(){return "hello";}
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(x.getClass(),  handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkMethods);
        assertEquals("methodNameUnconventional", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }


    @Test
    public void testMethodParamNameInvalid(){
        Object x = new Object(){
            private String someMethod(String BadName){return "hello";}
        };
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(x.getClass(),  handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkMethodParameterNames);
        assertEquals("methodParameterNameUnconventional", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }


    private class BadConstructorParamClass {
        private BadConstructorParamClass(String BadConstructorParamName){ }
    }
    @Test
    public void testConstructorParamName(){
        ClassTestEventHandlerEN handler = new ClassTestEventHandlerEN();
        ClassTester<?> t  = new ClassTester<>(BadConstructorParamClass.class,  handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, t::checkConstructorParameterNames);
        assertEquals("constructorParameterNameUnconventional", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }



}
