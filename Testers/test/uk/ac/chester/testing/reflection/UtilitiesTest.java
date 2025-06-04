package uk.ac.chester.testing.reflection;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    void unBox() {

        Integer x = 4;

        assertNotNull(Utilities.unBox(int.class, 42));
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.unBox(Integer.class,4);
            fail("passing a boxed type as the return type is not allowed");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.unBox(Integer.class,4.7);
            fail("types must match");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Utilities.unBox(String.class,"hello");
            fail("passing a non primitive type as the return type is not allowed");
        });
        assertEquals(Integer.class, Utilities.unBox(int.class, x).getClass());






    }



    //copied from https://www.baeldung.com/java-object-primitive-type

    public static boolean isPrimitiveType(Object source) {
        final Map<Class<?>, Class<?>> WRAPPER_TYPE_MAP;

            WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(16);
            WRAPPER_TYPE_MAP.put(Integer.class, int.class);
            WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
            WRAPPER_TYPE_MAP.put(Character.class, char.class);
            WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
            WRAPPER_TYPE_MAP.put(Double.class, double.class);
            WRAPPER_TYPE_MAP.put(Float.class, float.class);
            WRAPPER_TYPE_MAP.put(Long.class, long.class);
            WRAPPER_TYPE_MAP.put(Short.class, short.class);
            WRAPPER_TYPE_MAP.put(Void.class, void.class);

        return WRAPPER_TYPE_MAP.containsKey(source.getClass());
    }
}