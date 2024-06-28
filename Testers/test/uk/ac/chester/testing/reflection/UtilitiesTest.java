package uk.ac.chester.testing.reflection;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UtilitiesTest {

    @Test
    public void classesForArgs() {

        Object[] args = {1,"String", 2.3, new Object()};
        Class<?>[] classes = {Integer.class, String.class, Double.class, Object.class};
        Class<?>[] inferredClasses = Utilities.classesForArgs(args);
        for (int i = 0; i < inferredClasses.length; i++) {
            assertEquals(classes[i],inferredClasses[i]);
        }

        int[] intArgs = {2,3};
        Class<?>[] intClasses = {Integer.class, Integer.class};
        Class<?>[] inferredIntClasses = Utilities.classesForArgs(args);
        for (Class<?> inferredClass: inferredIntClasses) {
            assertNotEquals(inferredClass, int.class, "Int should be represented as the Integer class");
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


    }

    @Test
    public void equivalentType() {
    }

    @Test
    public void parameterNames() {

    }

    @Test
    public void sortParamsTypesByName() {
    }
}