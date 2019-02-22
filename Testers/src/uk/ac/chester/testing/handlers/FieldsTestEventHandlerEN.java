package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.FieldsTester;
import org.junit.Assert;

/**
 * English language implementation of FieldsTester.EventHandler
 */
public class FieldsTestEventHandlerEN implements FieldsTester.EventHandler {

    @Override
    public void fieldNotFound(String name) {
        Assert.fail("A field named '" + name +"' is expected, but not found");
    }

    @Override
    public void fieldFoundButNotCorrectType(String fieldName, Class requiredType, Class actualType) {
        Assert.fail("The field '" + fieldName + "' should be of type " + requiredType.getSimpleName() + ", however, it is declared as " + actualType.getSimpleName());
    }

    @Override
    public void fieldHasIncorrectModifier(String name, AccessModifier desiredModifier, AccessModifier actualModifier) {
        Assert.fail("The field '" + name + "' should be declared as " +desiredModifier+ " but is currently declared as " + actualModifier +".");
    }
}
