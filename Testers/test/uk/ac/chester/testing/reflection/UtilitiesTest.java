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
        assertEquals(int[].class, Utilities.classEquivalent(int[].class),"Arrays of primitive types are already objects");
        assertNotEquals(Integer[].class, Utilities.classEquivalent(int[].class),"int[] and Integer[] are not the same");
    }



    @Test
    public void classEquivalents() {
        Class<?>[] primitives = {int.class, boolean.class, double.class, char.class, String.class};
        Class<?>[] expected = {Integer.class, Boolean.class, Double.class, Character.class, String.class};
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
    }

    @Test
    void primitiveEquivalent() {

        assertEquals(int.class, Utilities.primitiveEquivalent(Integer.class));
        assertEquals(double.class, Utilities.primitiveEquivalent(Double.class));

        assertEquals(String.class, Utilities.primitiveEquivalent(String.class));


        //todo: test Utilities.primitiveEquivalent
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
        assertNotNull(Utilities.unBox(int.class, 42));
        assertNull(Utilities.unBox(Integer.class,4.7));
        assertNull(Utilities.unBox(String.class,"hello"));
        assertDoesNotThrow(() -> Utilities.unBox(int.class, 4));
    }

}