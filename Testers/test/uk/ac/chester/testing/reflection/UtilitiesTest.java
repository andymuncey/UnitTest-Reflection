package uk.ac.chester.testing.reflection;

import org.junit.Assert;
import org.junit.Test;

public class UtilitiesTest {

    @Test
    public void classesForArgs() {

        Object[] args = {1,"String", 2.3, new Object()};
        Class[] classes = {Integer.class, String.class, Double.class, Object.class};
        Class[] inferredClasses = Utilities.classesForArgs(args);
        for (int i = 0; i < inferredClasses.length; i++) {
            Assert.assertEquals(classes[i],inferredClasses[i]);
        }

        int[] intArgs = {2,3};
        Class[] intClasses = {Integer.class, Integer.class};
        Class[] inferredIntClasses = Utilities.classesForArgs(args);
        for (Class inferredClass: inferredIntClasses) {
            Assert.assertNotEquals("Int should be represented as the Integer class",inferredClass, int.class);
        }

    }

    @Test
    public void classEquivalent() {
        Assert.assertEquals(Integer.class, Utilities.classEquivalent(int.class));
        Assert.assertEquals("Existing class data types unchanged", Double.class, Utilities.classEquivalent(Double.class));
        Assert.assertEquals("Arrays of primitive types are already objects", int[].class, Utilities.classEquivalent(int[].class));
        Assert.assertNotEquals("int[] and Integer[] are not the same", Integer[].class, Utilities.classEquivalent(int[].class));
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