package uk.ac.chester.Testing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClassHelperTest {

    ClassHelper c;

    @Before
    public void setUp() throws Exception {
        c = new ClassHelper("uk.ac.chester.Testing");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findClass() {

        Assert.assertTrue(c.findClass("TestClass").isPresent());
        Assert.assertFalse(c.findClass("NonExistentClass").isPresent());


    }
}