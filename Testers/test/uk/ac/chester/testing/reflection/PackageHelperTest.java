package uk.ac.chester.testing.reflection;


import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.reflection.PackageHelper;
import uk.ac.chester.testing.testclasses.TestClass;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PackageHelperTest {

    @Test
    public void findClasses() {

        //need to declare class here as package will not be loaded otherwise
        @SuppressWarnings("unused")
        TestClass c = new TestClass();

        Set<Class<?>> classes = PackageHelper.findClasses("TestClass");
        assertFalse(classes.isEmpty());
    }

    @Test
    public void findClass() {
        String packageName = "uk.ac.chester.testing.testclasses";
        assertTrue(PackageHelper.findClass("TestClass", packageName).isPresent());
        assertFalse(PackageHelper.findClass("NonExistentClass", packageName).isPresent());
    }

}