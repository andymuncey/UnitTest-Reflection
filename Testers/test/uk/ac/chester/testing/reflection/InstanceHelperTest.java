package uk.ac.chester.testing.reflection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.TestingExecutionException;
import uk.ac.chester.testing.testclasses.PointTestClass;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstanceHelperTest {


    private InstanceHelper<PointTestClass> h;

    @BeforeEach
    public void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
           h = new InstanceHelper<>(PointTestClass.class, 2,3);
    }

    @AfterEach
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
        assertEquals(Integer.valueOf(2), result);
    }



}