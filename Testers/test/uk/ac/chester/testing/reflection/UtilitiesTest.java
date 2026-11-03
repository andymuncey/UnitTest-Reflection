package uk.ac.chester.testing.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UtilitiesTest {

    @Test
    public void classesForArgs() {

        Object[] args = {1,"String", 2.3, new Object()};
        Class<?>[] classes = {Integer.class, String.class, Double.class, Object.class};
        Class<?>[] inferredClasses = Utilities.classesForArgs(args);
        for (int i = 0; i < inferredClasses.length; i++) {
            assertEquals(classes[i],inferredClasses[i]);
        }

        Integer[] intArgs = {2,3};
        Class<?>[] inferredIntClasses = Utilities.classesForArgs(intArgs);
        for (Class<?> inferredClass: inferredIntClasses) {
            assertNotEquals(int.class, inferredClass, "Int should be represented as the Integer class");
        }
    }

    @Test
    public void classEquivalent() {
        assertEquals(Integer.class, Utilities.classEquivalent(int.class));
        assertEquals(Double.class, Utilities.classEquivalent(Double.class), "Existing class data types unchanged");
        assertEquals(Integer[].class, Utilities.classEquivalent(int[].class),"Arrays of primitive types are already objects");
    }

    @Test
    public void classEquivalents() {
        Class<?>[] primitives = {int.class, boolean.class, double.class, char.class, String.class, int[].class};
        Class<?>[] expected = {Integer.class, Boolean.class, Double.class, Character.class, String.class, Integer[].class};
        assertArrayEquals(expected, Utilities.classEquivalents(primitives));
    }

    @Test
    public void equivalentType() {
        assertTrue(Utilities.equivalentType(boolean.class, Boolean.class));
        assertTrue(Utilities.equivalentType(Boolean.class, boolean.class));
        assertTrue(Utilities.equivalentType(boolean.class, boolean.class));
        assertTrue(Utilities.equivalentType(Boolean.class, Boolean.class));
        assertFalse(Utilities.equivalentType(int.class, double.class));
        assertFalse(Utilities.equivalentType(Integer.class, Double.class));
        assertTrue(Utilities.equivalentType(String.class, String.class));
        assertTrue(Utilities.equivalentType(Integer[].class, int[].class));
        assertTrue(Utilities.equivalentType(Double[].class, double[].class));
        assertTrue(Utilities.equivalentType(double[].class, Double[].class));
    }

    @Test
    void primitiveEquivalent() {
        assertEquals(int.class, Utilities.primitiveEquivalent(Integer.class));
        assertEquals(double.class, Utilities.primitiveEquivalent(Double.class));
        assertEquals(String.class, Utilities.primitiveEquivalent(String.class));
        assertEquals(int[].class, Utilities.primitiveEquivalent(Integer[].class));
    }


    @SuppressWarnings("unused")
    private static class ParamsTestClass{

        ParamsTestClass(String theString, int theInt){

        }

    }

    @Test
    public void parameterNames() {
        try {
           String[] expected = {"theString", "theInt"};
            String[] paramNames = Utilities.parameterNames(ParamsTestClass.class.getDeclaredConstructor(String.class, int.class));
           assertArrayEquals(expected, paramNames);
        } catch (NoSuchMethodException e) {
            fail("Error getting constructor for testing: " + e.getMessage());
        }
    }

    @Test
    public void sortTypesByName() {
        Class<?>[] types = {String.class, int.class, double.class, float.class, Float.class};
        Utilities.sortTypesByName(types);
        //Float and String are java.lang.Float and java.lang.String
        Class<?>[] expected = {double.class, float.class, int.class, Float.class, String.class};
        assertArrayEquals(expected,types);
    }

    @Test
    void unBox() {
        assertEquals(Boolean.TRUE, Utilities.unBox(boolean.class, true));
        assertEquals(Boolean.FALSE, Utilities.unBox(boolean.class, false));

        Byte maxByte = Byte.MAX_VALUE;
        assertEquals(Byte.MAX_VALUE, Utilities.unBox(byte.class, maxByte));

        Character letter = 'c';
        assertEquals('c', Utilities.unBox(char.class, letter));
        assertEquals(23.4, Utilities.unBox(double.class, 23.4));
        assertEquals(23.4f, Utilities.unBox(float.class, 23.4f));
        assertEquals(42,Utilities.unBox(int.class, 42));
        assertEquals(42L, Utilities.unBox(long.class, 42L));

        Short maxShort = Short.MAX_VALUE;
        assertEquals(Short.MAX_VALUE, Utilities.unBox(short.class, maxShort));

        assertDoesNotThrow(() -> Utilities.unBox(int.class, 4));

        assertEquals(Boolean.TRUE, Utilities.unBox(boolean.class, true));
        assertNotEquals(Boolean.TRUE, Utilities.unBox(boolean.class, Boolean.FALSE));

        assertNull(Utilities.unBox(Boolean.class,true), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(Byte.class, maxByte), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(Character.class, 'c'), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(Double.class, 2.3), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(Float.class, 1.54F), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(Integer.class,4.7), "cannot unbox to a boxed type");
        assertNull(Utilities.unBox(String.class,"hello"), "cannot unbox to a boxed type");


    }

    @Test
    void commaSeparatedArgList() {
        Object[] args = {1,"hello", 3.4};
        String result =  Utilities.commaSeparatedArgList(args);
        assertEquals("1, \"hello\", 3.4", result);

        Object[] argsWithNull = {1,"hello", 3.4, null};
        String resultWithNull =  Utilities.commaSeparatedArgList(argsWithNull);
        assertEquals("1, \"hello\", 3.4, null", resultWithNull);

        Integer one = 1;
        Integer two = 2;
        Object[] argsWithBoxedTypes = {one, two};
        String resultWithBoxedTypes = Utilities.commaSeparatedArgList(argsWithBoxedTypes);
        assertEquals("1, 2", resultWithBoxedTypes);

        class Book {}
        Book book = new Book();
        Object[] testWithClass = {book};
        String resultWithClass =  Utilities.commaSeparatedArgList(testWithClass);
        assertEquals(book.toString(), resultWithClass);

    }

}