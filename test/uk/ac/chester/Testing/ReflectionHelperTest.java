package uk.ac.chester.Testing;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReflectionHelperTest {

    ReflectionHelper h;

    @Before
    public void setUp() throws Exception {
        h = new ReflectionHelper(TestClass.class);
    }

    @After
    public void tearDown() throws Exception {
        h = null;
    }

    @Test
    public void invokeMethod() {
        Assert.assertEquals(1,(int)h.invokeMethod(int.class,"returnsPrimitiveInt"));
        Assert.assertEquals(Integer.valueOf(1),(Integer)h.invokeMethod(Integer.class,"returnsInteger"));
    }

    @Test
    public void invokeMethodStrict() {

        Assert.assertEquals(1,(int)h.invokeMethod(true, int.class,"returnsPrimitiveInt"));

        //should not find method returning int
        Assert.assertNull(h.invokeMethod(true, int.class, "returnsInteger"));
        Assert.assertNull(h.invokeMethod(true, Integer.class, "returnsPrimitiveInt"));


    }

    @Test
    public void typesForArgs() {

        Assert.assertNotEquals(Integer.class, int.class); //just as a reminder
        Assert.assertNotEquals(Void.class, void.class); //also a reminder

        Object[] args = {1,5000000000L,1.5F, 2.345,'a',"text"};
        Class[] actualTypes = h.classesForArgs(args);
        Class[] expectedTypes = {Integer.class, Long.class, Float.class, Double.class, Character.class, String.class};
        Assert.assertArrayEquals(expectedTypes,actualTypes);

        Class[] primitiveTypes = {int.class, long.class, float.class, double.class, char.class, String.class};
        Assert.assertThat(primitiveTypes, IsNot.not(IsEqual.equalTo(actualTypes)));

    }

    @Test
    public void invoke() {
    }

    @Test
    public void findMethods() {
        //name and return type
        Assert.assertFalse("Primitive int type",h.findMethods(int.class, "returnsPrimitiveInt").isEmpty());
        Assert.assertTrue("Primitive int type", h.findMethods(Integer.class, "returnsPrimitiveInt").isEmpty());

        Assert.assertFalse("Class Integer type", h.findMethods(Integer.class, "returnsInteger").isEmpty());
        Assert.assertTrue("Class Integer type", h.findMethods(int.class, "returnsInteger").isEmpty());
    }

    @Test
    public void findMethodsNotStrict() {
        Assert.assertFalse("Primitive int type", h.findMethods(Integer.class,"returnsPrimitiveInt", false).isEmpty());
        Assert.assertFalse("Class Integer type", h.findMethods(int.class,"returnsInteger",  false).isEmpty());
    }

    @Test
    public void findMethod() {

        Assert.assertTrue(h.findMethod(int.class,"returnsPrimitiveInt", true).isPresent());
        Assert.assertFalse(h.findMethod(Integer.class,"returnsPrimitiveInt", true).isPresent());
        Assert.assertTrue(h.findMethod(Integer.class,"returnsPrimitiveInt", false).isPresent());

        Assert.assertTrue(h.findMethod(Integer.class,"returnsInteger", true).isPresent());
        Assert.assertFalse(h.findMethod(int.class,"returnsInteger", true).isPresent());
        Assert.assertTrue(h.findMethod(int.class,"returnsInteger", false).isPresent());

        Assert.assertTrue(h.findMethod(void.class,"noReturn",true).isPresent());
        Assert.assertFalse(h.findMethod(Void.class,"noReturn",true).isPresent());
        Assert.assertTrue(h.findMethod(Void.class,"noReturn",false).isPresent());


    }

    @Test
    public void methodsWithSignature() {
    }

    @Test
    public void methodsWithSignature1() {
    }
}