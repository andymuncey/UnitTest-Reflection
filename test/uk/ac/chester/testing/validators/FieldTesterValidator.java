package uk.ac.chester.testing.validators;

import org.junit.Test;
import uk.ac.chester.testing.FieldTestEventHandlerEN;
import uk.ac.chester.testing.FieldTester;
import uk.ac.chester.testing.TestClass;

public class FieldTesterValidator {


    //region failing tests

    //These tests are meant to fail!

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

    @Test
    public void testFieldType(){
        Object x = new Object(){
            private String myString;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t = new FieldTester<>(x.getClass(), handler);
        t.testSpecificField("myString",int.class,true);
    }


    @Test
    public void testFieldName(){
        Object x = new Object(){
            private String myString;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t = new FieldTester<>(x.getClass(), handler);
        t.testSpecificField("nonExistentField",String.class,true);
    }


    /**
     * Tests that with autoboxing not enabled, int and Integer cannot be used interchangeably
     */
    @Test
    public void testFieldTypeNoBoxing(){
        Object x = new Object(){
            private Integer myInteger;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t = new FieldTester<>(x.getClass(), handler);
        t.testSpecificField("myInteger",int.class,false);
    }
    //end region


    //region passing tests


    /**
     * Tests that a field exists with the name myString
     */
    @Test
    public void testExistingFieldName(){
        Object x = new Object(){
            private String myString;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t = new FieldTester<>(x.getClass(), handler);
        t.testSpecificField("myString",String.class,false);
    }


    /**
     * Tests that if autoboxing is permitted, a field of type Integer is acceptable
     * when type int is requested, and vice-versa
     */
    @Test
    public void testFieldTypeBoxed(){
        Object x = new Object(){
            private Integer myInteger;
            private int myInt;
        };
        FieldTestEventHandlerEN handler = new FieldTestEventHandlerEN();
        FieldTester t = new FieldTester<>(x.getClass(), handler);
        t.testSpecificField("myInteger",int.class,true);
        t.testSpecificField("myInt",Integer.class,true);
    }


    //endregion

}
