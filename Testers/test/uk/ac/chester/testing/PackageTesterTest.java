package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.handlers.PackageTestEventHandlerEN;

import static org.junit.jupiter.api.Assertions.*;

class PackageTesterTest {

    PackageTester tester;
    PackageTestEventHandlerEN handler;

    private final String thisPackageName = this.getClass().getPackage().getName();

    @BeforeEach
    void setUp() {
        handler = new PackageTestEventHandlerEN();
        tester = new PackageTester(handler);
    }

    @AfterEach
    void tearDown() {
        tester = null;
    }

    @Test
    void testGetClass() {

        //find any class
        assertEquals(tester.getClass("PackageTesterTest",thisPackageName),this.getClass());
        assertNull(tester.getClass("NonExistentClass",thisPackageName), "Shouldn't find non existent class in real package");
        assertNull(tester.getClass("PackageTesterTest","wrong.package"), "Shouldn't find real class in non-existent package");
        assertNull(tester.getClass("NonExistentClass","wrong.package"), "Shouldn't find non-existent class in non-existent package");

    }

    @Test
    void testClassExists() {

        //this package
        assertDoesNotThrow(() -> tester.testClassExists("PackageTesterTest",thisPackageName));

        //found in other packages
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("foundInWrongPackage",() -> tester.testClassExists("PackageTesterTest","wrong.package"));

        //wrong name variant
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("foundWrongCase",() -> tester.testClassExists("packageTesterTest",thisPackageName));

        //wrong name in other package
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("foundWrongCaseAndPackage",() -> tester.testClassExists("packageTesterTest","wrong.package"));

        //not found at all
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("notFound",() -> tester.testClassExists("ClassDoesNotExist","wrong.package"));

    }
}