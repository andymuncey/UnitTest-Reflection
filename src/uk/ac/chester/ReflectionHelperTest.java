package uk.ac.chester;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

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
    }

    @Test
    public void typesForArgs() {

        Assert.assertNotEquals(Integer.class, int.class); //just as a reminder

        Object[] args = {1,5000000000L,1.5F, 2.345,'a',"text"};
        Class[] actualTypes = h.typesForArgs(args);
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
        Assert.assertFalse("Primitive int type",h.findMethods("returnsPrimitiveInt", int.class).isEmpty());
        Assert.assertTrue("Primitive int type", h.findMethods("returnsPrimitiveInt",Integer.class).isEmpty());

        Assert.assertFalse("Class Integer type", h.findMethods("returnsInteger", Integer.class).isEmpty());
        Assert.assertTrue("Class Integer type", h.findMethods("returnsInteger", int.class).isEmpty());
    }

    @Test
    public void findMethodsNotStrict() {
        Assert.assertFalse("Primitive int type", h.findMethods("returnsPrimitiveInt",Integer.class, false).isEmpty());
        Assert.assertFalse("Class Integer type", h.findMethods("returnsInteger", int.class, false).isEmpty());
    }

    @Test
    public void findMethod() {
    }

    @Test
    public void methodsWithSignature() {
    }

    @Test
    public void methodsWithSignature1() {
    }
}