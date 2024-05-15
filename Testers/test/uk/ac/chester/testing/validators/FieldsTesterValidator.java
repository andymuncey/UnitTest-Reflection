package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.FieldsTester;
import uk.ac.chester.testing.handlers.FieldsTestEventHandlerEN;

public class FieldsTesterValidator {

    //region failing tests - These tests are meant to fail!

    @Test
    public void testFieldType(){
        Object x = new Object(){
            private String myString;
        };
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, int.class, "myString", true);
    }


    @Test
    public void testFieldName(){
        Object x = new Object(){
            private String myString;
        };
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, String.class, "nonExistentField", true);
    }


    /**
     * Tests that with autoboxing not enabled, int and Integer cannot be used interchangeably
     */
    @Test
    public void testFieldTypeNoBoxing(){
        Object x = new Object(){
            private Integer myInteger;
        };
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, int.class, "myInteger", false);
    }

    @Test
    public void testWrongAccessModifier(){
        Object x = new Object(){
            Integer myInteger;
        };
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, Integer.class, "myInteger", false);
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
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, String.class, "myString", false);
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
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester t = new FieldsTester<>(x.getClass(), handler);
        t.test(AccessModifier.PRIVATE, int.class, "myInteger", true);
        t.test(AccessModifier.PRIVATE, Integer.class, "myInt", true);
    }


    //endregion

}
