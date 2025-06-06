package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.handlers.FieldsTestEventHandlerEN;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldsTesterTest {

    private FieldsTester<?> tester;

    @SuppressWarnings("unused")
    private static class TestClass{
        private final String myString = "Hello";
        private String nullString;
        private final int myInt = 4;
        private static double myDouble;
    }

    private TestClass testObject;

    @BeforeEach
    void setUp() {

        testObject = new TestClass();

        tester = new FieldsTester<>(testObject.getClass(),null);

    }

    @AfterEach
    void tearDown() {
        tester = null;
    }

    @Test
    void fieldTypes() {
        assertEquals(2,tester.fieldTypes(true).size());
        assertEquals(3,tester.fieldTypes(false).size());

    }

    @Test
    void getValue() {

        assertEquals(testObject.myInt,tester.getValue(int.class, "myInt",testObject) );
        assertEquals(testObject.myString,tester.getValue(String.class, "myString",testObject) );
        assertNull(tester.getValue(String.class,"nullString",testObject));
        assertThrows(IllegalArgumentException.class, () ->
                tester.getValue(String.class, "myInt", "Not the right object"));
    }

    @Test
    void testFieldTypes() {

        Set<Class<?>> expected = new HashSet<>();

        //add types for fields in TestClass
        expected.add(String.class);
        expected.add(int.class);

        Set<Class<?>> result = tester.fieldTypes(true);
        assertEquals(expected,result);

        //double is static
        expected.add(double.class);
        result = tester.fieldTypes(false);
        assertEquals(expected, result);
    }

    @SuppressWarnings("unused")
    static class SimpleTestClass {
        private String myString;
        private Integer myInteger;
        private int myInt;
    }

    @Test
    public void testFieldType(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);

        //correct field type
        t.test(AccessModifier.PRIVATE,String.class,"myString");

        //incorrect field type
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class,
                () -> t.test(AccessModifier.PRIVATE, int.class, "myString", true));
        assertEquals("fieldFoundButNotCorrectType", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }


    /**
     * Tests that the fieldNotFound method in the handler is called if the specified field does not exist in the tested class
     */
    @Test
    public void testFieldName(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PRIVATE, String.class, "nonExistentField", true));
        assertEquals("fieldNotFound", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }


    /**
     * Tests that with autoboxing not enabled, int and Integer cannot be used interchangeably
     */
    @Test
    public void testFieldTypeNoBoxing(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PRIVATE, int.class, "myInteger", false));
        assertEquals("fieldFoundButNotCorrectType", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void testWrongAccessModifier(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);

        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PROTECTED, Integer.class, "myInteger", false));
        assertEquals("fieldHasIncorrectModifier", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    /**
     * Tests that a field exists with the name myString
     */
    @Test
    public void testExistingFieldName(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);
        t.test(AccessModifier.PRIVATE, String.class, "myString", false);
    }


    /**
     * Tests that if autoboxing is permitted, a field of type Integer is acceptable
     * when type int is requested, and vice versa
     */
    @Test
    public void testFieldTypeBoxed(){
        FieldsTestEventHandlerEN handler = new FieldsTestEventHandlerEN();
        FieldsTester<SimpleTestClass> t = new FieldsTester<>(SimpleTestClass.class, handler);
        t.test(AccessModifier.PRIVATE, int.class, "myInteger", true);
        t.test(AccessModifier.PRIVATE, Integer.class, "myInt", true);
    }
}