package uk.ac.chester.testing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.reflection.InstanceReflectionHelper;
import uk.ac.chester.testing.testclasses.PointTestClass;

import java.lang.reflect.InvocationTargetException;

public class InstanceMethodsHelperTest {


    private InstanceReflectionHelper<PointTestClass> h;

    @Before
    public void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
           h = new InstanceReflectionHelper<>(PointTestClass.class, 2,3);
    }

    @After
    public void tearDown()  {
        h = null;
    }

    @Test
    public void invokeMethod() throws TestingExecutionException {
//            Assert.assertEquals(Integer.valueOf(2), h.invokeMethod(Integer.class, "getX"));
//            Assert.assertEquals(Integer.valueOf(3), h.invokeMethod(Integer.class, "getY"));
    }

    @Test
    public void getFieldValue() throws TestingExecutionException {
        Integer result = h.fieldValue(int.class, "x");
        Assert.assertEquals(Integer.valueOf(2), result);
    }

}