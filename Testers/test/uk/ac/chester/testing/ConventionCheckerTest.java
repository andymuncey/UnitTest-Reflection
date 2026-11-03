package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConventionCheckerTest {

    ConventionChecker checker;

    @BeforeEach
    void setUp() {
        checker = new ConventionChecker(){};
    }

    @AfterEach
    void tearDown() {
        checker = null;
    }

    @Test
    void validVariableName() {
        assertTrue(checker.validVariableName("i"));
    }

    @Test
    void validMethodName() {
        assertFalse(checker.validMethodName("x"));
        assertTrue(checker.validMethodName("getItem"));

    }

    @Test
    void validClassConstantName() {
        assertFalse(checker.validClassConstantName("pi"));
        assertTrue(checker.validClassConstantName("PI"));
        assertTrue(checker.validClassConstantName("SNAKE_CASE_EXAMPLE"));
        assertFalse(checker.validClassConstantName("_SNAKE_CASE_EXAMPLE"));
        assertFalse(checker.validClassConstantName("SNAKE_CASE_EXAMPLE_"));
        assertFalse(checker.validClassConstantName("snake_case_example"));

        assertFalse(checker.validClassConstantName("X"), "one letter is not sufficient");
        assertFalse(checker.validClassConstantName("x"));
    }

    @Test
    void startsWithLowerChar() {
        assertTrue(checker.startsWithLowerChar("uuid"));
        assertFalse(checker.startsWithLowerChar("UUID"));
        assertFalse(checker.startsWithLowerChar(""));
    }
}