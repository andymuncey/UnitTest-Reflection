package uk.ac.chester.testing.handlers;

import uk.ac.chester.testing.ClassTester;
import static org.junit.jupiter.api.Assertions.fail;

public class ClassTestEventHandlerEN implements ClassTester.EventHandler {

    @Override
    public void methodNameUnconventional(String name) {
        String message = "Methods '" +name+ "' does not follow Java naming conventions. " +
                "It should be named using lowerCamelCase, and be a verb";
        fail(message);
    }

    @Override
    public void methodParameterNameUnconventional(String paramName, String methodName) {
        fail("The parameter '"+paramName+"' in method '"+methodName+"' does not follow the Java naming convention");
    }

    @Override
    public void fieldNotPrivate(String fieldName) {
        fail("Field '" + fieldName + "' is not private. For proper encapsulation fields should normally be private with an accessor and mutator being used for each field as required.");
    }

    @Override
    public void fieldNameUnconventional(String fieldName, boolean isStatic) {
        String message = isStatic ? "Static variables should be all uppercase, with words separated by underscores" : "Fields (a.k.a. instance variables), should be named using lowerCamelCase, and be longer than a single character in most cases";
        fail("Field '" + fieldName + "' doesn't match naming conventions: " + message);
    }

    @Override
    public void fieldStaticButNotFinal(String fieldName) {
        fail("The field '" + fieldName + "' is declared as static, but not final. It is highly unusual to have a class variable like this. " +
                "If the value should belong to an instance of the class, you should remove the static keyword, " +
                "if it's a class constant, then you should add the final keyword");
    }

    @Override
    public void constructorParameterNameUnconventional(String paramName) {
        fail("A constructor has a parameter named '" +paramName+ "' which does not follow the Java naming convention");
    }
}
