package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.FieldsTester;
import static org.junit.jupiter.api.Assertions.fail;
import uk.ac.chester.testing.NonAccessModifier;

import java.util.Set;

/**
 * English language implementation of FieldsTester.EventHandler
 */
public class FieldsTestEventHandlerEN implements FieldsTester.EventHandler {

    @Override
    public void fieldNotFound(String name) {
        fail("A field named '" + name +"' is expected, but not found");
    }

    @Override
    public void fieldFoundButNotCorrectType(String fieldName, Class requiredType, Class actualType) {
        fail("The field '" + fieldName + "' should be of type " + requiredType.getSimpleName() + ", however, it is declared as " + actualType.getSimpleName());
    }

    @Override
    public void fieldHasIncorrectModifier(String name, AccessModifier desiredModifier, AccessModifier actualModifier) {
        fail("The field '" + name + "' should be declared as " +desiredModifier+ " but is currently declared as " + actualModifier +".");
    }

    @Override
    public void fieldHasIncorrectNonAccessModifiers(String name, Set<NonAccessModifier> desiredNonAccessModifiers, Set<NonAccessModifier> actualNonAccessModifiers) {

        fail("The field '" + name + "' was expected to have " +
                nonAccessModifiersDescription(desiredNonAccessModifiers) + " but instead had " +
                nonAccessModifiersDescription(actualNonAccessModifiers));
    }

    private String nonAccessModifiersDescription(Set<NonAccessModifier> modifiers) {

        if (modifiers.size() == 0) {
            return "no non-access modifiers";
        } else {
            String plural = modifiers.size() > 1 ? "s" : "";
            StringBuilder builder = new StringBuilder("the following modifier"+ plural+": ");
            boolean first = true;
            for (NonAccessModifier modifier : modifiers) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("'");
                builder.append(modifier);
                builder.append("'");
            }
            return builder.toString();
        }
    }
}
