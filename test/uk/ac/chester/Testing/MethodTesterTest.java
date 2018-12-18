package uk.ac.chester.Testing;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.Testing.MethodTester;

public class MethodTesterTest {


    @Test
    public void testExecution() {


    }


    @Test
    public void paramNames() {
        Class[] params = {int.class, String.class, double.class};
        String names = MethodTester.paramNames(params);
        Assert.assertEquals("int, java.lang.String, double", names);
    }

    @Test
    public void testWithTolerance() {


    }

}