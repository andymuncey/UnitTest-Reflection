package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldsTesterTest {

    private FieldsTester<?> tester;

    @BeforeEach
    void setUp() {

        Object x = new Object(){
            private String myString;
            private String anotherString;
            private int myInt;
            private static double myDouble;
        };

        tester = new FieldsTester<>(x.getClass(),null);

    }

    @AfterEach
    void tearDown() {
        tester = null;
    }

    @Test
    void fieldTypes() {
        assertEquals(2,tester.fieldTypes(true).size());
        assertEquals(3,tester.fieldTypes(false).size());

    }
}