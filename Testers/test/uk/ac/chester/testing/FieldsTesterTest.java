package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldsTesterTest {

    private FieldsTester<?> tester;

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
}