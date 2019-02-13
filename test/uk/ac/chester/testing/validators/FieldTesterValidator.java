package uk.ac.chester.testing.validators;

import org.junit.Test;
import uk.ac.chester.testing.FieldTestEventHandlerEN;
import uk.ac.chester.testing.FieldTester;
import uk.ac.chester.testing.TestClass;

public class FieldTesterValidator {

    @Test
    public void testPublicField() {
        Object x = new Object(){
            public String publicField;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t  = new FieldTester<>(x.getClass(), handler);
        t.testFields();
    }

    @Test
    public void testBadFieldName() {
        Object x = new Object(){
            private String MY_STRING;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t  = new FieldTester<>(x.getClass(),  handler);
        t.testFields();
    }

    @Test
    public void testFieldStaticButNotFinal() {
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t  = new FieldTester<>(TestClass.class,  handler);
        t.testFields();
    }


}
