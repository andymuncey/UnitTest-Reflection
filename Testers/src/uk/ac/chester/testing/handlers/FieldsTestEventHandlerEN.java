package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.FieldsTester;
import org.junit.Assert;
import uk.ac.chester.testing.NonAccessModifier;

import java.util.Set;

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

    @Override
    public void fieldHasIncorrectNonAccessModifiers(String name, Set<NonAccessModifier> desiredNonAccessModifiers, Set<NonAccessModifier> actualNonAccessModifiers) {

        Assert.fail("The field '" + name + "' was expected to have " +
                nonAccessModifiersDescription(desiredNonAccessModifiers) + " but instead had " +
                nonAccessModifiersDescription(actualNonAccessModifiers));
    }

    private String nonAccessModifiersDescription(Set<NonAccessModifier> modifiers) {

        if (modifiers.size() == 0) {
            return "no non-access modifiers";
        } else {
            StringBuilder builder = new StringBuilder("the following modifiers: ");
            boolean first = true;
            for (NonAccessModifier modifier : modifiers) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(modifier);
            }
            return builder.toString();
        }
    }
}
