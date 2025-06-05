package uk.ac.chester.testing.reflection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldsHelperTest {

    @SuppressWarnings("unused")
    private static class FieldsTestClass{
        private int myInt;
        private String myString;
    }

    FieldsHelper<FieldsTestClass> helper;

    @BeforeEach
    void setUp() {
        helper = new FieldsHelper<>(FieldsTestClass.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fields() {
        assertEquals(2, helper.fields().size());
    }

    @Test
    void fieldsIgnoringCase() {
        assertEquals(1,helper.fieldsIgnoringCase("myint").size());

    }

    @Test
    void field() {
        assertTrue(helper.field("myInt").isPresent());
        assertTrue(helper.field("notHere").isEmpty());
    }
}