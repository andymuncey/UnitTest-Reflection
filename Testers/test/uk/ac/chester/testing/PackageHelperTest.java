package uk.ac.chester.testing;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class PackageHelperTest {

    @Test
    public void findClasses() {
        Set<Class> classes = PackageHelper.findClasses("TestClass");
        Assert.assertFalse(classes.isEmpty());
    }

    @Test
    public void findClass() {
        String packageName = "uk.ac.chester.testing";
        Assert.assertTrue(PackageHelper.findClass("TestClass", packageName).isPresent());
        Assert.assertFalse(PackageHelper.findClass("NonExistentClass", packageName).isPresent());
    }

}