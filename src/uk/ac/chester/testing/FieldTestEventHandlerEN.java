package uk.ac.chester.testing;

import org.junit.Assert;

/**
 * English language implementation of FieldTester.FieldsTestEventHandler
 */
public class FieldTestEventHandlerEN implements FieldTester.FieldsTestEventHandler {

    @Override
    public void nonPrivateFieldFound(String fieldName) {
        Assert.fail("Field '" + fieldName + "' is not private. For proper encapsulation fields should normally be private with an accessor and mutator being used for each field as required.");
    }

    @Override
    public void fieldNameUnconventional(String fieldName, boolean isStatic) {
        String message = isStatic ? "Static variables should be all uppercase, with words separated by underscores" : "Fields (a.k.a. instance variables), should be named using lowerCamelCase, and be longer than a single character in most cases";
        Assert.fail("Field '" + fieldName + "' doesn't match naming conventions: " + message);
    }

    @Override
    public void fieldStaticButNotFinal(String fieldName) {
        Assert.fail("The field '" + fieldName + "' is declared as static, but not final. It is highly unusual to have a class variable like this. " +
                "If the value should belong to an instance of the class, you should remove the static keyword, " +
                "if it's a class constant, then you should add the final keyword");
    }

    @Override
    public void fieldNotFound(String name) {
        Assert.fail("A field named '" + name +"' is expected, but not found");
    }

    @Override
    public void fieldFoundButNotCorrectType(String fieldName, Class requiredType, Class actualType) {
        Assert.fail("The field '" + fieldName + "' should be of type " + requiredType.getSimpleName() + ", however, it is declared as " + actualType.getSimpleName());
    }

}
