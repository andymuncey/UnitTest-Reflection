package uk.ac.chester.testing.reflection;


import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.reflection.PackageHelper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PackageHelperTest {

    @Test
    public void findClasses() {
        Set<Class<?>> classes = PackageHelper.findClasses("TestClass");
        assertFalse(classes.isEmpty());
    }

    @Test
    public void findClass() {
        String packageName = "uk.ac.chester.testing";
        assertTrue(PackageHelper.findClass("TestClass", packageName).isPresent());
        assertFalse(PackageHelper.findClass("NonExistentClass", packageName).isPresent());
    }

}