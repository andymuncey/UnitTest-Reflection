package uk.ac.chester.Testing;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class PackageHelperTest {

    @Test
    public void findClasses() {

        Set<Class> classes = PackageHelper.findClasses("TestClass");

        Assert.assertFalse(classes.isEmpty());





    }
}