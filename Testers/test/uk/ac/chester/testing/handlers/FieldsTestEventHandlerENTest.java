package uk.ac.chester.testing.handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.NonAccessModifier;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldsTestEventHandlerENTest {

    FieldsTestEventHandlerEN handler;

    @BeforeEach
    void setUp() {
        handler = new FieldsTestEventHandlerEN();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fieldHasIncorrectNonAccessModifiers() {

        Set<NonAccessModifier> intendedNonAccessModifiers = new HashSet<>();
        intendedNonAccessModifiers.add(NonAccessModifier.VOLATILE);
        intendedNonAccessModifiers.add(NonAccessModifier.FINAL);

        Set<NonAccessModifier> actualnonAccessModifiers = new HashSet<>();
        actualnonAccessModifiers.add(NonAccessModifier.SYNCHRONIZED);

        assertThrows(AssertionFailedError.class, ()->{
            handler.fieldHasIncorrectNonAccessModifiers("fieldName",intendedNonAccessModifiers, actualnonAccessModifiers);
        });


    }
}