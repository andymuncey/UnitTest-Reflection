package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class ClassTesterTest {

    static class Book implements Serializable {}

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extendsSuperclass() {
        ClassTester<String> tester = new ClassTester<>(String.class, null);
        assertTrue(tester.extendsSuperclass(Object.class));
        assertFalse(tester.extendsSuperclass(Test.class));

        ClassTester<Book> bookClassTester = new ClassTester<>(Book.class, null);
        assertTrue(bookClassTester.extendsSuperclass(Object.class));
        assertFalse(bookClassTester.extendsSuperclass(Book.class));
    }

    @Test
    void implementsInterface() {
        ClassTester<Book> bookClassTester = new ClassTester<>(Book.class, null);
        assertTrue(bookClassTester.implementsInterface(Serializable.class));
        assertFalse(bookClassTester.implementsInterface(Comparable.class));
    }
}